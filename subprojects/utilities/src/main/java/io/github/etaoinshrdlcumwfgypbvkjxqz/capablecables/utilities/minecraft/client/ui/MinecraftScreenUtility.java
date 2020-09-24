package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MinecraftScreenUtility extends Screen {
	private static final Supplier<MinecraftScreenUtility> INSTANCE = Suppliers.memoize(MinecraftScreenUtility::new);

	private MinecraftScreenUtility() {
		super(MinecraftTextComponentUtilities.getEmpty());
	}

	public static MinecraftScreenUtility getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public MinecraftScreenUtility setClient_(@Nullable Minecraft client) {
		minecraft = client;
		return this;
	}

	public MinecraftScreenUtility setWidth_(int width) {
		this.width = width;
		return this;
	}

	public MinecraftScreenUtility setHeight_(int height) {
		this.height = height;
		return this;
	}

	public MinecraftScreenUtility setBlitOffset_(int blitOffset) {
		setBlitOffset(blitOffset);
		return this;
	}

	public MinecraftScreenUtility setFont_(FontRenderer font) {
		this.font = font;
		return this;
	}

	public MinecraftScreenUtility setItemRenderer_(ItemRenderer itemRenderer) {
		this.itemRenderer = itemRenderer;
		return this;
	}

	@Override
	public void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { super.fillGradient(x1, y1, x2, y2, colorY1, colorY2); }

	@Override
	public void renderTooltip(ItemStack stack, int mouseX, int mouseY) { super.renderTooltip(stack, mouseX, mouseY); }

	@Override
	public void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { super.renderComponentHoverEffect(component, mouseX, mouseY); }
}
