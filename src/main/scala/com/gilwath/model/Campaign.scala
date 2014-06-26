package com.gilwath.model

import reactivemongo.bson._
import play.api.libs.json._
import scala.concurrent.Future
import play.api.libs.json.JsSuccess
import reactivemongo.bson.BSONInteger
import reactivemongo.api.DefaultDB
import play.api.libs.json.JsString
import reactivemongo.bson.BSONString
import play.api.data.validation.ValidationError
import reactivemongo.api.collections.default.BSONCollection

import scala.concurrent.ExecutionContext.Implicits._

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

object Campaigns {
  private def collection(implicit db: DefaultDB): BSONCollection = db("campaigns")

  def delete(id: BSONObjectID)(implicit db: DefaultDB): Future[Unit] =
    collection.remove(BSONDocument("_id" -> id)).map(_ => ())

  def update(campaign: Campaign)(implicit db: DefaultDB): Future[Campaign] =
    collection.update(BSONDocument("_id" -> campaign.id.get), campaign).map(_ => campaign)

  def insert(campaign: Campaign)(implicit db: DefaultDB): Future[Campaign] = {
    val c = campaign.copy(id = Some(BSONObjectID.generate))
    collection.save(c).map(_ => c)
  }

  def findOneById(id: BSONObjectID)(implicit db: DefaultDB): Future[Option[Campaign]] = {
    collection.find(BSONDocument("_id" -> id)).one[Campaign]
  }

  def findAll()(implicit db: DefaultDB): Future[List[Campaign]] =
    collection.find(BSONDocument()).cursor[Campaign].collect[List]()
}
