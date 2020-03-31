package $group__.$modId__.client.gui.polygons;

import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.utilities.builders.BuilderStructure;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class Rectangle<T extends Rectangle<T, N>, N extends Number> extends Polygon4<T, N, List<XY<?, N>>> {
	/* SECTION variables */

	protected XY<?, N> offset;
	protected XY<?, N> size;


	/* SECTION constructors */

	public Rectangle(N offsetX, N offsetY, N sizeX, N sizeY, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { this(new XY<>(offsetX, offsetY, mutator, logging), new XY<>(sizeX, sizeY, mutator, logging), mutator, logging); }

	public Rectangle(XY<?, N> offset, XY<?, N> size, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(Arrays.asList(offset.toImmutable(), offset.sumX(size).toImmutable(), offset.sum(ImmutableList.of(size)).toImmutable(), offset.sumY(size).toImmutable()), mutator, logging);
		this.offset = offset;
		this.size = size;
	}

	public Rectangle(Rectangle<?, N> copy) { this(copy, copy.getMutator()); }


	protected Rectangle(Rectangle<?, N> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getOffset(), copy.getSize(), mutator, copy.getLogging()); }


	/* SECTION constructors */

	public static <T extends BuilderStructure<T, V>, V extends Rectangle<V, N>, N extends Number> BuilderStructure<T, V> newBuilderRectangle(XY<?, N> offset, XY<?, N> size) { return new BuilderStructure<>(t -> castUncheckedUnboxedNonnull(new Rectangle<>(offset, size, t.mutator, t.logging))); }

	public static <T extends BuilderStructure<T, V>, V extends Rectangle<V, N>, N extends Number> BuilderStructure<T, V> newBuilderRectangle(N offsetX, N offsetY, N sizeX, N sizeY) { return new BuilderStructure<>(t -> castUncheckedUnboxedNonnull(new Rectangle<>(offsetX, offsetY, sizeX, sizeY, t.mutator, t.logging))); }


	/* SECTION getters & setters */

	public XY<?, N> getOffset() { return offset; }

	public void setOffset(XY<?, N> offset) {
		XY<?, N> change = offset.sum(ImmutableList.of(getOffset().negate()));
		replaceAll(t -> t.sum(ImmutableList.of(change)).toImmutable());
		this.offset = offset;
	}

	public XY<?, N> getSize() { return size; }

	public void setSize(XY<?, N> size) {
		XY<?, N> change = size.sum(ImmutableList.of(getSize().negate()));
		set(1, b().sumX(change).toImmutable());
		set(2, c().sum(ImmutableList.of(change)).toImmutable());
		set(3, d().sumY(change).toImmutable());
		this.size = size;
	}

	public final void setOffsetAndSize(Rectangle<?, N> rect) { setOffsetAndSize(rect.getOffset(), rect.getSize()); }

	public void setOffsetAndSize(XY<?, N> offset, XY<?, N> size) {
		setOffset(offset);
		setSize(size);
	}

	@Override
	@Deprecated
	public void setA(XY<?, N> a) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setB(XY<?, N> b) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setC(XY<?, N> c) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setD(XY<?, N> d) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	public XY<?, N> max() { return a().max(ImmutableList.of(c())); }

	public XY<?, N> min() { return a().min(ImmutableList.of(c())); }
	

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new Rectangle<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
