package $group__.client.gui;

import $group__.$modId__.client.gui.components.*;
import $group__.Globals;
import $group__.client.gui.components.*;
import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.themes.ITheme;
import $group__.client.gui.themes.IThemed;
import $group__.client.gui.themes.IThemedUser;
import $group__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.logging.ILogging;
import $group__.logging.ILoggingUser;
import $group__.traits.ISpec;
import $group__.utilities.builders.BuilderDefaults;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.concurrent.IMutatorUser;
import $group__.utilities.extensions.IStructure;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.Optional;

import static $group__.client.gui.bases.GuiContainerBases.initGuiBase;
import static $group__.client.gui.coordinates.NumberRelativeDisplay.X.newBuilderX;
import static $group__.client.gui.coordinates.NumberRelativeDisplay.Y.newBuilderY;
import static $group__.client.gui.polygons.Rectangle.newBuilderRectangle;
import static $group__.utilities.builders.BuilderStructure.KEY_DEFAULT_LOGGING;
import static $group__.utilities.builders.BuilderStructure.KEY_DEFAULT_MUTATOR;
import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiWrench<T extends GuiWrench<T, TH>, TH extends ITheme<TH>> extends GuiContainer implements IStructure<T, T>, ISpec<Rectangle<?, ?>>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>, Logger>, IThemedUser<IThemed<TH>, TH> {
	protected GuiTabs<?, ?, ?, ?, ?, ?> tabs;
	protected IThemed<TH> themed;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;

	@SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "MagicNumber"})
	public GuiWrench(Container container, int open, @Nullable TH theme, IMutatorImmutablizable<?, ?> mutator,
	                 @Nullable Logger logger) {
		super(container);
		this.mutator = trySetNonnull(mutator, mutator, true);
		logging = trySetNonnull(getMutator(), ILogging.of(logger, getMutator()), true);

		themed = trySetNonnull(getMutator(), IThemed$.MODULE$.of(theme, getMutator(), getLogging()), true);
		{
			Runnable popDefaults = BuilderDefaults
					.pushDefaultStart(KEY_DEFAULT_MUTATOR, getMutator())
					.pushDefault(KEY_DEFAULT_LOGGING, getLogging())
					.pushDefault(BuilderGuiDrawable.KEY_DEFAULT_COLORED, null)
					.pushDefault(BuilderGuiDrawable.KEY_DEFAULT_THEMED, getThemed()).stopPushing();
			tabs = GuiTabs.newBuilderGuiTabs(
					Arrays.asList(
							GuiTab.newBuilderGuiTab(
									castUncheckedUnboxedNonnull(GuiRectangle.newBuilderGuiRectangle(newBuilderRectangle(newBuilderX(0.8F).build(), newBuilderY(0.8F).build(), newBuilderX(0.1F).build(), newBuilderY(0.1F).build()).build()).build()),
									castUncheckedUnboxedNonnull(GuiRectangleMatcher.newBuilderGuiRectangleMatcher(
											newBuilderRectangle(32, 32, newBuilderX(0.1F).build(),
													newBuilderY(0.1F).setOffset(-32).build()).build(),
											GuiResource.newBuilderGuiResource(
													newBuilderRectangle(64, 64, newBuilderX(0.1F).build(),
															newBuilderY(0.1F).setOffset(-32).build()).build(),
													Globals.Client.Resources.GUI_WRENCH,
													Globals.Client.Resources.GUI_WRENCH_INFO
											).build()
									).build())
							).build()
					),
					open
			).build();
			popDefaults.run();
		}
	}

	@Override
	public IThemed<TH> getThemed() { return themed; }

	@Override
	public boolean trySetThemed(IThemed<TH> themed) { return trySet(t -> this.themed = t, themed); }

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }

	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) { return trySet(t -> this.mutator = t, mutator); }

	@Override
	public void initGui() { initGuiBase(this, super::initGui, t -> xSize = t, t -> ySize = t); }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.tryDraw(mc); }


	@Override
	public Optional<Rectangle<?, ?>> spec() { return Optional.ofNullable(unboxOptional(tabs.spec())); }


	@Override
	public T toImmutable() { throw rejectUnsupportedOperation(); }

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = PACKAGE, when = When.NEVER)
	public final String toString() { return getToStringString(this, super.toString()); }
}
