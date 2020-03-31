package $group__.$modId__.client.configurations;

import $group__.$modId__.utilities.Singleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

import static $group__.$modId__.Globals.LOGGER;

/**
 * Provides a customized client configuration screen.
 *
 * @author William So
 * @see IModGuiFactory
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public class ModGuiFactoryThis extends Singleton implements IModGuiFactory {
	/* SECTION static variables */

	/**
	 * {@code package} of this class without the group.
	 *
	 * @since 0.0.1.0
	 */
	public static final String SUBPACKAGE = "client.configurations";
	/**
	 * {@link Class#getSimpleName} of this class.
	 *
	 * @since 0.0.1.0
	 */
	public static final String CLASS_SIMPLE_NAME = "ModGuiFactoryThis";


	/* SECTION constructor */

	/**
	 * Unused default constructor.
	 *
	 * @implNote {@link Mod#guiFactory} requires constructor to be {@code public}
	 *
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link Singleton#getSingletonInstance}, used by {@link Mod#guiFactory} only
	 */
	@Deprecated
	public ModGuiFactoryThis() { super(LOGGER); }


	/* SECTION methods */

	@Override
	public void initialize(Minecraft client) { /* MARK empty */ }

	@Override
	public boolean hasConfigGui() { return true; }

	@Override
	@Nullable
	public GuiScreen createConfigGui(GuiScreen parentScreen) { return null; }

	/**
	 * @since 0.0.1.0
	 * @deprecated unused
	 */
	@Override
	@Nullable
	@Deprecated
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}
