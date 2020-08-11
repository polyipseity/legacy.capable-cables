package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionScreenProvider<C extends IUIInfrastructure<?, ?, ?>>
		extends IUIExtension<C> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "screen");

	Screen getScreen();

	Set<Integer> getCloseKeys();

	Set<Integer> getChangeFocusKeys();

	boolean isPaused();

	void setPaused(boolean paused);
}
