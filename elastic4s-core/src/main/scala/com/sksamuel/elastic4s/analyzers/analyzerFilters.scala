package com.sksamuel.elastic4s.analyzers

import org.elasticsearch.common.xcontent.{XContentFactory, XContentBuilder}
import scala.collection.JavaConverters._

trait AnalyzerFilter {
  def name: String
}

trait AnalyzerFilterDefinition {
  def filterType: String
  protected[elastic4s] def build(source: XContentBuilder): Unit
  def json: XContentBuilder = {
    val builder = XContentFactory.jsonBuilder
    builder.startObject()
    builder.field("type", filterType)
    build(builder)
    builder.endObject()
    builder
  }
}

trait CharFilter extends AnalyzerFilter

trait CharFilterDefinition extends CharFilter with AnalyzerFilterDefinition

case object HtmlStripCharFilter extends CharFilter {
  val name = "html_strip"
}

case class MappingCharFilter(name: String, mappings: (String, String)*)
    extends CharFilterDefinition {

  val filterType = "mapping"

  def build(source: XContentBuilder): Unit = {
    source.field("mappings", mappings.map({ case (k, v) => s"$k=>$v" }).asJava)
  }
}

case class PatternReplaceCharFilter(name: String, pattern: String, replacement: String)
    extends CharFilterDefinition {

  val filterType = "pattern_replace"

  def build(source: XContentBuilder): Unit = {
    source.field("pattern", pattern)
    source.field("replacement", replacement)
  }
}

