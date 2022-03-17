package fix.pixiv

import scala.meta._

import scalafix.v1._
import util.SymbolConverter.SymbolToSemanticType

class ZeroIndexToHead extends SemanticRule("ZeroIndexToHead") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t @ Term.Apply(x1, List(Lit.Int(0))) =>
        try {
          if (x1.symbol.isAssignableTo(classOf[collection.Seq[Any]])
            && (!x1.symbol.isAssignableTo(classOf[collection.IndexedSeq[Any]]))) {
            Patch.replaceTree(
              t,
              Term.Select(x1, Term.Name("head")).toString
            )
          } else {
            Patch.empty
          }
        } catch {
          case _: Throwable => Patch.empty
        }
    }.asPatch
  }
}
