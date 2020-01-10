package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.EnumTheme;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ICloneable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.tryCastTo;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.*;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number, D extends GuiTabs<N, D>> extends Gui implements IDrawable<N, D> {
	protected List<ITab<N, ?>> tabs;
	protected int open;

	public GuiTabs(int open, List<ITab<N, ?>> tabs) {
		if (tabs.isEmpty()) throw rejectArguments(tabs);
		this.open = open;
		this.tabs = tabs;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabs(int open, ITab<N, ?>... tabs) { this(open, Arrays.asList(tabs)); }

	public void setTabs(List<ITab<N, ?>> tabs) { throw rejectUnsupportedOperation(); }

	public List<ITab<N, ?>> getTabs() { return tabs; }

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void setOpen(int open) {
		tabs.get(open); // Check index.
		this.open = open;
	}

	public int getOpen() { return open; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Minecraft game) {
		int i = 0;
		for (ITab<N, ?> t : tabs) {
			if (i++ == open) t.draw(game);
			else t.drawClosed(game);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
	@Override
	public Rectangle<N, ?> specification() {
		Rectangle<N, ?> f = tabs.get(0).specification();
		@SuppressWarnings("SuspiciousToArrayCall") Rectangle<N, ?>[] r = (Rectangle<N, ?>[]) tabs.subList(1, tabs.size()).stream().map(IDrawable::specification).toArray(Rectangle<?, ?>[]::new);
		@SuppressWarnings("SuspiciousToArrayCall") XY<N, ?> min = f.min().min((XY<N, ?>[]) Arrays.stream(r).map(Rectangle::min).toArray(XY<?, ?>[]::new));
		return new Rectangle<>(min, f.max().max((XY<N, ?>[]) Arrays.stream(r).map(Rectangle::max).toArray(XY<?, ?>[]::new)).minus(min));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D clone() {
		D r;
		try { r = (D) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) {
			throw unexpectedThrowable(ex);
		}
		@SuppressWarnings("SuspiciousToArrayCall") ITab<N, ?>[] tabsC = (ITab<N, ?>[]) tabs.stream().map(ICloneable::copy).toArray(ITab<?, ?>[]::new);
		r.tabs = tabs instanceof ImmutableList ? ImmutableList.copyOf(tabsC) : Arrays.asList(tabsC);
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiTabs)) return false;
		GuiTabs<?, ?> guiTabs = (GuiTabs<?, ?>) o;
		return open == guiTabs.open &&
				tabs.equals(guiTabs.tabs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(tabs, open); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiTabs<N, D> {
		public Immutable(int open, List<ITab<N, ?>> tabs) { super(open, ImmutableList.copyOf(tabs)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(int open, ITab<N, ?>... tabs) { super(open, ImmutableList.copyOf(tabs)); }

		public Immutable(GuiTabs<N, ?> c) { this(c.open, c.tabs); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }


		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public D toImmutable() { return (D) this; }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isImmutable() { return true; }
	}

	public interface ITab<N extends Number, D extends ITab<N, D>> extends IDrawable<N, D> {
		void drawClosed(Minecraft game);

		class Impl<N extends Number, D extends Impl<N, D>> implements ITab<N, D> {
			protected IDrawable<N, ?> content;
			protected IDrawable<N, ?> access;

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content) {
				this.content = content;
				this.access = access;
			}

			@SuppressWarnings("EmptyMethod")
			protected void merge() {}

			public void setContent(IDrawable<N, D> content) { this.content = content; }

			public IDrawable<N, ?> getContent() { return content; }

			public void setAccess(IDrawable<N, D> access) { this.access = access; }

			public IDrawable<N, ?> getAccess() { return access; }

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void draw(Minecraft game) {
				access.draw(game);
				content.draw(game);
				merge();
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void drawClosed(Minecraft game) { access.draw(game); }

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Rectangle<N, ?> specification() {
				XY<N, ?> min = access.specification().min().min(content.specification().min());
				return new Rectangle<>(min, access.specification().max().max(content.specification().max()).minus(min));
			}

			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings("unchecked")
			@Override
			public D clone() {
				D r;
				try { r = (D) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) {
					throw unexpectedThrowable(ex);
				}
				r.access = access.clone();
				r.content = content.clone();
				return r;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (!(o instanceof Impl)) return false;
				Impl<?, ?> impl = (Impl<?, ?>) o;
				return content.equals(impl.content) &&
						access.equals(impl.access);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int hashCode() { return Objects.hash(content, access); }

			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings("unchecked")
			@Override
			public D toImmutable() { return (D) new Immutable<>(this); }

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, D extends Immutable<N, D>> extends Impl<N, D> {
				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content) { super(access.toImmutable(), content.toImmutable()); }

				public Immutable(Impl<N, ?> c) { this(c.access, c.content); }

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void setContent(IDrawable<N, D> content) { throw rejectUnsupportedOperation(); }

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void setAccess(IDrawable<N, D> access) { throw rejectUnsupportedOperation(); }


				/**
				 * {@inheritDoc}
				 */
				@SuppressWarnings("unchecked")
				@Override
				public D toImmutable() { return (D) this; }

				/**
				 * {@inheritDoc}
				 */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}

	public interface ITabThemed<N extends Number, D extends ITabThemed<N, D>> extends ITab<N, D>, IDrawableThemed<N, D, EnumTheme> {
		class Impl<N extends Number, D extends Impl<N, D>> implements ITabThemed<N, D> {
			protected IDrawable<N, ?> content;
			protected IDrawable<N, ?> access;
			protected EnumTheme theme;

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, EnumTheme theme) {
				this.content = content;
				this.access = access;
				this.theme = theme;
			}

			@SuppressWarnings("EmptyMethod")
			protected void merge() {}

			public void setContent(IDrawable<N, D> content) { this.content = content; }

			public IDrawable<N, ?> getContent() { return content; }

			public void setAccess(IDrawable<N, D> access) { this.access = access; }

			public IDrawable<N, ?> getAccess() { return access; }

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void draw(Minecraft game) {
				access.draw(game);
				content.draw(game);
				merge();
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void drawClosed(Minecraft game) { access.draw(game); }

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Rectangle<N, ?> specification() {
				XY<N, ?> min = access.specification().min().min(content.specification().min());
				return new Rectangle<>(min, access.specification().max().max(content.specification().max()).minus(min));
			}

			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings("unchecked")
			@Override
			public D clone() {
				D r;
				try { r = (D) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) {
					throw unexpectedThrowable(ex);
				}
				r.access = access.clone();
				r.content = content.clone();
				return r;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (!(o instanceof ITab.Impl)) return false;
				ITab.Impl<?, ?> impl = (ITab.Impl<?, ?>) o;
				return content.equals(impl.content) &&
						access.equals(impl.access);
			}


			/** {@inheritDoc} */
			@Override
			public void setTheme(EnumTheme theme) {
				IThemed<EnumTheme> t;
				if ((t = tryCastTo(EnumTheme.class, access)) != null) t.setTheme(theme);
				if ((t = tryCastTo(EnumTheme.class, content)) != null) t.setTheme(theme);
				this.theme = theme;
			}

			/** {@inheritDoc} */
			@Override
			public EnumTheme getTheme() { return theme; }


			/**
			 * {@inheritDoc}
			 */
			@Override
			public int hashCode() { return Objects.hash(content, access); }

			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings("unchecked")
			@Override
			public D toImmutable() { return (D) new Immutable<>(this); }

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, D extends Immutable<N, D>> extends ITabThemed.Impl<N, D> {
				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, EnumTheme theme) { super(access.toImmutable(), content.toImmutable(), theme); }

				public Immutable(ITabThemed.Impl<N, ?> c) { this(c.access, c.content, c.theme); }


				/**
				 * {@inheritDoc}
				 */
				@Override
				public void setContent(IDrawable<N, D> content) { throw rejectUnsupportedOperation(); }

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void setAccess(IDrawable<N, D> access) { throw rejectUnsupportedOperation(); }

				/** {@inheritDoc} */
				@Override
				public void setTheme(EnumTheme theme) { throw rejectUnsupportedOperation(); }


				/**
				 * {@inheritDoc}
				 */
				@SuppressWarnings("unchecked")
				@Override
				public D toImmutable() { return (D) this; }

				/**
				 * {@inheritDoc}
				 */
				@Override
				public boolean isImmutable() { return true; }
			}
		}
	}
}
