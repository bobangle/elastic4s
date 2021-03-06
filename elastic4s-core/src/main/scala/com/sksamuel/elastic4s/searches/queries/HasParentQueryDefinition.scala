package com.sksamuel.elastic4s.searches.queries

import com.sksamuel.elastic4s.searches.{HighlightDefinition, QueryDefinition}
import org.elasticsearch.index.query.{HasParentQueryBuilder, InnerHitBuilder, QueryBuilders}

case class HasParentQueryDefinition(`type`: String,
                                    query: QueryDefinition,
                                    score: Boolean,
                                    boost: Option[Double] = None,
                                    ignoreUnmapped: Option[Boolean] = None,
                                    innerHit: Option[InnerHitDefinition] = None,
                                    queryName: Option[String] = None)
  extends QueryDefinition {

  def builder: HasParentQueryBuilder = {
    val builder = QueryBuilders.hasParentQuery(`type`, query.builder, score)
    boost.map(_.toFloat).foreach(builder.boost)
    innerHit.map(_.builder).foreach(builder.innerHit)
    ignoreUnmapped.foreach(builder.ignoreUnmapped)
    queryName.foreach(builder.queryName)
    builder
  }

  def boost(boost: Double): HasParentQueryDefinition = copy(boost = Some(boost))
  def ignoreUnmapped(ignoreUnmapped: Boolean): HasParentQueryDefinition = copy(ignoreUnmapped = Some(ignoreUnmapped))
  def innerHit(innerHit: InnerHitDefinition): HasParentQueryDefinition = copy(innerHit = Some(innerHit))
  def queryName(queryName: String): HasParentQueryDefinition = copy(queryName = Some(queryName))
}

case class InnerHitDefinition(name: String,
                              highlight: Option[HighlightDefinition] = None) {

  def builder = {
    val builder = new InnerHitBuilder().setName(name)
    // todo
    // highlight.foreach(highlight => builder.setHighlightBuilder(highlight.builder))
    builder
  }

  def highlighting(highlight: HighlightDefinition) = copy(highlight = Some(highlight))
}
