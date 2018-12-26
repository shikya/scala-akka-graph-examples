package com.shrikant.example

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey}
import java.util.Base64

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.{AcknowledgeRequest, PubSubMessage, PublishRequest, ReceivedMessage}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.duration._

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

  // Receive the message

  val subscriptionSource: Source[ReceivedMessage, NotUsed] = GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, subscription)

  val ackSink: Sink[AcknowledgeRequest, Future[Done]] = GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, subscription)

  subscriptionSource.map { message =>
    println("Got message from google pub sub > "+ message)
    message.ackId
  }.groupedWithin(1000, 1.minute)
    .map(AcknowledgeRequest.apply)
    .to(ackSink)

}
