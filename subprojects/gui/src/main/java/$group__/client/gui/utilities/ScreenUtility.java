package $group__.client.gui.utilities;

import $group__.utilities.helpers.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@SuppressWarnings("UnusedReturnValue")
@OnlyIn(CLIENT)
public final class ScreenUtility extends Screen {
	public static final ScreenUtility INSTANCE = new ScreenUtility();
	private static final Logger LOGGER = LogManager.getLogger();

	private ScreenUtility() {
		super(new StringTextComponent(""));
		Preconditions.requireRunOnceOnly(LOGGER);
	}

	public ScreenUtility setClient_(@Nullable Minecraft client) {
		minecraft = client;
		return this;
	}

	public ScreenUtility setWidth_(int width) {
		this.width = width;
		return this;
	}

	public ScreenUtility setHeight_(int height) {
		this.height = height;
		return this;
	}

	public ScreenUtility setBlitOffset_(int blitOffset) {
		setBlitOffset(blitOffset);
		return this;
	}

	public ScreenUtility setFont_(FontRenderer font) {
		this.font = font;
		return this;
	}

	public ScreenUtility setItemRenderer_(ItemRenderer itemRenderer) {
		this.itemRenderer = itemRenderer;
		return this;
	}

	@Override
	public void hLine(int x1, int x2, int y, int color) { super.hLine(x1, x2, y, color); }

	@Override
	public void vLine(int x, int y1, int y2, int color) { super.vLine(x, y1, y2, color); }

	@Override
	public void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { super.fillGradient(x1, y1, x2, y2, colorY1, colorY2); }

	@Override
	public void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { super.renderComponentHoverEffect(component, mouseX, mouseY); }

	@Override
	public void renderTooltip(ItemStack stack, int mouseX, int mouseY) { super.renderTooltip(stack, mouseX, mouseY); }
}
