package $group__.client.gui.components;

import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.themes.ITheme;
import $group__.client.gui.traits.IDrawable;
import $group__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.logging.ILogging;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.extensions.delegated.IListDelegated;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.specific.Throwables.consumeCaught;
import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class GuiTab<T extends GuiTab<T, N, C, TH, E>, N extends Number, C, TH extends ITheme<TH>,
		E extends IDrawable<N>> extends GuiGroup<T, N, C, TH, List<E>, E> implements IListDelegated<List<E>, E> {
	public static final int INDEX_ACCESS = 0;
	public static final int INDEX_CONTENT = 1;


	protected boolean open = false;


	public GuiTab(E access, E content, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { super(Arrays.asList(access, content), mutator, logging); }

	public GuiTab(GuiTab<?, N, ?, ?, ? extends E> copy) { this(copy, copy.getMutator()); }


	protected GuiTab(GuiTab<?, N, ?, ?, ? extends E> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getAccess(), copy.getContent(), mutator, copy.getLogging()); }


	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiTab<V, N, C, TH, E>, N extends Number, C, TH extends ITheme<TH>, L extends Logger, E extends IDrawable<N>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGuiTab(E access, E content) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiTab<>(access, content, t.mutator, t.logging))); }


	public boolean isOpen() { return open; }

	public void setOpen(boolean open) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetOpen(open)); }

	@SuppressWarnings("SameReturnValue")
	public boolean trySetOpen(boolean open) {
		this.open = open;
		return true;
	}

	public Optional<Boolean> tryIsOpen() { return Optional.of(isOpen()); }

	public E getAccess() { return getChildren().get(INDEX_ACCESS); }

	public void setAccess(E access) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetAccess(access)); }

	public boolean trySetAccess(E access) {
		try {
			getChildren().set(INDEX_ACCESS, access);
			return true;
		} catch (UnsupportedOperationException e) {
			consumeCaught(e, getLogger());
			return false;
		}
	}

	public Optional<E> tryGetAccess() { return Optional.of(getAccess()); }

	public E getContent() { return getChildren().get(INDEX_CONTENT); }

	public void setContent(E content) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetContent(content)); }

	public boolean trySetContent(E content) {
		try {
			getChildren().set(INDEX_CONTENT, content);
			return true;
		} catch (UnsupportedOperationException e) {
			consumeCaught(e, getLogger());
			return false;
		}
	}

	public Optional<E> tryGetContent() { return Optional.of(getContent()); }

	@Override
	@Deprecated
	public List<E> getList() { return getChildren(); }

	@Override
	@Deprecated
	public void setList(List<E> list) { setChildren(list); }


	@Override
	public boolean tryDraw(Minecraft client) { return isOpen() ? super.tryDraw(client) : getAccess().tryDraw(client); }

	@Override
	public Optional<Rectangle<?, N>> spec() { return isOpen() ? super.spec() : getAccess().spec(); }


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiTab<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}
}
