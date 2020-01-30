package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Throwables;
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
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

@SideOnly(Side.CLIENT)
public class GuiGroup<N extends Number, E extends IDrawable<N, ?>, T extends GuiGroup<N, E, T>> extends Gui implements IDrawable<N, T> {
	/* SECTION variables */

	protected Collection<E> elements;


	/* SECTION constructors */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiGroup(E... elements) { this(Arrays.asList(elements)); }

	public GuiGroup(Collection<? extends E> elements) { this.elements = castUncheckedUnboxedNonnull(elements); }

	public GuiGroup(GuiGroup<N, ? extends E, ?> copy) { this(copy.getElements()); }


	/* SECTION getters & setters */

	public Collection<E> getElements() { return elements; }

	public void setElements(Collection<? extends E> elements) { this.elements = castUncheckedUnboxedNonnull(elements); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final void setElements(E... elements) { setElements(Arrays.asList(elements)); }


	/* SECTION methods */

	@Override
	public Optional<Rectangle<N, ?>> spec() {
		Collection<E> e = getElements();
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
	public void draw(Minecraft client) { getElements().forEach(t -> t.draw(client)); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getElements()) : getHashCode(this, super.hashCode());
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getElements().equals(t.getElements())) : isEqual(this, o, super.equals(o));
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
			throw Throwables.unexpected(e);
		}
		r.elements = tryCloneUnboxedNonnull(elements);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"elements", getElements()});
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, E extends IDrawable<N, ?>, T extends GuiGroup<N, E, T>> extends GuiGroup<N, E, T> {
		/* SECTION constructors */

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(E... elements) { this(ImmutableList.copyOf(elements)); }

		public Immutable(Collection<? extends E> elements) { super(tryToImmutableUnboxedNonnull(elements)); }

		public Immutable(GuiGroup<N, ? extends E, ?> copy) { this(copy.getElements()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setElements(Collection<? extends E> elements) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
