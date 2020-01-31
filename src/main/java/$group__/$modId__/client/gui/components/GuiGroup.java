package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.ICollectionDelegated;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.*;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
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
	public GuiGroup(GuiGroup<N, C, E, ?> copy) { this(copy.getChildren()); }


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
	public void setChildren(C children) { this.children = children; }

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
	@Override
	public Optional<Rectangle<N, ?>> spec() {
		Collection<E> e = getChildren();
		if (e.isEmpty()) return Optional.empty();

		List<E> l = new ArrayList<>(e);
		@Nullable Rectangle<N, ?> f = unboxOptional(l.get(0).spec());
		List<Rectangle<N, ?>> r = l.subList(1, l.size()).stream().map(E::spec).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		if (f == null) {
			if (r.isEmpty()) return Optional.empty();
			else {
				f = r.get(0);
				r = r.subList(1, r.size());
			}
		}

		XY<N, ?> min = f.min().min(r.stream().map(Rectangle::min).collect(Collectors.toList()));
		return Optional.of(new Rectangle<>(min, f.max().max(r.stream().map(Rectangle::max).collect(Collectors.toList())).sum(of(min.negate()))));
	}


	@Override
	public void draw(Minecraft client) { getChildren().forEach(t -> t.draw(client)); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getChildren()) : getHashCode(this, super.hashCode());
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getChildren().equals(t.getChildren())) : isEqual(this, o, super.equals(o));
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
			throw unexpected(e);
		}
		r.children = tryCloneUnboxedNonnull(children);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"children", getChildren()});
	}


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
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
