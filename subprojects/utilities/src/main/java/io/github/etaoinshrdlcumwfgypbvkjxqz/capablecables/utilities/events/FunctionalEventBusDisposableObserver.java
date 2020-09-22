package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class FunctionalEventBusDisposableObserver<T>
		extends DefaultDisposableObserver<T>
		implements ISubscribeEventSupplier {
	@Nullable
	private final SubscribeEvent subscribeEvent;
	private final Consumer<? super T> action;

	public FunctionalEventBusDisposableObserver(Consumer<? super T> action) { this(null, action); }

	public FunctionalEventBusDisposableObserver(@Nullable SubscribeEvent subscribeEvent, Consumer<? super T> action) {
		this.subscribeEvent = subscribeEvent;
		this.action = action;
	}

	@Override
	public void onNext(@Nonnull T t) { getAction().accept(t); }

	protected Consumer<? super T> getAction() { return action; }

	@Override
	public Optional<? extends SubscribeEvent> getSubscribeEvent() { return Optional.ofNullable(subscribeEvent); }
}
