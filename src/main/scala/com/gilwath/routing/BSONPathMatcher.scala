package com.gilwath.routing

import spray.routing._
import reactivemongo.bson.BSONObjectID
import scala.Some


object BSONPathMatcher {
  val bsonId: PathMatcher1[BSONObjectID] = PathMatcher("""[\da-fA-F]{24}""".r) flatMap { string =>
    Some(BSONObjectID(string))
  }
}
