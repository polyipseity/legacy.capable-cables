package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.ICollectionDelegated;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IDirty;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Casts;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IDirty.isDirty;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

/**
 * Contains a group of {@link IDrawable}.
 * <p>
 * Used for grouping a bunch of {@code IDrawable} into one {@code IDrawable}.
 *
 * @author William So
 * @param <N> type of {@link Number} used for {@link Rectangle}
 * @param <C> type of {@link Collection} used for storing {@code E}
 * @param <E> type of {@link IDrawable} of the children
 * @param <T> type of this object
 * @see IDrawable
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiGroup<N extends Number, C extends Collection<E>, E extends IDrawable<N, ?>, T extends GuiGroup<N, C, E, T>> extends Gui implements IDrawable<N, T>, ICollectionDelegated<C, E, T> {
	/* SECTION variables */

	/**
	 * Group of {@link E}.
	 *
	 * @since 0.0.1.0
	 */
	protected C children;
	protected long dirtiness;
	@SuppressWarnings("unused")
	protected final IDirty dirtyCustom = new IDirty() {
		/* SECTION methods */

		@Override
		public void markDirty() { getChildren().forEach(IDirty::markDirty); }

		@Override
		public long getDirtiness() { return getChildren().stream().mapToLong(IDirty::getDirtiness).sum(); }
	};

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@Nullable
	protected Optional<Rectangle.Immutable<N, ?>> cachedSpec;
	protected final AtomicLong cachedSpecDirtiness = new AtomicLong();


	/* SECTION constructors */

	/**
	 * Creates a group containing the provided {@code children}.
	 *
	 * @param children {@link #children} of this group
	 * @see #GuiGroup(E...) varargs version
	 * @since 0.0.1.0
	 */
	public GuiGroup(C children) { this.children = children; }

	/**
	 * See {@link #GuiGroup(Collection)}.
	 * <p>
	 * Parameter {@code children} is in varargs form for convenience.
	 *
	 * @implNote
	 * Type parameter {@link C} is considered compatible with
	 * {@code java.util.Arrays.ArrayList}.
	 * Will result in {@link ClassCastException} at a undefined time if not so.
	 *
	 * @param children {@link #children} of this group
	 * @see #GuiGroup(Collection) {@link Collection} version
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiGroup(E... children) { this(Casts.<C>castUncheckedUnboxedNonnull(Arrays.asList(children))); }

	/**
	 * Shallow copy constructor.
	 *
	 * @param copy to-be-shallow-copied object
	 * @see #clone() as-deep-as-possible clone method
	 * @since 0.0.1.0
	 */
	public GuiGroup(GuiGroup<N, C, E, ?> copy) {
		this(copy.getChildren());
		dirtiness = copy.dirtiness;
		synchronized (cachedSpecDirtiness) {
			cachedSpec = copy.cachedSpec;
			cachedSpecDirtiness.set(copy.cachedSpecDirtiness.get());
		}
	}


	/* SECTION getters & setters */

	/**
	 * Gets the {@link #children} of this group that will be drawn.
	 *
	 * @return the {@code children} of this group
	 * @since 0.0.1.0
	 */
	public C getChildren() { return children; }

	/**
	 * Replaces the children of this group, configuring the new children for drawing if
	 * needed.
	 *
	 * @param children new children of this group
	 * @throws UnsupportedOperationException if immutable
	 * @see #setChildren(E...) the varargs version
	 * @since 0.0.1.0
	 */
	public void setChildren(C children) {
		this.children = children;
		markDirty();
	}

	/**
	 * See {@link #setChildren(Collection)}.
	 * <p>
	 * Parameter {@code children} is in varargs form for convenience.
	 *
	 * @implNote
	 * Type parameter {@link C} is considered compatible with
	 * {@code java.util.Arrays.ArrayList}.
	 * Will result in {@code ClassCastException} at a undefined time if not so.
	 *
	 * @param children new children of this group
	 * @throws UnsupportedOperationException if immutable
	 * @see #setChildren(Collection) the {@code Collection} version
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("varargs")
	@SafeVarargs
	public final void setChildren(E... children) { setChildren(Casts.<C>castUncheckedUnboxedNonnull(Arrays.asList(children))); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #getChildren}
	 */
	@Override
	@Deprecated
	public C getCollection() { return getChildren(); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #setChildren}
	 */
	@Override
	@Deprecated
	public void setCollection(C collection) { setChildren(collection); }


	/* SECTION methods */

	/**
	 * Returns the GUI size of this group.
	 * <p>
	 * Returns {@link Optional#empty} if every children has a spec of
	 * {@code Optional#empty}.
	 *
	 * @return the GUI size of this group, optional
	 */
	@SuppressWarnings("OptionalAssignedToNull")
	@Override
	public Optional<Rectangle.Immutable<N, ?>> spec() {
		synchronized (cachedSpecDirtiness) {
			if (isDirty(this, cachedSpecDirtiness) || cachedSpec == null) {
				Collection<E> e = getChildren();
				if (e.isEmpty()) return cachedSpec = Optional.empty();

				List<E> l = new ArrayList<>(e);
				@Nullable Rectangle<N, ?> f = unboxOptional(l.get(0).spec());
				List<Rectangle<N, ?>> r = l.subList(1, l.size()).stream().map(E::spec).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
				if (f == null) {
					if (r.isEmpty()) return cachedSpec = Optional.empty();
					else {
						f = r.get(0);
						r = r.subList(1, r.size());
					}
				}

				XY<N, ?> min = f.min().min(r.stream().map(Rectangle::min).collect(Collectors.toList()));
				return cachedSpec = Optional.of(new Rectangle.Immutable<>(min, f.max().max(r.stream().map(Rectangle::max).collect(Collectors.toList())).sum(of(min.negate()))));
			} else return cachedSpec;
		}
	}


	@Override
	public void draw(Minecraft client) { getChildren().forEach(t -> t.draw(client)); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingStatus(group = GROUP)
	public final T clone() { return ICloneable.clone(() -> super.clone()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }


	/* SECTION static classes */

	/**
	 * Immutable version of {@link GuiGroup}.
	 *
	 * @author William So
	 * @param <N> see {@link GuiGroup}
	 * @param <C> see {@link GuiGroup}
	 * @param <E> see {@link GuiGroup}
	 * @param <T> see {@link GuiGroup}
	 * @see GuiGroup
	 * @since 0.0.1.0
	 */
	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, C extends Collection<E>, E extends IDrawable<N, ?>, T extends GuiGroup<N, C, E, T>> extends GuiGroup<N, C, E, T> {
		/* SECTION constructors */

		/**
		 * See {@link #GuiGroup(Collection)}.
		 *
		 * @param children see {@link #GuiGroup(Collection)}
		 * @see #GuiGroup(Collection)
		 * @since 0.0.1.0
		 */
		public Immutable(C children) { super(tryToImmutableUnboxedNonnull(children)); }

		/**
		 * See {@link #GuiGroup(E...)}.
		 *
		 * @param children see {@link #GuiGroup(E...)}
		 * @see #GuiGroup(E...)
		 * @since 0.0.1.0
		 */
		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(E... children) { this(Casts.<C>castUncheckedUnboxedNonnull(ImmutableList.copyOf(children))); }

		/**
		 * See {@link #GuiGroup(GuiGroup)}.
		 *
		 * @param copy see {@link #GuiGroup(GuiGroup)}
		 * @see #GuiGroup(GuiGroup)
		 * @since 0.0.1.0
		 */
		public Immutable(GuiGroup<N, C, E, ?> copy) { this(copy.getChildren()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setChildren(C children) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
