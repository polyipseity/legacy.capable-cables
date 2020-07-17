package $group__.client.gui.utilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public enum TextComponents {
	;

	public static final Logger LOGGER = LogManager.getLogger();
	public static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");

	public static void openURI(URI uri) { Util.getOSType().openURI(uri); }

	/**
	 * @see Screen#renderComponentHoverEffect(ITextComponent, int, int)
	 */
	@SuppressWarnings("JavadocReference")
	public static void renderComponentHoverEffect(Minecraft client, @Nullable ITextComponent component, int mouseX, int mouseY, int width, int height, FontRenderer font) {
		if (component != null && component.getStyle().getHoverEvent() != null) {
			HoverEvent event = component.getStyle().getHoverEvent();
			if (event.getAction() == HoverEvent.Action.SHOW_ITEM) {
				ItemStack stack = ItemStack.EMPTY;

				try {
					stack = ItemStack.read(JsonToNBT.getTagFromJson(event.getValue().getString()));
				} catch (CommandSyntaxException ignored) {}

				if (stack.isEmpty())
					Tooltips.renderTooltip(TextFormatting.RED + "Invalid Item!", mouseX, mouseY, width, height, font);
				else Tooltips.renderTooltip(client, stack, mouseX, mouseY, width, height, font);
			} else if (event.getAction() == HoverEvent.Action.SHOW_ENTITY) {
				if (client.gameSettings.advancedItemTooltips) {
					try {
						CompoundNBT nbt = JsonToNBT.getTagFromJson(event.getValue().getString());
						List<String> list = Lists.newArrayList();
						@Nullable ITextComponent componentAdvanced = ITextComponent.Serializer.fromJson(nbt.getString("name"));
						if (componentAdvanced != null) list.add(componentAdvanced.getFormattedText());

						if (nbt.contains("type", 8)) {
							String s = nbt.getString("type");
							list.add("Type: " + s);
						}

						list.add(nbt.getString("id"));
						Tooltips.renderTooltip(list, mouseX, mouseY, width, height, font);
					} catch (CommandSyntaxException | JsonSyntaxException ex) {
						Tooltips.renderTooltip(TextFormatting.RED + "Invalid Entity!", mouseX, mouseY, width, height, font);
					}
				}
			} else if (event.getAction() == HoverEvent.Action.SHOW_TEXT) {
				Tooltips.renderTooltip(client.fontRenderer.listFormattedStringToWidth(event.getValue().getFormattedText(), Math.max(width / 2, Tooltips.TOOLTIP_WIDTH_MAX)), mouseX, mouseY, width, height, font);
			}
		}
	}

	/**
	 * @see Screen#handleComponentClicked(ITextComponent)
	 */
	public static boolean handleComponentClicked(Screen screen, BiConsumer<String, Boolean> insertTextMethod, BiConsumer<String, Boolean> sendMessageMethod, @Nullable ITextComponent component) {
		if (component != null) {
			@Nullable ClickEvent event = component.getStyle().getClickEvent();
			if (Screen.hasShiftDown()) {
				if (component.getStyle().getInsertion() != null)
					insertTextMethod.accept(component.getStyle().getInsertion(), false);
			} else if (event != null) {
				switch (event.getAction()) {
					case OPEN_URL:
						if (!screen.getMinecraft().gameSettings.chatLinks) return false;
						try {
							URI uri = new URI(event.getValue());
							String s = uri.getScheme();
							if (s == null) throw new URISyntaxException(event.getValue(), "Missing protocol");

							if (!ALLOWED_PROTOCOLS.contains(s.toLowerCase(Locale.ROOT)))
								throw new URISyntaxException(event.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));

							if (screen.getMinecraft().gameSettings.chatLinksPrompt) {
								screen.getMinecraft().displayGuiScreen(new ConfirmOpenLinkScreen(t -> {
									if (t) openURI(uri);
									screen.getMinecraft().displayGuiScreen(screen);
								}, event.getValue(), false));
							} else openURI(uri);
						} catch (URISyntaxException ex) {
							LOGGER.error("Can't open url for {}", event, ex);
						}
						break;
					case OPEN_FILE:
						openURI(new File(event.getValue()).toURI());
						break;
					case SUGGEST_COMMAND:
						insertTextMethod.accept(event.getValue(), true);
						break;
					case RUN_COMMAND:
						sendMessageMethod.accept(event.getValue(), false);
						break;
					default:
						LOGGER.error("Don't know how to handle {}", event);
						break;
				}
				return true;
			}
		}
		return false;
	}
}
