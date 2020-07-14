package $group__.macros

import scala.annotation.compileTimeOnly

private object MacroTypeNamesImpl {
	def getTypeName[T](c: blackbox.Context)(implicit t: c.WeakTypeTag[T]): c.Expr[String] = {
		val r = showRaw(t.tpe.typeSymbol.name)
		reify {
			c.Expr[String] {
				Literal(Constant(r))
			}.splice
		}
	}

	def getTypeFullName[T](c: blackbox.Context)(implicit t: c.WeakTypeTag[T]): c.Expr[String] = {
		val r = showRaw(t.tpe.typeSymbol.fullName)
		reify {
			c.Expr[String] {
				Literal(Constant(r))
			}.splice
		}
	}
}

trait MacroTypeNames {
	import language.experimental.macros

	@compileTimeOnly("enable macro")
	def getTypeName[T]: String = macro MacroTypeNamesImpl.getTypeName[T]

	@compileTimeOnly("enable macro")
	def getTypeFullName[T]: String = macro MacroTypeNamesImpl.getTypeFullName[T]
}

object MacroTypeNames extends MacroTypeNames
