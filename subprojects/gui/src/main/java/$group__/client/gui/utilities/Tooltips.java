package $group__.client.gui.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Tooltips {
	;

	/**
	 * @see Screen#getTooltipFromItem(ItemStack)
	 */
	public static List<String> getTooltipFromItem(Minecraft client, ItemStack stack) {
		return ScreenUtility.INSTANCE
				.setClient_(client)
				.getTooltipFromItem(stack);
	}

	/**
	 * @see Screen#renderTooltip(ItemStack, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void renderTooltip(Minecraft client, int width, int height, FontRenderer font, ItemRenderer itemRenderer, ItemStack item, int mouseX, int mouseY) {
		ScreenUtility.INSTANCE
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(item, mouseX, mouseY);
	}

	/**
	 * @see Screen#renderTooltip(String, int, int)
	 */
	public static void renderTooltip(int width, int height, FontRenderer font, ItemRenderer itemRenderer, String tooltip, int mouseX, int mouseY) {
		ScreenUtility.INSTANCE
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(tooltip, mouseX, mouseY);
	}

	/**
	 * @see Screen#renderTooltip(List, int, int)
	 */
	public static void renderTooltip(int width, int height, FontRenderer font, ItemRenderer itemRenderer, List<String> tooltip, int mouseX, int mouseY) {
		ScreenUtility.INSTANCE
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(tooltip, mouseX, mouseY);
	}

	/**
	 * @see Screen#renderTooltip(List, int, int, FontRenderer)
	 */
	public static void renderTooltip(int width, int height, ItemRenderer itemRenderer, List<String> tooltip, int mouseX, int mouseY, FontRenderer font) {
		ScreenUtility.INSTANCE
				.setWidth_(width)
				.setHeight_(height)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(tooltip, mouseX, mouseY, font);
	}
}
