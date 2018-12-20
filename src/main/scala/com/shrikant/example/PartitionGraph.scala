package com.shrikant.example

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.RunnableGraph
import akka.stream.scaladsl.{GraphDSL, Partition, Sink, Source}

object PartitionGraph extends App {
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val in = Source(1 to 10)
    val out1 = Sink.foreach(println)
    val out2 = Sink.ignore

    val partition = builder.add(Partition[Int](2, o => if (o %2 == 1) 0 else 1))

    partition.out(0) ~> out1
    partition.out(1) ~> out2

    in ~> partition

    ClosedShape
  })

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  g.run(actorMaterializer)
}
