package com.gilwath.libs.json

import play.api.libs.json.{JsString, JsValue, Writes}
import reactivemongo.bson.BSONObjectID

/**
 *
 * User: benjaminthuillier
 * Date: 26/06/14
 * Time: 18:10
 *
 */
object BSONWrites {
  implicit object BSONObjectIdWrites extends Writes[BSONObjectID] {
    def writes(o: BSONObjectID): JsValue = JsString(o.stringify)
  }
}
