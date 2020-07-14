package $group__.compat.convert

import java.util.{concurrent => juc}
import java.{lang => jl}

trait WrapAsJavaExtension {

	import WrappersExtension._

	import language.implicitConversions

	implicit def asJavaRunnable(sf: () => Unit): jl.Runnable = sf match {
		case AsScalaFromRunnable(jr) => jr
		case _ => AsJavaRunnable(sf)
	}

	def asJavaCallable[R](sf: () => R): juc.Callable[R] = sf match {
		case AsScalaFromCallable(jc) => jc
		case _ => AsJavaCallable(sf)
	}
}

object WrapAsJavaExtension extends WrapAsJavaExtension
