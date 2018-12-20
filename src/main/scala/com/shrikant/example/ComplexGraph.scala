package com.shrikant.example

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.RunnableGraph
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, Partition, Source}
import org.scalactic.Or

object ComplexGraph extends App{
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val in = Source(1 to 10)
    val out= Sink.foreach(println)

    val doubler = Flow[Int].map(_ * 2)
    val multi = Flow[Int].map(_ *10)
    val stringConvert = Flow[Int].map(_.toString)
    val append01 = Flow[String].map( _ + "a1")
    val oddEvenPartition = b.add(Partition[Int](2, o => if(o%2==1) 0 else 1))
    val bcast = b.add(Broadcast[Int](2))
    val merger = b.add(Merge[String](3))
    
    in ~> bcast ~>  stringConvert    ~> append01 ~>                   merger ~> out
          bcast ~>  oddEvenPartition
                    oddEvenPartition.out(0) ~> doubler  ~> stringConvert ~>  merger
                    oddEvenPartition.out(1) ~> multi    ~> stringConvert ~>  merger

    ClosedShape
  })

  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()

  g.run(materializer)
}
