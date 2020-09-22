package $group__.ui.structures.shapes.interactions;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.ui.structures.shapes.descriptors.DelegatingShapeDescriptor;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.templates.CommonConfigurationTemplate;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class ProviderShapeDescriptor<S extends Shape>
		extends DelegatingShapeDescriptor<S> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final OptionalWeakReference<IShapeDescriptorProvider> owner;

	public ProviderShapeDescriptor(IShapeDescriptorProvider owner, IShapeDescriptor<S> delegated) {
		super(delegated);
		this.owner = new OptionalWeakReference<>(owner);
	}

	@Override
	public boolean modify(Supplier<? extends Boolean> action)
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
