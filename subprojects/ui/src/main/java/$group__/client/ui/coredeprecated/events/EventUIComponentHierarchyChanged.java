package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.mvvm.views.domlike.components.IUIComponentContainerDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentHierarchyChanged<T> extends EventUIComponentChanged<T> {
	protected EventUIComponentHierarchyChanged(EnumEventHookStage stage, IUIComponentDOMLike component, T previous, T next) { super(stage, component, previous, next); }

	@OnlyIn(Dist.CLIENT)
	public static class Parent extends EventUIComponentChanged<Optional<IUIComponentContainerDOMLike>> {
		public Parent(EnumEventHookStage stage, IUIComponentDOMLike component,
		              @Nullable IUIComponentContainerDOMLike previous, @Nullable IUIComponentContainerDOMLike current) {
			super(stage, component, Optional.ofNullable(previous), Optional.ofNullable(current));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Index extends EventUIComponentChanged<Integer> {
		public Index(EnumEventHookStage stage, IUIComponentDOMLike component,
		             int previous, int current) {
			super(stage, component, previous, current);
		}
	}
}
