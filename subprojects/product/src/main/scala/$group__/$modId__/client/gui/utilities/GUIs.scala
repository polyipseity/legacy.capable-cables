package $group__.$modId__.client.gui.utilities

import java.awt.Color

import javax.annotation.Nullable
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.ResourceLocation

@SideOnly(Side.CLIENT)
object GUIs {
	val TEXTURE_MANAGER: TextureManager = CLIENT.getTextureManager

	def resetColor(): Unit = color(1F, 1F, 1F, 1F)

	def bindTexture(tex: ResourceLocation): Unit = TEXTURE_MANAGER.bindTexture(tex)

	def drawRectangle(@Nullable that: ITheme[_], rect: Rectangle[_, _], color: Color): Unit = getOrDefaultTheme(that).drawRect(rect, color)

	def drawModalRectWithCustomSizedTexture(@Nullable theme: ITheme[_], rect: Rectangle[_, _], tex: Rectangle[_, _]): Unit = getOrDefaultTheme(theme).drawModalRectWithCustomSizedTexture(rect, tex)

	private def getOrDefaultTheme(@Nullable theme: ITheme[_]): ITheme[_] = {
		if (theme == null) ITheme.NULL
		else theme
	}

	def drawScaledCustomSizeModalRect(@Nullable theme: ITheme[_], rect: Rectangle[_, _], tex: Rectangle[_, _], tile: XY[_, _]): Unit = getOrDefaultTheme(theme).drawScaledCustomSizeModalRect(rect, tex, tile)

	def translateAndScaleFromTo(from: Rectangle[_, _ <: Number], to: Rectangle[_, _ <: Number]): Unit = {
		val fromO = from.getOffset
		val fromS = from.getSize
		val toO = to.getOffset
		val toS = to.getSize
		val scaleX = toS.getX.doubleValue / fromS.getX.doubleValue
		val scaleY = toS.getY.doubleValue / fromS.getY.doubleValue
		scale(scaleX, scaleY, 1D)
		translate(toO.getX.doubleValue / scaleX - fromO.getX.doubleValue, toO.getY.doubleValue / scaleY - fromO.getY.doubleValue, 0D)
	}
}
