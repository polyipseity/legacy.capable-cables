package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed.tryCastThemedTo;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.*;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number, D extends GuiTabs<N, D>> extends Gui implements IDrawable<N, D> {
	/* SECTION variables */

	protected List<ITab<N, ?>> tabs;
	protected int open;


	/* SECTION constructors */

	public GuiTabs(List<ITab<N, ?>> tabs, int open) {
		this.tabs = tabs;
		setOpen(this, open);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabs(int open, ITab<N, ?>... tabs) { this(Arrays.asList(tabs), open); }

	public GuiTabs(GuiTabs<N, ?> c) { this(c.getTabs(), c.getOpen()); }


	/* SECTION getters & setters */

	public List<ITab<N, ?>> getTabs() { return tabs; }

	public void setTabs(List<ITab<N, ?>> tabs, int open) {
		this.tabs = tabs;
		setOpen(open);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final void setTabs(int open, ITab<N, ?>... tabs) { setTabs(Arrays.asList(tabs), open); }

	public int getOpen() { return open; }

	public void setOpen(int open) { setOpen(this, open); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Rectangle<N, ?> specification() {
		List<ITab<N, ?>> t = getTabs();
		Rectangle<N, ?> f = t.get(0).specification();
		Rectangle<N, ?>[] r = castUnchecked(t.subList(1, t.size()).stream().map(IDrawable::specification).toArray(Rectangle<?, ?>[]::new));
		XY<N, ?> min = f.min().min(castUnchecked(Arrays.stream(r).map(Rectangle::min).toArray(XY<?, ?>[]::new)));
		return new Rectangle<>(min, f.max().max(castUnchecked(Arrays.stream(r).map(Rectangle::max).toArray(XY<?, ?>[]::new))).minus(min));
	}


	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) { getTabs().forEach(t -> t.draw(game)); }


	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return castUnchecked(new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"tabs", getTabs()},
				new Object[]{"open", getOpen()});
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getTabs(), getOpen()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getOpen() == t.getOpen(),
			t -> getTabs().equals(t.getTabs())); }

	/** {@inheritDoc} */
	@Override
	public D clone() {
		D r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
		List<ITab<N, ?>> t = getTabs();
		ITab<N, ?>[] tabsC = castUnchecked(t.stream().map(ICloneable::copy).toArray(ITab<?, ?>[]::new));
		r.tabs = t instanceof ImmutableList ? ImmutableList.copyOf(tabsC) : Arrays.asList(tabsC);
		return r;
	}


	/* SECTION static methods */

	protected static void setOpen(GuiTabs<?, ?> t, int open) {
		int bound = t.getTabs().size();
		if (bound <= open) throw rejectIndexOutOfBounds(bound, open);
		t.open = open;
		open = t.getOpen();

		int i = 0;
		for (ITab<?, ?> tab : t.getTabs()) { tab.setOpen(i++ == open); }
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiTabs<N, D> {
		/* SECTION constructors */

		public Immutable(List<ITab<N, ?>> tabs, int open) { super(ImmutableList.copyOf(tabs), open); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(int open, ITab<N, ?>... tabs) { this(Arrays.asList(tabs), open); }

		public Immutable(GuiTabs<N, ?> c) { this(c.getTabs(), c.getOpen()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setTabs(List<ITab<N, ?>> iTabs, int open) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public D toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}


	public interface ITab<N extends Number, D extends ITab<N, D>> extends IDrawable<N, D> {
		/* SECTION methods */

		boolean isOpen();

		void setOpen(boolean open);


		/** {@inheritDoc} */
		@Override
		String toString();

		/** {@inheritDoc} */
		@Override
		int hashCode();

		/** {@inheritDoc} */
		@Override
		boolean equals(Object o);

		/** {@inheritDoc} */
		@Override
		D clone();


		/* SECTION static classes */

		class Impl<N extends Number, D extends Impl<N, D>> implements ITab<N, D> {
			/* SECTION variables */

			protected IDrawable<N, ?> access;
			protected IDrawable<N, ?> content;
			protected boolean open;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) {
				this.access = access;
				this.content = content;
				this.open = open;
			}

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

			public Impl(Impl<N, ?> c) { this(c.getAccess(), c.getContent(), c.isOpen()); }


			/* SECTION getters & setters */

			public IDrawable<N, ?> getAccess() { return access; }

			public void setAccess(IDrawable<N, D> access) { this.access = access; }

			public IDrawable<N, ?> getContent() { return content; }

			public void setContent(IDrawable<N, D> content) { this.content = content; }


			/** {@inheritDoc} */
			@Override
			public boolean isOpen() { return open; }

			/** {@inheritDoc} */
			@Override
			public void setOpen(boolean open) { this.open = open; }


			/* SECTION methods */

			@SuppressWarnings("EmptyMethod")
			protected void merge() {}


			/** {@inheritDoc} */
			@Override
			public Rectangle<N, ?> specification() {
				Rectangle<N, ?> aSpec = getAccess().specification();
				Rectangle<N, ?> cSpec = getContent().specification();
				if (isOpen()) {
					XY<N, ?> min = aSpec.min().min(cSpec.min());
					return new Rectangle<>(min, aSpec.max().max(cSpec.max()).minus(min));
				} else {
					XY<N, ?> min = aSpec.min();
					return new Rectangle<>(min, aSpec.max().minus(min));
				}
			}


			/** {@inheritDoc} */
			@Override
			public void draw(Minecraft game) {
				getAccess().draw(game);
				if (isOpen()) {
					getContent().draw(game);
					merge();
				}
			}


			/** {@inheritDoc} */
			@SuppressWarnings("unchecked")
			@Override
			public D toImmutable() { return castUnchecked(new Immutable<>(this)); }


			/** {@inheritDoc} */
			@Override
			public String toString() { return getToStringString(this, super.toString(),
						new Object[]{"access", getAccess()},
						new Object[]{"content", getContent()},
						new Object[]{"open", isOpen()}); }

			/** {@inheritDoc} */
			@Override
			public int hashCode() { return getHashCode(this, super.hashCode(), getContent(), getAccess(), isOpen()); }

			/** {@inheritDoc} */
			@Override
			public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
					t -> getAccess().equals(t.getAccess()),
					t -> getContent().equals(t.getContent()),
					t -> isOpen() == t.isOpen()); }

			/** {@inheritDoc} */
			@Override
			public D clone() {
				D r;
				try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
				r.access = access.clone();
				r.content = content.clone();
				return r;
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, D extends Immutable<N, D>> extends Impl<N, D> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) { super(access.toImmutable(), content.toImmutable(), open); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

				public Immutable(Impl<N, ?> c) { this(c.getAccess(), c.getContent(), c.isOpen()); }


				/* SECTION getters & setters */

				/** {@inheritDoc} */
				@Override
				public void setContent(IDrawable<N, D> content) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setAccess(IDrawable<N, D> access) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				/** {@inheritDoc} */
				@Override
				public D toImmutable() { return castUnchecked(this); }

				/** {@inheritDoc} */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}


	public interface ITabThemed<N extends Number, T extends IThemed.ITheme<T>, D extends ITabThemed<N, T, D>> extends ITab<N, D>, IDrawableThemed<N, T, D> {
		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		String toString();

		/** {@inheritDoc} */
		@Override
		int hashCode();

		/** {@inheritDoc} */
		@Override
		boolean equals(Object o);

		/** {@inheritDoc} */
		@Override
		D clone();


		/* SECTION static classes */

		class Impl<N extends Number, T extends ITheme<T>, D extends Impl<N, T, D>> extends ITab.Impl<N, D> implements ITabThemed<N, T, D> {
			/* SECTION variables */

			protected T theme;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, T theme, boolean open) {
				super(access, content, open);
				setTheme(this, theme);
			}

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, T theme) { this(access, content, theme, false); }

			public Impl(ITabThemed.Impl<N, T, ?> c) { this(c.getAccess(), c.getContent(), c.getTheme(), c.isOpen()); }


			/* SECTION getters & setters */

			/** {@inheritDoc} */
			@Override
			public T getTheme() { return theme; }

			/** {@inheritDoc} */
			@Override
			public void setTheme(T theme) { setTheme(this, theme); }


			/* SECTION methods */

			/** {@inheritDoc} */
			@SuppressWarnings("unchecked")
			@Override
			public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


			/** {@inheritDoc} */
			@Override
			public String toString() { return IStrictToString.getToStringString(this, super.toString(),
						new Object[]{"theme", getTheme()}); }

			/** {@inheritDoc} */
			@Override
			public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

			/** {@inheritDoc} */
			@Override
			public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
					t -> getTheme().equals(t.getTheme())); }


			/* SECTION static methods */

			protected static <T extends ITheme<T>> void setTheme(ITabThemed.Impl<?, T, ?> t, T theme) {
				t.theme = theme;
				theme = t.getTheme();

				IThemed<T> td;
				if ((td = tryCastThemedTo(t.getAccess())) != null) td.setTheme(theme);
				if ((td = tryCastThemedTo(t.getContent())) != null) td.setTheme(theme);
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, T extends ITheme<T>, D extends Immutable<N, T, D>> extends ITabThemed.Impl<N, T, D> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, T theme, boolean open) { super(access.toImmutable(), content.toImmutable(), theme, open); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, T theme) { this(access.toImmutable(), content.toImmutable(), theme, false); }

				public Immutable(ITabThemed.Impl<N, T, ?> c) { this(c.getAccess(), c.getContent(), c.getTheme()); }


				/* SECTION getters & setters */

				/** {@inheritDoc} */
				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setContent(IDrawable<N, D> content) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setAccess(IDrawable<N, D> access) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setTheme(T theme) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				/** {@inheritDoc} */
				@Override
				public D toImmutable() { return castUnchecked(this); }

				/** {@inheritDoc} */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}
}
