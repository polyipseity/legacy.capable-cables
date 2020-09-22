package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum TextComponentUtilities {
	;

	private static final TextComponent EMPTY = new StringTextComponent("");

	public static TextComponent getEmpty() { return EMPTY; }

	/**
	 * @see Screen#renderComponentHoverEffect(ITextComponent, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void renderComponentHoverEffect(Minecraft client, int width, int height, FontRenderer font, ITextComponent component, int mouseX, int mouseY) {
		UIScreenUtility.getInstance()
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
