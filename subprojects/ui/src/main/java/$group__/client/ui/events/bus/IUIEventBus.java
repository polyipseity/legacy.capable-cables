package $group__.client.ui.events.bus;

public interface IUIEventBus<E, T> {
	boolean register(T target);

	boolean unregister(T target);

	boolean post(E event);

	Class<E> getEventClass();

	Class<T> getTargetClass();
}
