package com.sksamuel.elastic4s.searches.queries

import com.sksamuel.elastic4s.searches.QueryDefinition
import org.elasticsearch.index.query.TermsQueryBuilder

trait GenericTermsQueryDefinition extends QueryDefinition {

  def builder: TermsQueryBuilder

  def boost(boost: Double): this.type = {
    builder.boost(boost.toFloat)
    this
  }

  def queryName(queryName: String): this.type = {
    builder.queryName(queryName)
    this
  }
}
