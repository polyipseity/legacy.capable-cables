package $group__.client.gui.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum TextComponents {
	;

	/**
	 * @see Screen#renderComponentHoverEffect(ITextComponent, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void renderComponentHoverEffect(Minecraft client, int width, int height, FontRenderer font, ITextComponent component, int mouseX, int mouseY) {
		ScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.renderComponentHoverEffect(component, mouseX, mouseY);
	}

	/**
	 * @see Screen#handleComponentClicked(ITextComponent)
	 */
	public static boolean handleComponentClicked(Screen screen, ITextComponent component) { return screen.handleComponentClicked(component); }
}
