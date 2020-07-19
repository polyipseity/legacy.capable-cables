package $group__.common.registrables.inventory;

import $group__.client.gui.debug.GuiComponentDebug;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static $group__.Constants.MOD_ID;

public enum ContainersThis {
	;

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MOD_ID);

	// todo add debug flag
	@SuppressWarnings("unused")
	private static final RegistryObject<ContainerType<?>> DEBUG_GUI_COMPONENT = CONTAINERS.register(GuiComponentDebug.PATH, GuiComponentDebug::getContainerEntry);
}
