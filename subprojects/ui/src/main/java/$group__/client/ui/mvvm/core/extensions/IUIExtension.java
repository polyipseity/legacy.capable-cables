package $group__.client.ui.mvvm.core.extensions;

import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.specific.ThrowableUtilities;
import $group__.utilities.structures.Registry;
import $group__.utilities.structures.Singleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtension<C extends IExtensionContainer<?, ?>>
		extends IExtension<C> {
	IType<?, ?> getType();

	@OnlyIn(Dist.CLIENT)
	interface IType<T extends IUIExtension<C>, C extends IExtensionContainer<?, ?>> {
		Optional<T> get(C component);

		ResourceLocation getKey();
	}

	@OnlyIn(Dist.CLIENT)
	final class RegUIExtension extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final RegUIExtension INSTANCE = Singleton.getSingletonInstance(RegUIExtension.class, LOGGER);

		@SuppressWarnings("unused")
		protected RegUIExtension() { super(true, LOGGER); }

		public static void checkExtensionRegistered(IUIExtension<?> extension) {
			if (!INSTANCE.containsValue(extension.getType())) {
				throw ThrowableUtilities.BecauseOf.illegalArgument("Unregistered UI extension type",
						"extension.getType()", extension.getType(),
						"extension", extension);
			}
		}
	}
}
