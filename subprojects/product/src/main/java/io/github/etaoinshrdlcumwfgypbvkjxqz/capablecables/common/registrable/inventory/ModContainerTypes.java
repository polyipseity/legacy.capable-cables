package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public enum ModContainerTypes {
	;

	private static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModConstants.getModID());

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<ContainerType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug()
				? getRegister().register(UIMinecraftDebug.getPath(), UIMinecraftDebug::getContainerEntry)
				: null;
	}

	public static DeferredRegister<ContainerType<?>> getRegister() {
		return REGISTER;
	}

	@SuppressWarnings("unused")
	@Nullable
	private static RegistryObject<ContainerType<?>> getDebugUI() {
		return DEBUG_UI;
	}
}
