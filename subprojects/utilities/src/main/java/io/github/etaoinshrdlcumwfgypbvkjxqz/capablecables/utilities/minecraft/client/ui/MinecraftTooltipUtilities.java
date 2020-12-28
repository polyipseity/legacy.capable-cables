package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public enum MinecraftTooltipUtilities {
	;

	/**
	 * @see Screen#getTooltipFromItem(ItemStack)
	 */
	public static List<String> getTooltipFromItem(Minecraft client, ItemStack stack) {
		return MinecraftScreenUtility.getInstance()
				.setClient_(client)
				.getTooltipFromItem(stack);
	}

	/**
	 * @see Screen#renderTooltip(ItemStack, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void renderTooltip(Minecraft client, int width, int height, FontRenderer font, ItemRenderer itemRenderer, ItemStack item, int mouseX, int mouseY) {
		MinecraftScreenUtility.getInstance()
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
	public static void renderTooltip(int width, int height, FontRenderer font, ItemRenderer itemRenderer, CharSequence tooltip, int mouseX, int mouseY) {
		MinecraftScreenUtility.getInstance()
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(tooltip.toString(), mouseX, mouseY);
	}

	/**
	 * @see Screen#renderTooltip(List, int, int)
	 */
	@SuppressWarnings("UnstableApiUsage")
	public static void renderTooltip(int width, int height, FontRenderer font, ItemRenderer itemRenderer, List<? extends CharSequence> tooltip, int mouseX, int mouseY) {
		MinecraftScreenUtility.getInstance()
				.setWidth_(width)
				.setHeight_(height)
				.setFont_(font)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(
						tooltip.stream()
								.map(CharSequence::toString)
								.collect(ImmutableList.toImmutableList()),
						mouseX, mouseY
				);
	}

	/**
	 * @see Screen#renderTooltip(List, int, int, FontRenderer)
	 */
	@SuppressWarnings("UnstableApiUsage")
	public static void renderTooltip(int width, int height, ItemRenderer itemRenderer, List<? extends CharSequence> tooltip, int mouseX, int mouseY, FontRenderer font) {
		MinecraftScreenUtility.getInstance()
				.setWidth_(width)
				.setHeight_(height)
				.setItemRenderer_(itemRenderer)
				.renderTooltip(
						tooltip.stream()
								.map(CharSequence::toString)
								.collect(ImmutableList.toImmutableList()),
						mouseX, mouseY,
						font
				);
	}
}
