package $group__.$modId__.compat.convert

import java.util.{concurrent => juc}
import java.{lang => jl}

trait WrappersExtension {

	case class AsJavaCallable[V](sf: () => V) extends juc.Callable[V] {
		@inline
		override final def call(): V = sf()
	}

	case class AsScalaFromCallable[+R](jc: juc.Callable[R]) extends (() => R) {
		@inline
		override final def apply(): R = jc.call()
	}

	case class AsJavaRunnable(sf: () => Unit) extends jl.Runnable {
		@inline
		override final def run(): Unit = sf()
	}

	case class AsScalaFromRunnable(jr: jl.Runnable) extends (() => Unit) {
		@inline
		override final def apply(): Unit = jr.run()
	}

}

@SerialVersionUID(1581925252635552158L)
object WrappersExtension extends WrappersExtension with Serializable
