package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum ContainersThis {
	;

	private static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModConstants.getModId());

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<ContainerType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug()
				? getRegister().register(UIDebugMinecraft.getPath(), UIDebugMinecraft::getContainerEntry)
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
