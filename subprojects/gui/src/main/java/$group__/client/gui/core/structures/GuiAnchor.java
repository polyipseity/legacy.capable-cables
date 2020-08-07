package $group__.client.gui.core.structures;

import $group__.client.gui.core.events.EventGuiShapeDescriptor;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Immutable
public final class GuiAnchor implements IGuiAnchor {
	public static final ImmutableList<Function<GuiAnchor, Object>> OBJECT_VARIABLES = ImmutableList.of(
			GuiAnchor::getTo, GuiAnchor::getFromSide, GuiAnchor::getToSide, GuiAnchor::getBorderThickness, GuiAnchor::getContainer);
	public static final ImmutableMap<String, Function<GuiAnchor, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("to", "fromSide", "toSide", "borderThickness", "container"), OBJECT_VARIABLES));
	protected final IShapeDescriptor<?, ?> to;
	protected final EnumGuiSide fromSide, toSide;
	protected final double borderThickness;
	protected WeakReference<IGuiAnchorSet<?>> container = new WeakReference<>(null);

	public GuiAnchor(IShapeDescriptor<?, ?> to, EnumGuiSide fromSide, EnumGuiSide toSide) { this(to, fromSide, toSide, 0); }

	public GuiAnchor(IShapeDescriptor<?, ?> to, EnumGuiSide fromSide, EnumGuiSide toSide, double borderThickness) {
		this.to = to;
		this.fromSide = fromSide;
		this.toSide = toSide;
		this.borderThickness = borderThickness;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	protected void onShapeDescriptorModify(EventGuiShapeDescriptor.Modify event) {
		if (event.getStage() == EnumEventHookStage.POST && event.getShapeDescriptor() == getTo())
			getContainer().ifPresent(c -> anchor(c.getFrom()));
	}

	@Override
	public IShapeDescriptor<?, ?> getTo() { return to; }

	@Override
	public EnumGuiSide getToSide() { return toSide; }

	@Override
	public EnumGuiSide getFromSide() { return fromSide; }

	@Override
	public double getBorderThickness() { return borderThickness; }

	@Override
	public void anchor(IShapeDescriptor<?, ?> from) {
		Rectangle2D bounds = from.getShapeProcessed().getBounds2D();
		getFromSide().getSetter().accept(bounds,
				getFromSide().getApplyBorder().apply(
						getToSide().getGetter().apply(getTo().getShapeProcessed().getBounds2D()),
						getBorderThickness()));
		try {
			from.modify(from, cl -> {
				cl.bound(bounds);
				return true;
			});
		} catch (ConcurrentModificationException ex) {
			throw new ConcurrentModificationException("Anchor cycle involving: " + this, ex);
		}
	}

	@Override
	public void onContainerAdd(IGuiAnchorSet<?> container) {
		setContainer(container);
		Bus.FORGE.bus().get().register(this);
		Cleaner.create(container, this::onContainerRemove);
		anchor(container.getFrom());
	}

	@Override
	public void onContainerRemove() {
		Bus.FORGE.bus().get().unregister(this);
		setContainer(null);
	}

	protected Optional<IGuiAnchorSet<?>> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable IGuiAnchorSet<?> container) { this.container = new WeakReference<>(container); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
