package $group__.$modId__.client.gui.traits

import java.util.Optional

import javax.annotation.Nullable
import org.apache.logging.log4j.Logger

@SideOnly(Side.CLIENT)
object IColored {
	def of[T, L <: Logger](@Nullable color: T, mutator: IMutatorImmutablizable[_, _], logging: ILogging[Logger]): IColored[T] = {
		if (color == null) GuiColorNull.getInstance[T]
		else new GuiColor(color, mutator, logging)
	}
}

@SideOnly(Side.CLIENT)
trait IColored[T] {
	@Nullable
	def getColor: T

	@throws[UnsupportedOperationException]
	def setColor(@Nullable color: T): Unit = rejectUnsupportedOperationIf(!trySetColor(color))

	def trySetColor(@Nullable color: T): Boolean

	def tryGetColor: Optional[T] = Optional.ofNullable(getColor)
}
