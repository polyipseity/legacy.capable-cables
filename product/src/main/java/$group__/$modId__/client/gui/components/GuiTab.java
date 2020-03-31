package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.utilities.extensions.delegated.IListDelegated;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.consumeCaught;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class GuiTab<T extends GuiTab<T, N, C, TH, E>, N extends Number, C, TH extends ITheme<TH>, E extends IDrawable<N>> extends GuiGroup<T, N, C, TH, List<E>, E> implements IListDelegated<List<E>, E> {
	/* SECTION static variables */

	public static final int INDEX_ACCESS = 0;
	public static final int INDEX_CONTENT = 1;


	/* SECTION variables */

	protected boolean open = false;


	/* SECTION constructors */

	public GuiTab(E access, E content, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { super(Arrays.asList(access, content), mutator, logging); }

	public GuiTab(GuiTab<?, N, ?, ?, ? extends E> copy) { this(copy, copy.getMutator()); }


	protected GuiTab(GuiTab<?, N, ?, ?, ? extends E> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getAccess(), copy.getContent(), mutator, copy.getLogging()); }


	/* SECTION static methods */

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiTab<V, N, C, TH, E>, N extends Number, C, TH extends ITheme<TH>, L extends Logger, E extends IDrawable<N>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGT(E access, E content) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiTab<>(access, content, t.mutator, t.logging))); }


	/* SECTION getters & setters */

	public boolean isOpen() { return open; }

	public boolean trySetOpen(boolean open) {
		this.open = open;
		return true;
	}

	public Optional<Boolean> tryIsOpen() { return Optional.of(isOpen()); }

	public void setOpen(boolean open) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetOpen(open)); }

	public E getAccess() { return getChildren().get(INDEX_ACCESS); }

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

	public void setAccess(E access) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetAccess(access)); }

	public E getContent() { return getChildren().get(INDEX_CONTENT); }

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

	public void setContent(E content) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetContent(content)); }


	@Override
	@Deprecated
	public List<E> getList() { return getChildren(); }

	@Override
	@Deprecated
	public void setList(List<E> list) { setChildren(list); }


	/* SECTION methods */

	@Override
	public boolean tryDraw(Minecraft client) { return isOpen() ? super.tryDraw(client) : getAccess().tryDraw(client); }

	@Override
	public Optional<Rectangle<?, N>> spec() { return isOpen() ? super.spec() : getAccess().spec(); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiTab<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
