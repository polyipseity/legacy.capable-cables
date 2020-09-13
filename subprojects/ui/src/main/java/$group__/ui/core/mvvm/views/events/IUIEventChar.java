package $group__.ui.core.mvvm.views.events;

public interface IUIEventChar extends IUIEvent {
	char getCodePoint();

	int getModifiers();
}
