package $group__.$modId__.client.gui.bases

import java.util.function.IntConsumer

@SideOnly(Side.CLIENT)
object GuiContainerBases {

	def initGuiBase[T <: GuiContainer with ISpec[Rectangle[_, _ <: Number]]](thisObj: T, superFunction: Runnable, xSizeSetter: IntConsumer, ySizeSetter: IntConsumer): Unit = {
		thisObj.spec.asScala.map(_.getSize).foreach(t => {
			xSizeSetter.accept(t.getX.intValue)
			ySizeSetter.accept(t.getY.intValue)
		})
		superFunction.run()
	}
}
