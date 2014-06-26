package com.gilwath.libs.json

import play.api.libs.json.{JsString, JsValue, Writes}
import reactivemongo.bson.BSONObjectID


object BSONWrites {
  implicit object BSONObjectIdWrites extends Writes[BSONObjectID] {
    def writes(o: BSONObjectID): JsValue = JsString(o.stringify)
  }
}
