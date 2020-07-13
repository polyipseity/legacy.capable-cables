package $group__.$modId__.compat.convert

trait Decorators {

	class AsJava[A](op: => A) {
		@inline
		final def asJava: A = op
	}

	class AsScala[A](op: => A) {
		@inline
		final def asScala: A = op
	}

}

object Decorators extends Decorators
