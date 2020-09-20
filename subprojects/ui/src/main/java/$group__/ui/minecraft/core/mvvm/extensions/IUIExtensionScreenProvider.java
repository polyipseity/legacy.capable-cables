package $group__.ui.minecraft.core.mvvm.extensions;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionScreenProvider
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), AREA_UI + ".screen");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionScreenProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			AbstractExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionScreenProvider>) i.getExtension(t.getKey())));

	Screen getScreen();

	Set<Integer> getCloseKeys();

	Set<Integer> getChangeFocusKeys();

	void setPaused(boolean paused);

	void setTitle(ITextComponent title);
}
