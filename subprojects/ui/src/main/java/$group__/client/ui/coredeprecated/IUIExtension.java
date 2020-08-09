package $group__.client.ui.coredeprecated;

import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.interfaces.IExtension;
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
public interface IUIExtension extends IExtension<IUIComponent> {
	IType<?, ?> getType();

	@OnlyIn(Dist.CLIENT)
	interface IType<T extends IUIExtension, C extends IUIComponent> {
		Optional<T> get(C component);

		ResourceLocation getKey();
	}

	@OnlyIn(Dist.CLIENT)
	final class Reg extends Registry<ResourceLocation, IType<?, ?>> {
		private static final Logger LOGGER = LogManager.getLogger();
		public static final Reg INSTANCE = Singleton.getSingletonInstance(Reg.class, LOGGER);

		@SuppressWarnings("unused")
		protected Reg() { super(false, LOGGER); }

		public static void checkExtensionRegistered(IUIExtension extension) {
			if (!INSTANCE.containsValue(extension.getType())) {
				throw ThrowableUtilities.BecauseOf.illegalArgument("Unregistered extension type",
						"extension.getType()", extension.getType(),
						"extension", extension);
			}
		}
	}
}
