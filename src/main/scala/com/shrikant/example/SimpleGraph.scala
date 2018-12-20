package com.shrikant.example

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.RunnableGraph
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, Sink, Source}

object SimpleGraph extends App {
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val in = Source(1 to 2)
    val out = Sink.foreach(println)

    val bcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

    in ~> bcast ~> f2       ~> merge ~> f3 ~> out
          bcast ~> f4 ~> f3 ~> merge

    ClosedShape
  })

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  g.run(actorMaterializer)
}
