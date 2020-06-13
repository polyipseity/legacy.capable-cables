package $group__.client.configurations;

import $group__.Globals;
import $group__.utilities.Singleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Provides a customized client configuration screen.
 *
 * @author William So
 * @see IModGuiFactory
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public class ModGuiFactoryThis extends Singleton implements IModGuiFactory {
	/**
	 * Unused default constructor.
	 *
	 * @implNote {@link Mod#guiFactory} requires constructor to be {@code public}
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link Singleton#getSingletonInstance}, used by {@link Mod#guiFactory} only
	 */
	@Deprecated
	public ModGuiFactoryThis() { super(Globals.LOGGER); }


	@Override
	public void initialize(Minecraft client) { /* MARK empty */ }

	@Override
	public boolean hasConfigGui() { return true; }

	@Override
	@Nullable
	public GuiScreen createConfigGui(GuiScreen parentScreen) { return null; }

	/**
	 * @since 0.0.1
	 * @deprecated unused
	 */
	@Override
	@Nullable
	@Deprecated
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}
