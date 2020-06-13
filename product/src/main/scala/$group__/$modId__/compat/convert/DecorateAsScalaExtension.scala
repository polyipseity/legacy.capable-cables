package $group__.$modId__.compat.convert

import java.util.{concurrent => juc}
import java.{lang => jl}

trait DecorateAsScalaExtension {

	import Decorators._
	import WrapAsScalaExtension._

	import language.implicitConversions

	implicit def asScalaFromRunnableConverter(jr: jl.Runnable): AsScala[() => Unit] = new AsScala(asScalaFromRunnable(jr))

	implicit def asScalaFromCallableConverter[R](jc: juc.Callable[R]): AsScala[() => R] = new AsScala(asScalaFromCallable(jc))
}

object DecorateAsScalaExtension extends DecorateAsScalaExtension
