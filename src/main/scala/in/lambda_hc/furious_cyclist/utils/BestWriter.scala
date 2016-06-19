package in.lambda_hc.furious_cyclist.utils

import scala.io.Source

/**
  * Created by vishnu on 18/6/16.
  */
object BestWriter {

  def main(args: Array[String]) {

    if (args.length == 1) {

      val lines = Source.fromFile(args.head).getLines().toArray

      val numOfTestCases = lines(0).toInt

      lines.slice(1, lines.length).grouped(2).foreach(i => {
        val N = i(0).toInt
        val (a, b) = i(1).split(" ").slice(0, N).partition(_ == "1")
        if (a.length == b.length)
          println("D")
        else if (a.length < b.length)
          println("B")
        else
          println("A")
      }
      )

    } else {
      println("Pass the file as an argument")
    }

  }

}
