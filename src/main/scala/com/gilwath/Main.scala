package com.gilwath

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import reactivemongo.api.MongoDriver
import spray.httpx.PlayJsonSupport
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import com.gilwath.model.Campaign
import scala.concurrent.ExecutionContext.Implicits._
import play.api.libs.json.Json
import spray.http.{StatusCodes, HttpResponse}

object Main extends App with SimpleRoutingApp with PlayJsonSupport {
  implicit val system = ActorSystem("my-system")
  val driver = new MongoDriver(system)
  val db = driver.connection(Seq("localdocker")).db("summit")
  val collection: BSONCollection = db("campaigns")

  import com.gilwath.model.Campaign._

  val route = pathPrefix("campaign"){
    pathEnd {
      get {
        complete(collection.find(BSONDocument()).cursor[Campaign].collect[List]().map(l => Json.obj("campaigns" -> l)))
      } ~
        post { entity(as[Campaign]) { campaign =>
          val c = campaign.copy(id = Some(BSONObjectID.generate))
          complete(collection.save(campaign).map(_ => c))
          }
        }
    } ~
      path(Segment) { id =>
        put {
          entity(as[Campaign]) { campaign =>
            complete(collection.update(BSONDocument("_id" -> campaign.id.get), campaign).map(_ => campaign))
          }
        } ~
          get {
            complete(collection.find(BSONDocument("_id" -> BSONObjectID(id))).one[Campaign])
          } ~
          delete {
            complete(collection.remove(BSONDocument("_id" -> BSONObjectID(id))).map(_ => HttpResponse(StatusCodes.OK)))
          }
      }
  }

  startServer("localhost", 9090) {
    pathSingleSlash {
      complete("hello world")
    } ~ route
  }

}
