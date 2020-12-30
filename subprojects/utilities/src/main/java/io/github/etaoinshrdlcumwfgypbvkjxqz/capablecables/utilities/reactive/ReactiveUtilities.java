package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive;

import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public enum ReactiveUtilities {
	;

	public static final long UNBOUNDED_REQUESTS = Long.MAX_VALUE;

	public static <T> DisposableSubscriber<T> decorateAsListener(Function<? super Subscriber<? super T>, ? extends Subscriber<? super T>> constructor,
	                                                             Logger logger) {
		return ContinuouslyRequestingSubscriber.of(
				constructor.apply(
						ErrorLoggingSubscriber.of(logger)
				)
		);
	}

	public static void addRequest(AtomicLong requested, long n) {
		assert n >= 0L;
		requested.accumulateAndGet(n, (requested1, n1) -> {
			if (requested1 == getUnboundedRequests() || n1 == getUnboundedRequests())
				return getUnboundedRequests();
			long result = requested1 + n1;
			if (result < requested1)
				return getUnboundedRequests();
			return result;
		});
	}

	public static long getUnboundedRequests() {
		return UNBOUNDED_REQUESTS;
	}

	public static boolean trySatisfyRequest(AtomicLong requested) {
		return requested.updateAndGet(requested1 -> requested1 == getUnboundedRequests()
				? getUnboundedRequests()
				: requested1 - 1L)
				>= 0L;
	}
}
