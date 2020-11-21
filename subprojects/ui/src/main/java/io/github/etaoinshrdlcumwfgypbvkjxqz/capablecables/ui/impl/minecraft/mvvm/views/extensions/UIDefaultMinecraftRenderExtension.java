package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.extensions.IUIMinecraftRenderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.DoubleSupplier;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftRenderExtension
		implements IUIMinecraftRenderExtension {
	@SuppressWarnings("UnstableApiUsage")
	private static final TypeToken<IUIView<?>> STATIC_TYPE_TOKEN = TypeToken.of(CastUtilities.castUnchecked(IUIView.class));
	private final DoubleSupplier partialTicksSupplier;

	public UIDefaultMinecraftRenderExtension(DoubleSupplier partialTicksSupplier) {
		this.partialTicksSupplier = partialTicksSupplier;
	}

	@Override
	public double getPartialTicks() {
		return getPartialTicksSupplier().getAsDouble();
	}

	protected DoubleSupplier getPartialTicksSupplier() {
		return partialTicksSupplier;
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIView<?>> getType() {
		return StaticHolder.getType().getValue();
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends IUIView<?>> getTypeToken() {
		return getStaticTypeToken();
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static TypeToken<IUIView<?>> getStaticTypeToken() {
		return STATIC_TYPE_TOKEN;
	}
}
