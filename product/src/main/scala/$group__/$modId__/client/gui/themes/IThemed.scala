package $group__.$modId__.client.gui.themes

import java.util.Optional

import javax.annotation.Nullable
import org.apache.logging.log4j.Logger

@SideOnly(Side.CLIENT)
object IThemed {
	def of[T <: ITheme[T], L <: Logger](@Nullable theme: T, mutator: IMutatorImmutablizable[_, _], logging: ILogging[Logger]): IThemed[T] = {
		if (theme == null) GuiThemedNull.getInstance[T]
		else new GuiThemed(theme, mutator, logging)
	}
}

@SideOnly(Side.CLIENT)
trait IThemed[T <: ITheme[T]] {
	@Nullable
	def getTheme: T

	@throws[UnsupportedOperationException]
	def setTheme(@Nullable theme: T): Unit = rejectUnsupportedOperationIf(!trySetTheme(theme))

	def trySetTheme(@Nullable theme: T): Boolean

	def tryGetTheme: Optional[T] = Optional.ofNullable(getTheme)
}
