package com.sksamuel.elastic4s

import scala.language.implicitConversions

/**
* Models one or more indexes, eg
* - "index1"
* - "index1,index2"
* - "_all"
*/
case class Indexes(values: Seq[String]) {
  def toIndexesAndTypes: IndexesAndTypes = IndexesAndTypes(values, Nil)
}

object Indexes {
  implicit def apply(index: String): Indexes = Indexes(Seq(index))
  implicit def apply(first: String, rest: String*): Indexes = Indexes(first +: rest)
  implicit def apply(indexes: Iterable[String]): Indexes = Indexes(indexes.toSeq)
}

/**
 * Models one index associated with one type.
 */
case class IndexAndType(index: String, `type`: String) {
  def toIndexAndTypes: IndexAndTypes = IndexAndTypes(index, Seq(`type`))
  def toIndexesAndTypes: IndexesAndTypes = IndexesAndTypes(Seq(index), Seq(`type`))
}

object IndexAndType {
  implicit def apply(str: String): IndexAndType = str.split('/') match {
    case Array(index, tpe) => IndexAndType(index, tpe)
    case _ => sys.error(s"Could not parse '$str' into index/type")
  }
}

/**
 * Models one index associated with one or more types.
 *
 * So for example,
 * - index1/type1
 * - index1/type1,type2
 */
case class IndexAndTypes(index: String, types: Seq[String]) {
  def toIndexesAndTypes = IndexesAndTypes(Seq(index), types)
}

object IndexAndTypes {
  implicit def apply(string: String): IndexAndTypes = {
    string.split("/") match {
      case Array(index) => IndexAndTypes(index, Nil)
      case Array(index, t) => IndexAndTypes(index, t.split(","))
      case _ => sys.error(s"Could not parse '$string' into index/type1[,type2,...]")
    }
  }
  implicit def apply(indexAndType: IndexAndType): IndexAndTypes = apply(indexAndType.index, indexAndType.`type`)
  implicit def apply(index: String, `type`: String): IndexAndTypes = IndexAndTypes(index, Seq(`type`))
  implicit def apply(indexAndType: (String, String)): IndexAndTypes = apply(indexAndType._1, indexAndType._2)

}

/**
 * Models one or more indexes associated with one or more types.
 *
 * So for example,
 * - index1/type1
 * - index1/type1,type2
 * - index1,index2/type1
 * - index1,index2/type1,type2
 */
case class IndexesAndTypes(indexes: Seq[String], types: Seq[String])

object IndexesAndTypes {

  implicit def apply(string: String): IndexesAndTypes = {
    string.split("/") match {
      case Array(index) => IndexesAndTypes(index.split(","), Nil)
      case Array(index, t) => IndexesAndTypes(index.split(","), t.split(","))
      case _ => sys.error(s"Could not parse '$string' into index1[,index2,...]/type1[,type2,...]")
    }
  }

  implicit def apply(indexAndType: IndexAndType): IndexesAndTypes = apply(indexAndType.index, indexAndType.`type`)

  // iterables of strings are assumed to be lists of indexes with no types
  implicit def apply(indexes: String*): IndexesAndTypes = apply(indexes)
  implicit def apply(indexes: Iterable[String]): IndexesAndTypes = IndexesAndTypes(indexes.toSeq, Nil)

  // a tuple is assumed to be an index and a type
  implicit def apply(indexAndType: (String, String)): IndexesAndTypes = apply(indexAndType._1, indexAndType._2)
  implicit def apply(index: String, `type`: String): IndexesAndTypes = IndexesAndTypes(Seq(index), Seq(`type`))

  implicit def apply(indexAndTypes: IndexAndTypes): IndexesAndTypes = indexAndTypes.toIndexesAndTypes
}

case class IndexesAndType(indexes: Seq[String], `type`: String)

object IndexesAndType {
  implicit def apply(indexAndType: IndexAndType): IndexesAndType = IndexesAndType(Seq(indexAndType.index), indexAndType.`type`)
}
