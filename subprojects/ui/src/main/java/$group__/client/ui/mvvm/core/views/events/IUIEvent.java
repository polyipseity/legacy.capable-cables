package $group__.client.ui.mvvm.core.views.events;

import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;

public interface IUIEvent {
	String
			TYPE_SELECT_STRING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "select";
	ResourceLocation
			TYPE_SELECT = new ResourceLocation(TYPE_SELECT_STRING);

	ResourceLocation getType();

	IUIEventTarget getTarget();

	EnumPhase getPhase();

	void advancePhase();

	void reset();

	boolean canBubble();

	boolean isPropagationStopped();

	void stopPropagation();

	enum EnumPhase {
		NONE,
		CAPTURING_PHASE,
		AT_TARGET,
		BUBBLING_PHASE,
	}
}
