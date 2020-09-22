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

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModConstants.MOD_ID);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<ContainerType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? CONTAINERS.register(UIDebugMinecraft.getPATH(), UIDebugMinecraft::getContainerEntry) : null;
	}
}
