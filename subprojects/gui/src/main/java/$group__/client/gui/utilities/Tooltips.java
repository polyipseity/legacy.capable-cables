package $group__.client.gui.utilities;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Tooltips {
	;

	public static final int TOOLTIP_WIDTH_MAX = 200;

	public static List<String> getTooltipFromItem(Minecraft client, ItemStack stack) {
		List<ITextComponent> tt = stack.getTooltip(client.player, client.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
		List<String> r = Lists.newArrayList();
		for (ITextComponent text : tt) r.add(text.getFormattedText());
		return r;
	}

	public static void renderTooltip(Minecraft client, ItemStack stack, int mouseX, int mouseY, int width, int height, FontRenderer fontDefault) {
		@Nullable FontRenderer font = stack.getItem().getFontRenderer(stack);
		GuiUtils.preItemToolTip(stack);
		renderTooltip(getTooltipFromItem(client, stack), mouseX, mouseY, width, height, (font == null ? fontDefault : font));
		GuiUtils.postItemToolTip();
	}

	public static void renderTooltip(String tooltip, int mouseX, int mouseY, int width, int height, FontRenderer font) {
		renderTooltip(Collections.singletonList(tooltip), mouseX, mouseY, width, height, font);
	}

	public static void renderTooltip(List<String> tooltip, int mouseX, int mouseY, int width, int height, FontRenderer font) { GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, width, height, -1, font); }
}