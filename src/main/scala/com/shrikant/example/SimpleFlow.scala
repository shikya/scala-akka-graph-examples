package com.shrikant.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object SimpleFlow extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val src = Source(1 to 10)
  val doublerFlow = Flow[Int].map(_ * 2)
  val adderFLow = Flow[Int].map(_ + 1)
  val sink = Sink.foreach[Int](println)

  val runnableFlow = src.via(doublerFlow).via(adderFLow).to(sink)

  runnableFlow.run()
}
