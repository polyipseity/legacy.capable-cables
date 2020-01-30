package $group__.$modId__.client.configurations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ModGuiFactoryThis implements IModGuiFactory {
	/* SECTION static variables */

	public static final String
			SUBPACKAGE = "client.configurations",
			CLASS_SIMPLE_NAME = "ModGuiFactoryThis";


	/* SECTION methods */

	@Override
	public void initialize(Minecraft game) {}

	@Override
	public boolean hasConfigGui() { return true; }

	@Nullable
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) { return null; }

	@Nullable
	@Deprecated
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
}
