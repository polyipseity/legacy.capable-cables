package $group__.$modId__.client.gui.traits;

import $group__.$modId__.annotations.OverridingStatus;
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
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IDrawable {
	/* SECTION methods */

	/**
	 * Draws the given object.
	 * <p>
	 * Prefer this method over {@link #draw()}.
	 *
	 * @param client {@link Minecraft} client
	 * @return whether the operation is completed successfully
	 * @since 0.0.1.0
	 */
	@SuppressWarnings("SpellCheckingInspection")
	default boolean draw(Minecraft client) { return false; }

	/**
	 * See {@link #draw(Minecraft)}.
	 * <p>
	 * Useful when you do not have a {@link Minecraft} instance.
	 *
	 * @implSpec consider this method as {@code final}
	 *
	 * @see #draw(Minecraft) the overloaded method
	 * @return whether the operation is completed successfully
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = GROUP, when = When.NEVER)
	default boolean draw() { return draw(CLIENT); }
}
