package $group__.compat.convert

import org.apache.logging.log4j.{util => l4ju}

object JavaConversionsExtension extends WrapAsJavaExtension with WrapAsScalaExtension {
	def asJavaLog4jSupplier[T](sf: () => T): l4ju.Supplier[T] = new l4ju.Supplier[T] {
		@inline
		override final def get(): T = sf()
	}
}
