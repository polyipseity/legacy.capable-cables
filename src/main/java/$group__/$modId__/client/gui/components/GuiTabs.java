package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.IListDelegated;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Casts;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IDirty.isDirty;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectIndexOutOfBounds;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number, L extends List<E>, E extends GuiTabs.ITab<N, ?>, T extends GuiTabs<N, L, E, T>> extends GuiGroup<N, L, E, T> implements IListDelegated<L, E, T> {
	/* SECTION variables */

	protected int open;


	/* SECTION constructors */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabs(int open, E... tabs) { this(castUncheckedUnboxedNonnull(Arrays.asList(tabs)), open); }

	public GuiTabs(L tabs, int open) {
		super(tabs);
		setOpen(this, open);
	}

	public GuiTabs(GuiTabs<N, L, E, ?> copy) { this(copy.getTabs(), copy.getOpen()); }


	/* SECTION static methods */

	protected static void setOpen(GuiTabs<?, ?, ?, ?> t, int open) {
		Collection<? extends ITab<?, ?>> tabs = t.getTabs();
		int bound = tabs.size();
		if (bound <= open) throw rejectIndexOutOfBounds(bound, open);
		t.open = open;

		int o1 = t.getOpen();
		final int[] i = {0};
		tabs.forEach(e -> e.setOpen(i[0]++ == o1));
	}


	/* SECTION getters & setters */

	public int getOpen() { return open; }

	public void setOpen(int open) {
		setOpen(this, open);
		markDirty();
	}

	public L getTabs() { return children; }

	public void setTabs(L tabs, int open) {
		this.children = tabs;
		setOpen(this, open);
		markDirty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final void setTabs(int open, E... tabs) { setTabs(castUncheckedUnboxedNonnull(Arrays.asList(tabs)), open); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #getTabs}
	 */
	@Override
	@Deprecated
	public L getChildren() { return getTabs(); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #setTabs}
	 */
	@Override
	@Deprecated
	public void setChildren(L children) { setTabs(children, 0); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #getTabs}
	 */
	@Override
	@Deprecated
	public L getList() { return getChildren(); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #setTabs}
	 */
	@Override
	@Deprecated
	public void setList(L list) { setChildren(list); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


	/* SECTION static classes */

	public interface ITab<N extends Number, T extends ITab<N, T>> extends IDrawable<N, T> {
		/* SECTION methods */

		boolean isOpen();

		void setOpen(boolean open);


		/* SECTION static classes */

		class Impl<N extends Number, T extends Impl<N, T>> implements ITab<N, T> {
			/* SECTION variables */

			protected IDrawable<N, ?> access;
			protected IDrawable<N, ?> content;
			protected boolean open;
			protected long dirtiness;

			@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
			@Nullable
			protected Optional<Rectangle.Immutable<N, ?>> cachedSpec;
			protected final AtomicLong cachedSpecDirtiness = new AtomicLong();


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) {
				this.access = access;
				this.content = content;
				this.open = open;
			}

			public Impl(Impl<N, ?> copy) {
				this(copy.getAccess(), copy.getContent(), copy.isOpen());
				dirtiness = copy.dirtiness;
				synchronized (cachedSpecDirtiness) {
					cachedSpec = copy.cachedSpec;
					cachedSpecDirtiness.set(copy.cachedSpecDirtiness.get());
				}
			}


			/* SECTION getters & setters */

			public IDrawable<N, ?> getAccess() { return access; }

			public void setAccess(IDrawable<N, T> access) {
				this.access = access;
				markDirty();
			}

			public IDrawable<N, ?> getContent() { return content; }

			public void setContent(IDrawable<N, T> content) {
				this.content = content;
				markDirty();
			}


			@Override
			public boolean isOpen() { return open; }

			@Override
			public void setOpen(boolean open) {
				this.open = open;
				markDirty();
			}


			/* SECTION methods */

			@SuppressWarnings("OptionalAssignedToNull")
			@Override
			public Optional<Rectangle.Immutable<N, ?>> spec() {
				synchronized (cachedSpecDirtiness) {
					if (isDirty(this, cachedSpecDirtiness) || cachedSpec == null) {
						Optional<? extends Rectangle<N, ?>> aSpec = getAccess().spec(), cSpec = getContent().spec();
						if (isOpen() && cSpec.isPresent()) {
							Rectangle<N, ?> cSpecU = cSpec.get();
							return cachedSpec = Optional.of(aSpec.map(t -> t.min().min(of(cSpecU.min()))).map(min -> new Rectangle.Immutable<>(min, aSpec.get().max().max(of(cSpecU.max())).sum(of(min.negate())))).orElseGet(() -> new Rectangle.Immutable<>(cSpecU)));
						} else
							return cachedSpec = aSpec.map(Rectangle.Immutable::new);
					} else return cachedSpec;
				}
			}

			@Override
			public void draw(Minecraft client) {
				getAccess().draw(client);
				if (isOpen()) {
					getContent().draw(client);
					merge();
				}
			}

			@SuppressWarnings("EmptyMethod")
			protected void merge() { /* MARK empty */ }

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
			@OverridingStatus(group = GROUP, when = When.MAYBE)
			@OverridingMethodsMustInvokeSuper
			public T clone() { return ICloneable.clone(() -> super.clone()); }

			@Override
			@OverridingStatus(group = GROUP)
			public final String toString() { return getToStringString(this, super.toString()); }


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Impl<N, T> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) { super(access.toImmutable(), content.toImmutable(), tryToImmutableUnboxedNonnull(open)); }

				public Immutable(Impl<N, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.isOpen()); }


				/* SECTION getters & setters */

				@Override
				@Deprecated
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				@Override
				@Deprecated
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				@Override
				@Deprecated
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				@Override
				@OverridingStatus(group = GROUP)
				public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

				@Override
				@OverridingStatus(group = GROUP)
				public final boolean isImmutable() { return true; }
			}
		}
	}


	public interface ITabThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends ITabThemed<N, TH, T>> extends ITab<N, T>, IDrawableThemed<N, TH, T> {
		/* SECTION static classes */

		class Impl<N extends Number, TH extends ITheme<TH>, T extends Impl<N, TH, T>> extends ITab.Impl<N, T> implements ITabThemed<N, TH, T> {
			/* SECTION variables */

			@SuppressWarnings("NotNullFieldNotInitialized")
			protected TH theme;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access, content, theme, false); }

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) {
				super(access, content, open);
				setTheme(this, theme);
			}

			public Impl(ITabThemed.Impl<N, TH, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.getTheme(), copy.isOpen()); }


			/* SECTION static methods */

			protected static <T extends ITheme<T>> void setTheme(ITabThemed.Impl<?, T, ?> t, T theme) {
				t.theme = theme;
				Casts.<IThemed<T>>castChecked(t.getAccess(), castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t1 -> t1.setTheme(theme));
				Casts.<IThemed<T>>castChecked(t.getContent(), castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t1 -> t1.setTheme(theme));
			}


			/* SECTION getters & setters */

			@Override
			public TH getTheme() { return theme; }

			@Override
			public void setTheme(TH theme) {
				setTheme(this, theme);
				markDirty();
			}


			/* SECTION methods */

			@Override
			public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

			@Override
			public boolean isImmutable() { return false; }


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends ITabThemed.Impl<N, TH, T> {
				/* SECTION constructors */

				public Immutable(ITabThemed.Impl<N, TH, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.getTheme()); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access.toImmutable(), content.toImmutable(), theme, false); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) { super(access.toImmutable(), content.toImmutable(), tryToImmutableUnboxedNonnull(theme), tryToImmutableUnboxedNonnull(open)); }


				/* SECTION getters & setters */

				@Override
				@Deprecated
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				@Override
				@Deprecated
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				@Override
				@Deprecated
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }

				@Override
				@Deprecated
				public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				@Override
				@OverridingStatus(group = GROUP)
				public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

				@Override
				@OverridingStatus(group = GROUP)
				public final boolean isImmutable() { return true; }
			}
		}
	}

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, L extends List<E>, E extends ITab<N, ?>, T extends Immutable<N, L, E, T>> extends GuiTabs<N, L, E, T> {
		/* SECTION constructors */

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(int open, E... tabs) { this(castUncheckedUnboxedNonnull(ImmutableList.copyOf(tabs)), open); }

		public Immutable(L tabs, int open) { super(tryToImmutableUnboxedNonnull(tabs), tryToImmutableUnboxedNonnull(open)); }

		public Immutable(GuiTabs<N, L, E, ?> copy) { this(copy.getTabs(), copy.getOpen()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTabs(L tabs, int open) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
