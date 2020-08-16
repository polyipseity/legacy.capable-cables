package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.structures.Registry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionScreenProvider
		extends IUIExtension<ResourceLocation, IUIInfrastructure<?, ?, ?>> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".screen");
	Registry.RegistryObject<IType<ResourceLocation, IUIExtensionScreenProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	Screen getScreen();

	Set<Integer> getCloseKeys();

	Set<Integer> getChangeFocusKeys();

	boolean isPaused();

	void setPaused(boolean paused);
}
