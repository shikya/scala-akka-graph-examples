package com.shrikant.example

import java.util.Base64

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.{PubSubMessage, PublishRequest}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

object GooglePubSubExample extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val projectId = ???
  val apiKey = ???
  val clientEmail = ???
  val privateKey = ???
  val topic = ???

  // Publish a message first
  val publishMessage = PubSubMessage( new String(Base64.getEncoder.encode("Hello".getBytes)), "1" )
  val publishRequest = PublishRequest(scala.collection.immutable.Seq(publishMessage))

  val source: Source[PublishRequest, NotUsed] = Source.single(publishRequest)

  val publishFlow: Flow[PublishRequest, Seq[String], NotUsed] = GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, topic)

  val publishedMessageIds: Future[Seq[Seq[String]]] = source.via(publishFlow).runWith(Sink.seq)
}
