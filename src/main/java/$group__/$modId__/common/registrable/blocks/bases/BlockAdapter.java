package $group__.$modId__.common.registrable.blocks.bases;

import $group__.$modId__.common.registrable.utilities.constructs.IForgeRegistryEntryExtension;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IAdapter;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.common.registrable.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.Bulk.copyFields;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class BlockAdapter<B extends Block, T extends BlockAdapter<B, T>> extends Block implements IAdapter.IImmutable<B, T>, IForgeRegistryEntryExtension<Block> {
	/* SECTION variables */

	protected final B block;


	/* SECTION constructors */

	@SuppressWarnings("ConstantConditions")
	public BlockAdapter(B block) {
		super(null, null);
		this.block = block;
		initializeInstance(this);
	}


	/* SECTION static methods */

	@SuppressWarnings("UnusedReturnValue")
	private static <T extends BlockAdapter<?, ?>> T initializeInstance(T t) {
		copyFields(Block.class, t.getBlock(), t);
		return t;
	}


	/* SECTION getters & setters */

	public B getBlock() {
		return block;
	}

	@Deprecated
	public void setBlock(@SuppressWarnings("unused") B value) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public B get() { return getBlock(); }

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void set(B value) { setBlock(value); }

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public Block setUnlocalizedName(ResourceLocation name) { return setUnlocalizedName(getNamespacedUnlocalizedNameForRegistry(name)); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@Override
	@OverridingMethodsMustInvokeSuper
	@Deprecated
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { throw rejectUnsupportedOperation(); }
}
