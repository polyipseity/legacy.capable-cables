package $group__.client.gui.traits;

import $group__.Globals;
import $group__.annotations.OverridingStatus;
import $group__.client.gui.polygons.Rectangle;
import $group__.traits.ISpec;
import $group__.utilities.Constants;
import $group__.utilities.helpers.specific.Throwables;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

/**
 * Represents an object that may be drawn.
 *
 * @param <N> type of {@link Number}
 *
 * @author William So
 * @since 0.0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IDrawable<N extends Number> extends ISpec<Rectangle<?, N>> {
	/**
	 * Tries drawing the given object.
	 * <p>
	 * Prefer this method over {@link #tryDraw()}.
	 *
	 * @param client {@link Minecraft} client
	 *
	 * @return whether the operation is completed successfully
	 *
	 * @since 0.0.1.0
	 */
	boolean tryDraw(Minecraft client);

	/**
	 * See {@link #tryDraw(Minecraft)}.
	 * <p>
	 * Useful when you do not have a {@link Minecraft} instance.
	 * Prefer {@link #tryDraw(Minecraft)} over this method.
	 *
	 * @return whether the operation is completed successfully
	 *
	 * @implSpec consider this method {@code final}
	 * @see #tryDraw(Minecraft) overloaded method
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default boolean tryDraw() { return tryDraw(Globals.Client.CLIENT); }

	/**
	 * Draws the given object.
	 * <p>
	 * Prefer {@link #tryDraw(Minecraft)} over this method.
	 *
	 * @param client {@link Minecraft} client
	 *
	 * @throws UnsupportedOperationException if the operation is incomplete
	 * @see #tryDraw(Minecraft) overloaded method
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default void draw(Minecraft client) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!tryDraw(client)); }

	/**
	 * See {@link #draw(Minecraft)}.
	 * <p>
	 * Prefer {@link #tryDraw()} over this method.
	 *
	 * @throws UnsupportedOperationException if the operation is incomplete
	 * @implSpec consider this method {@code final}
	 * @see #tryDraw(Minecraft) overloaded method
	 * @see #draw(Minecraft)
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default void draw() throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!tryDraw()); }
}
