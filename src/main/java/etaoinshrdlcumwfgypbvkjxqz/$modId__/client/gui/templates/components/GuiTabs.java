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
public class GuiTabs<N extends Number, T extends GuiTabs<N, T>> extends Gui implements IDrawable<N, T> {
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
	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }


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
	public T clone() {
		T r;
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
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiTabs<N, T> {
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
		public T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}


	public interface ITab<N extends Number, T extends ITab<N, T>> extends IDrawable<N, T> {
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
		T clone();


		/* SECTION static classes */

		class Impl<N extends Number, T extends Impl<N, T>> implements ITab<N, T> {
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

			public void setAccess(IDrawable<N, T> access) { this.access = access; }

			public IDrawable<N, ?> getContent() { return content; }

			public void setContent(IDrawable<N, T> content) { this.content = content; }


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
			@Override
			public T toImmutable() { return castUnchecked(new Immutable<>(this)); }


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
			public T clone() {
				T r;
				try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
				r.access = access.clone();
				r.content = content.clone();
				return r;
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Impl<N, T> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) { super(access.toImmutable(), content.toImmutable(), open); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

				public Immutable(Impl<N, ?> c) { this(c.getAccess(), c.getContent(), c.isOpen()); }


				/* SECTION getters & setters */

				/** {@inheritDoc} */
				@Override
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				/** {@inheritDoc} */
				@Override
				public T toImmutable() { return castUnchecked(this); }

				/** {@inheritDoc} */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}


	public interface ITabThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends ITabThemed<N, TH, T>> extends ITab<N, T>, IDrawableThemed<N, TH, T> {
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
		T clone();


		/* SECTION static classes */

		class Impl<N extends Number, TH extends ITheme<TH>, T extends Impl<N, TH, T>> extends ITab.Impl<N, T> implements ITabThemed<N, TH, T> {
			/* SECTION variables */

			protected TH theme;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) {
				super(access, content, open);
				setTheme(this, theme);
			}

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access, content, theme, false); }

			public Impl(ITabThemed.Impl<N, TH, ?> c) { this(c.getAccess(), c.getContent(), c.getTheme(), c.isOpen()); }


			/* SECTION getters & setters */

			/** {@inheritDoc} */
			@Override
			public TH getTheme() { return theme; }

			/** {@inheritDoc} */
			@Override
			public void setTheme(TH theme) { setTheme(this, theme); }


			/* SECTION methods */

			/** {@inheritDoc} */
			@Override
			public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


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
			public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends ITabThemed.Impl<N, TH, T> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) { super(access.toImmutable(), content.toImmutable(), theme, open); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access.toImmutable(), content.toImmutable(), theme, false); }

				public Immutable(ITabThemed.Impl<N, TH, ?> c) { this(c.getAccess(), c.getContent(), c.getTheme()); }


				/* SECTION getters & setters */

				/** {@inheritDoc} */
				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				/** {@inheritDoc} */
				@Override
				public T toImmutable() { return castUnchecked(this); }

				/** {@inheritDoc} */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}
}
