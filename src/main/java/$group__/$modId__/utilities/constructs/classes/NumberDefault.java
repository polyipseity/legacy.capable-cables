package $group__.$modId__.utilities.constructs.classes;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;

public abstract class NumberDefault<T extends NumberDefault<T>> extends Number implements IStructureCloneable<T>, IOperable.INumberOperable<T> {
	/* SECTION static variables */

	private static final long serialVersionUID = 982068990995544271L;


	/* SECTION methods */

	@SuppressWarnings("EmptyMethod")
	@Override
	public String toString() { return super.toString(); }

	@Override
	public T clone() {
		try { return castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
			throw unexpected(e);
		}
	}
}
