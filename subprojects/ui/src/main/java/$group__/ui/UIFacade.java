package $group__.ui;

import $group__.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import $group__.ui.minecraft.core.mvvm.IUIViewModelMinecraft;
import $group__.ui.minecraft.core.mvvm.views.IUIViewMinecraft;
import $group__.ui.minecraft.mvvm.UIInfrastructureMinecraft;
import $group__.ui.minecraft.mvvm.adapters.AbstractContainerScreenAdapter;
import $group__.ui.minecraft.mvvm.adapters.AbstractScreenAdapter;
import $group__.ui.minecraft.mvvm.adapters.UIScreenAdapter;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.binding.core.IBinder;
import $group__.utilities.client.minecraft.ResourceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

		public static Document parseResourceDocument(INamespacePrefixedString location) throws IOException, SAXException {
			try (InputStream res = ResourceUtilities.getResource(NamespaceUtilities.toResourceLocation(location)).getInputStream()) {
				return Resources.parseDocumentInput(new InputSource(res));
			}
		}
	}

	public enum Resources {
		;

		private static final DocumentBuilder DOCUMENT_BUILDER;
		private static final Logger LOGGER = LogManager.getLogger();

		static {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DOCUMENT_BUILDER = ThrowableUtilities.Try.call(dbf::newDocumentBuilder, LOGGER).orElseThrow(ThrowableCatcher::rethrow);
		}

		public static Document parseDocumentInput(InputSource is) throws IOException, SAXException { return DOCUMENT_BUILDER.parse(is); }
	}
}
