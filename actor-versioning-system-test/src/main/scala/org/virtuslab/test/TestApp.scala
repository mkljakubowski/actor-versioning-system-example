package org.virtuslab.test

import akka.actor.{ActorSystem, Props, Actor}
import org.virtuslab.avs.core.ActorVersioningCore
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
 * tested system with versioning
 * @author Mikołaj Jakubowski
 */
object PrinterActor {

  case class SimpleMessage(text: String)

  def props() = Props[PrinterActor]
}

/**
 * tested actor
 */
class PrinterActor extends Actor {

  import PrinterActor._

  def receive = {
    case SimpleMessage(text) => println(s"version 1: $text")
  }
}

object TestApp extends App {

  import PrinterActor._
  import com.virtuslab.avs._
  import ExecutionContext.Implicits.global

  val system = ActorSystem("test")

  /**
   * main application cake
   */
  val actorSystemModule = new ActorVersioningCore(system) {

    val printerRef = system.actorOf(PrinterActor.props().withVersioning)
    system.scheduler.schedule(1.second, 2.second, printerRef, SimpleMessage("Hello World!"))

  }

}

