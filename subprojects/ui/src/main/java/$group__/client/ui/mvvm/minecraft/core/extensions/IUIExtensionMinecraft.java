package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionMinecraft
		extends IUIExtension<ResourceLocation, IUIComponent> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT, AREA_UI + ".container");

	void render(final IAffineTransformStack stack, Point2D cursorPosition, double partialTicks);

	void crop(final IAffineTransformStack stack, EnumCropMethod method, Point2D mouse, double partialTicks, boolean write);

	void initialize(final IAffineTransformStack stack);

	void tick(final IAffineTransformStack stack);

	void removed(final IAffineTransformStack stack);

	@OnlyIn(Dist.CLIENT)
	enum EnumCropMethod {
		GL_SCISSOR,
		STENCIL_BUFFER,
	}
}
