package com.virtuslab.test

import akka.actor.{ActorLogging, ActorSystem, Props, Actor}
import com.virtuslab.avs.core.ActorVersioningCore
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
 * set of classes for testing
 * shows all use cases included in project
 * @author Mikołaj Jakubowski
 */
object ObjectWithStaticValue {
  val staticValue = 3
}

/**
 * message that is the API of all actors below
 */
case class SimpleMessage(text: String)

/**
 * actors that the system starts with
 */
object PrinterActor {
  def props() = Props[PrinterActor]
}

/**
 * actor showing an upgrade of class with same FQN as previous version
 */
class PrinterActor extends Actor {

  def receive = {
    case text => println(s"version 2 with same FCN: $text")
  }
}

/**
 * actor showing an upgrade of class with different FQN as previous version
 */
class PrinterActor2 extends Actor with ActorLogging {

  def receive = {
    case SimpleMessage(text) => log.error(s"version 2: $text")
    case msg => log.error(msg.toString)
  }
}

/**
 * crashes system and causes it to revert to previous working version
 */
class PrinterActorCrash extends Actor with ActorLogging {

  def receive = {
    case msg => throw new RuntimeException
  }
}

/**
 * shows how actor class dependency is getting upgraded together with its class
 */
class PrinterActorStatic extends Actor with ActorLogging {

  def receive = {
    case msg => println(ObjectWithStaticValue.staticValue)
  }
}

/**
 * actor showing how state can be forwared upon stop to new actor
 */
class PrinterActorSave extends Actor with ActorLogging {

  override def postStop(): Unit = {
    context.parent ! "My state, that I want to forward to next version."
  }

  def receive = {
    case SimpleMessage(text) => log.error(s"saver: $text")
    case text => println(s"saver: $text")
  }
}

/**
 * actor receiving state from previous actor
 */
class PrinterActorLoad extends Actor with ActorLogging {

  def receive = {
    case SimpleMessage(text) => log.error(s"loader: $text")
    case msg: String => log.info(s"Previous version says: '$msg'")
    case text => println(s"loader: $text")
  }
}

object TestApp extends App {

  import com.virtuslab.avs._
  import ExecutionContext.Implicits.global

  /**
   * main application cake
   */
  val actorSystemModule = new ActorVersioningCore(ActorSystem("test")) {
    val printerRef = system.actorOf(PrinterActor.props().withVersioning)
    system.scheduler.schedule(1 second, 5 second, printerRef, SimpleMessage("Hello World!"))
  }


}

