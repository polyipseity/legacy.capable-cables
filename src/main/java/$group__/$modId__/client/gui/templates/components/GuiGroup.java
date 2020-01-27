package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;
import $group__.$modId__.utilities.helpers.Throwables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.*;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
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

	public GuiGroup(Collection<? extends E> elements) { this.elements = castUncheckedUnboxedNonnull(elements); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiGroup(E... elements) { this(Arrays.asList(elements)); }

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
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"elements", getElements()});
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getElements()); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getElements().equals(t.getElements())); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) { throw Throwables.unexpected(e); }
		r.elements = tryCloneUnboxedNonnull(elements);
		return r;
	}


	/* SECTION static classes */

	public static class Immutable<N extends Number, E extends IDrawable<N, ?>, T extends GuiGroup<N, E, T>> extends GuiGroup<N, E, T> {
		/* SECTION constructors */

		public Immutable(Collection<? extends E> elements) { super(IImmutablizable.tryToImmutableUnboxedNonnull(elements)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(E... elements) { this(Arrays.asList(elements)); }

		public Immutable(GuiGroup<N, ? extends E, ?> copy) { this(copy.getElements()); }


		/* SECTION methods */

		@Override
		public void setElements(Collection<? extends E> elements) { throw rejectUnsupportedOperation(); }


		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
