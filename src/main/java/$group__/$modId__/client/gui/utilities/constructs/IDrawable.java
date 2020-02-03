package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IDirty;
import $group__.$modId__.utilities.constructs.interfaces.basic.ISpec;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.Client.CLIENT;

/**
 * Represents an object that can be drawn.
 *
 * @author William So
 * @param <N> type of {@link Number} used for {@link Rectangle}
 * @param <T> type of this object
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number, T extends IDrawable<N, T>> extends IStructureCloneable<T>, ISpec<Rectangle<N, ?>>, IDirty {
	/* SECTION methods */

	/**
	 * Draws the given object.
	 * <p>
	 * Prefer this method over {@link #draw()}.
	 *
	 * @param client {@link Minecraft} client
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("SpellCheckingInspection")
	void draw(Minecraft client);

	/**
	 * See {@link #draw(Minecraft)}.
	 * <p>
	 * Useful when you do not have a {@link Minecraft} instance.
	 *
	 * @implSpec consider this method as {@code final}
	 *
	 * @see #draw(Minecraft) the overloaded method
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = GROUP, when = When.NEVER)
	default void draw() { draw(CLIENT); }
}
