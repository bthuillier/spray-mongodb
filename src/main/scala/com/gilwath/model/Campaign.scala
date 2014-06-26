package com.gilwath.model

import reactivemongo.bson._
import play.api.libs.json._
import reactivemongo.bson.BSONString
import play.api.libs.json.JsSuccess
import reactivemongo.bson.BSONInteger
import play.api.data.validation.ValidationError


case class Campaign(id: Option[BSONObjectID], title: String, advertiser: String, volume: Int)

object Campaign {

  implicit object BSONObjectIdReads extends Reads[BSONObjectID] {
    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(n) => JsSuccess(BSONObjectID.parse(n).get)
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }

  implicit object BSONObjectIdWrites extends Writes[BSONObjectID] {
    def writes(o: BSONObjectID): JsValue = JsString(o.stringify)
  }

  implicit val campaignFormat: Format[Campaign] = Json.format[Campaign]


  implicit object CampaignWriter extends BSONDocumentWriter[Campaign] {
    def write(campaign: Campaign): BSONDocument = BSONDocument(
      "_id" -> campaign.id.getOrElse(BSONObjectID.generate),
      "title" -> BSONString(campaign.title),
      "advertiser" -> BSONString(campaign.advertiser),
      "volume" -> BSONInteger(campaign.volume)
    )
  }

  implicit object CampaignReader extends BSONDocumentReader[Campaign] {
    def read(bson: BSONDocument): Campaign = Campaign(
      bson.getAs[BSONObjectID]("_id"),
      bson.getAs[String]("title").get,
      bson.getAs[String]("advertiser").get,
      bson.getAs[Int]("volume").get
    )
  }
}
