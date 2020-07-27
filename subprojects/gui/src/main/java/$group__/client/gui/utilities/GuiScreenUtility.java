package $group__.client.gui.utilities;

import $group__.utilities.Preconditions;
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
public final class GuiScreenUtility extends Screen {
	public static final GuiScreenUtility INSTANCE = new GuiScreenUtility();
	private static final Logger LOGGER = LogManager.getLogger();

	private GuiScreenUtility() {
		super(new StringTextComponent(""));
		Preconditions.requireRunOnceOnly(LOGGER);
	}

	public GuiScreenUtility setClient_(@Nullable Minecraft client) {
		minecraft = client;
		return this;
	}

	public GuiScreenUtility setWidth_(int width) {
		this.width = width;
		return this;
	}

	public GuiScreenUtility setHeight_(int height) {
		this.height = height;
		return this;
	}

	public GuiScreenUtility setBlitOffset_(int blitOffset) {
		setBlitOffset(blitOffset);
		return this;
	}

	public GuiScreenUtility setFont_(FontRenderer font) {
		this.font = font;
		return this;
	}

	public GuiScreenUtility setItemRenderer_(ItemRenderer itemRenderer) {
		this.itemRenderer = itemRenderer;
		return this;
	}

	@Override
	public void fillGradient(int x1, int y1, int x2, int y2, int colorY1, int colorY2) { super.fillGradient(x1, y1, x2, y2, colorY1, colorY2); }

	@Override
	public void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { super.renderComponentHoverEffect(component, mouseX, mouseY); }

	@Override
	public void renderTooltip(ItemStack stack, int mouseX, int mouseY) { super.renderTooltip(stack, mouseX, mouseY); }
}
