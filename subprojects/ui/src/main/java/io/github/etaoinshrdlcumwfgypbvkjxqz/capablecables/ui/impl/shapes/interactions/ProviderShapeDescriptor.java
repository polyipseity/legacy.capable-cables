package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.AbstractDelegatingShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

public class ProviderShapeDescriptor<S extends Shape>
		extends AbstractDelegatingShapeDescriptor<S, IShapeDescriptor<S>> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final OptionalWeakReference<IShapeDescriptorProvider> owner;

	public ProviderShapeDescriptor(IShapeDescriptorProvider owner, IShapeDescriptor<S> delegated) {
		super(delegated);
		this.owner = new OptionalWeakReference<>(owner);
	}

	@Override
	public S getShapeOutput() {
		return getDelegate().getShapeOutput();
	}

	@Override
	public boolean modify(BooleanSupplier action)
			throws ConcurrentModificationException {
		if (!getOwner().map(IShapeDescriptorProvider::isModifyingShape).orElse(true))
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("this", this).addKeyValue("action", action)
							.addMessages(() -> getResourceBundle().getString("modify.owner.modifying.check.fail"))
							.build()
			);
		return super.modify(action);
	}

	protected Optional<? extends IShapeDescriptorProvider> getOwner() { return owner.getOptional(); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
