package com.gilwath

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import reactivemongo.api.MongoDriver
import spray.httpx.PlayJsonSupport
import reactivemongo.bson.BSONObjectID
import com.gilwath.model.{Campaigns, Campaign}
import scala.concurrent.ExecutionContext.Implicits._
import play.api.libs.json.Json
import spray.http.{StatusCodes, HttpResponse}
import com.gilwath.routing.BSONPathMatcher._



object Main extends App with SimpleRoutingApp with PlayJsonSupport {
  implicit val system = ActorSystem("my-system")
  val driver = new MongoDriver(system)
  implicit val db = driver.connection(Seq("localhost")).db("spray-mongodb")

  val route = pathPrefix("campaign") {
    pathEnd {
      post {
        entity(as[Campaign]) { campaign =>
          complete(Campaigns.insert(campaign))
        }
      } ~
      get {
        complete(Campaigns.findAll().map(c => Json.obj("campaigns" -> c)))
      }
    } ~
    path(bsonId) { id =>
      get {
        complete(Campaigns.findOneById(id))
      } ~
      put {
        entity(as[Campaign]) { campaign =>
          complete(Campaigns.update(campaign))
        }
      } ~
      delete {
        complete(Campaigns.delete(id).map(_ => HttpResponse(StatusCodes.OK)))
      }
    }
  }

  startServer("localhost", 9090) {
    pathSingleSlash {
      complete("hello world")
    } ~ route
  }

}
