package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging.LoggingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.ext.XLogger;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public enum ThrowableUtilities {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static boolean isChecked(Throwable throwable) { return !isUnchecked(throwable); }

	public static boolean isUnchecked(Throwable throwable) { return throwable instanceof RuntimeException || throwable instanceof Error; }

	public static RuntimeException propagate(Throwable throwable)
			throws RuntimeException, Error { throw propagate(throwable, ThrowableUtilities::wrap); }

	@SuppressWarnings({"ThrowCaughtLocally", "RedundantThrows"})
	public static <T extends Throwable, R extends Throwable> RuntimeException propagate(T throwable,
	                                                                                    Function<? super T, R> checkedWrapper)
			throws R, RuntimeException, Error {
		try {
			throw propagate(throwable, checkedWrapper, Function.identity());
		} catch (Throwable t) {
			// COMMENT RC is declared in throws, RU should be unchecked
			throw propagateUnverified(t);
		}
	}

	public static RuntimeException wrap(Throwable throwable) {
		return new RuntimeException(new LogMessageBuilder()
				.addMarkers(UtilitiesMarkers.getInstance()::getMarkerThrowable)
				.addMessages(() -> getResourceBundle().getString("throwable.wrap.message"))
				.build(),
				throwable);
	}

	public static <T extends Throwable, RC extends Throwable, RU extends Throwable> RuntimeException propagate(T throwable,
	                                                                                                           Function<? super T, RC> checkedWrapper,
	                                                                                                           Function<? super T, RU> uncheckedWrapper)
			throws RC, RU, RuntimeException, Error {
		throwIfUnrecoverable(throwable);
		if (isUnchecked(throwable))
			throw uncheckedWrapper.apply(throwable);
		else
			throw checkedWrapper.apply(throwable);
	}

	public static RuntimeException propagateUnverified(Throwable throwable) {
		DynamicUtilities.UNSAFE.throwException(throwable);
		throw new AssertionError();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static void throwIfUnrecoverable(Throwable throwable)
			throws RuntimeException, Error {
		if (throwable instanceof VirtualMachineError)
			throw (VirtualMachineError) throwable;
		if (throwable instanceof AssertionError)
			throw (AssertionError) throwable;
		if (throwable instanceof LinkageError)
			throw (LinkageError) throwable;
		if (throwable instanceof ThreadDeath)
			throw (ThreadDeath) throwable;
	}

	public static Throwable create() {
		return new RuntimeException(new LogMessageBuilder()
				.addMarkers(UtilitiesMarkers.getInstance()::getMarkerThrowable)
				.addMessages(() -> getResourceBundle().getString("throwable.create.message"))
				.build());
	}

	public static void logCatch(Throwable t, Logger logger) { LoggingUtilities.getXLogger(logger).catching(XLogger.Level.DEBUG, t); }

	public static <TH extends Throwable> void runQuietly(IThrowingRunnable<? extends TH> lambda, Class<TH> throwableClass, IThrowableHandler<? super Throwable> handler) {
		getQuietly(() -> {
			lambda.run();
			return null;
		}, throwableClass, handler);
	}

	public static <V, TH extends Throwable> Optional<V> getQuietly(IThrowingSupplier<V, ? extends TH> lambda, Class<TH> throwableClass, IThrowableHandler<? super Throwable> handler) {
		try {
			return Optional.ofNullable(lambda.get());
		} catch (Throwable throwable) {
			throwIfUnrecoverable(throwable);
			Optional<TH> throwableCasted = CastUtilities.castChecked(throwableClass, throwable);
			throwableCasted.ifPresent(handler);
			if (!throwableCasted.isPresent())
				throw propagateUnverified(throwable);
			return Optional.empty();
		}
	}
}
