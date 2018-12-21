package com.shrikant.example

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer}
import akka.stream.scaladsl.{Sink, Source}

object DivertToGraph extends App {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()

    val in = Source(1 to 10)
    val out1 = Sink.foreach(println)
    val out2 = Sink.ignore

    in.divertTo(out2, x=>x%2==0)
      .to(out1).run()

}
