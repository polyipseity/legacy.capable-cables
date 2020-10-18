package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIViewModelMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.UIInfrastructureMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters.AbstractScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters.UIScreenAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftResourceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public enum UIFacade {
	;

	@OnlyIn(Dist.CLIENT)
	public enum Minecraft {
		;

		public static <C extends Container> AbstractContainerScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>, C> createScreen(ITextComponent title, IUIInfrastructureMinecraft<?, ?, ?> infrastructure, C container) {
			return new UIScreenAdapter.Builder.WithChildren<>(title, infrastructure, container).build();
		}

		public static AbstractScreenAdapter<? extends IUIInfrastructureMinecraft<?, ?, ?>> createScreen(ITextComponent title, IUIInfrastructureMinecraft<?, ?, ?> infrastructure) {
			return new UIScreenAdapter.Builder<>(title, infrastructure).build();
		}

		public static IUIInfrastructureMinecraft<?, ?, ?> createInfrastructure(IUIViewMinecraft<?> view, IUIViewModelMinecraft<?> viewModel, IBinder binder) {
			return new UIInfrastructureMinecraft<>(view, viewModel, binder);
		}

		public static Document parseResource(INamespacePrefixedString location) throws IOException, SAXException {
			try (InputStream res = MinecraftResourceUtilities.getInputStream(location)) {
				return Resources.parseDocumentInput(new InputSource(res));
			}
		}
	}

	public enum Resources {
		;

		private static final DocumentBuilder DOCUMENT_BUILDER;

		static {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			try {
				DOCUMENT_BUILDER = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw ThrowableUtilities.propagate(e);
			}
		}

		public static Document parseDocumentInput(InputSource is) throws IOException, SAXException { return getDocumentBuilder().parse(is); }

		private static DocumentBuilder getDocumentBuilder() { return DOCUMENT_BUILDER; }
	}
}
