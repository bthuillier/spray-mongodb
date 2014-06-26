package com.gilwath.libs.json

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.data.validation.ValidationError


object BSONReads {
  implicit object BSONObjectIdReads extends Reads[BSONObjectID] {
    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(n) => JsSuccess(BSONObjectID.parse(n).get)
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }
}
