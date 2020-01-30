package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.ISpec;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.Client.CLIENT;

@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, T extends IDrawable<N, T>> extends IStructureCloneable<T>, ISpec<Rectangle<N, ?>> {
	/* SECTION methods */

	@OverridingStatus(group = GROUP, when = When.NEVER)
	default void draw() { draw(CLIENT); }

	void draw(Minecraft client);
}
