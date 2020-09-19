package $group__.common.registrable.inventory;

import $group__.ui.UIConstants;
import $group__.ui.debug.UIDebugMinecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.ModConstants.MOD_ID;

public enum ContainersThis {
	;

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<ContainerType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? CONTAINERS.register(UIDebugMinecraft.PATH, UIDebugMinecraft::getContainerEntry) : null;
	}
}
