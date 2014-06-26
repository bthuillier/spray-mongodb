package com.gilwath.routing

import spray.routing._
import reactivemongo.bson.BSONObjectID
import scala.Some

/**
 *
 * User: benjaminthuillier
 * Date: 26/06/14
 * Time: 17:26
 *
 */
object BSONPathMatcher {
  val bsonId: PathMatcher1[BSONObjectID] = PathMatcher("""[\da-fA-F]{24}""".r) flatMap { string =>
    Some(BSONObjectID(string))
  }
}
