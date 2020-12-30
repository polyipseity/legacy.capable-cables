package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.views.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.extensions.IUIMinecraftRenderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
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
	public IExtensionType<IIdentifier, ?, IUIView<?>> getType() {
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
