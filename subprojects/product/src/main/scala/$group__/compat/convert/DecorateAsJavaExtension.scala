package $group__.compat.convert

import java.util.{concurrent => juc}
import java.{lang => jl}

trait DecorateAsJavaExtension {

	import Decorators._
	import WrapAsJavaExtension._

	import language.implicitConversions

	implicit def asJavaRunnableConverter(sf: () => Unit): AsJava[jl.Runnable] = new AsJava(asJavaRunnable(sf))

	implicit def asJavaCallableConverter[R](sf: () => R): AsJava[juc.Callable[R]] = new AsJava(asJavaCallable(sf))
}

object DecorateAsJavaExtension extends DecorateAsJavaExtension
