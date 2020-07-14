package $group__.compat.convert

import java.util.{concurrent => juc}

trait WrapAsScalaExtension {

	import WrappersExtension._

	import language.implicitConversions

	implicit def asScalaFromRunnable(jr: Runnable): () => Unit = jr match {
		case AsJavaRunnable(sf) => sf
		case _ => AsScalaFromRunnable(jr)
	}

	implicit def asScalaFromCallable[R](jc: juc.Callable[R]): () => R = jc match {
		case AsJavaCallable(sf) => sf
		case _ => AsScalaFromCallable(jc)
	}
}

object WrapAsScalaExtension extends WrapAsScalaExtension
