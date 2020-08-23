package $group__.client.ui.core.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.util.ConcurrentModificationException;
import java.util.function.Supplier;

public interface IShapeDescriptorProvider {
	IShapeDescriptor<?> getShapeDescriptor();

	boolean modifyShape(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException;

	boolean isModifyingShape();
}
