package blackboard

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import graph.{BytestringSplitter, MessageParser}

import scala.collection.immutable.Seq
import scala.util.{Failure, Success}

object WebSocketTest extends App {

  implicit val system: ActorSystem = ActorSystem("websocket-test")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  private val midiSource: Source[String, NotUsed] =
    Source.empty.via(Serial().open("/dev/ttyAMA0", SerialSettings(
      baud = 38400,
      characterSize = 8,
      twoStopBits = false,
      parity = Parity.None
    )))
      .via(new BytestringSplitter())
      .via(new MessageParser())
      .map(_.toString)

  private val wsHandler: Flow[Message, Message, NotUsed] =
    Flow[Message]
      .mapConcat(_ => Seq.empty[String])
      .merge(midiSource)
      .map(l => TextMessage(l.toString))

  val route =
    path("ws") {
      handleWebSocketMessages(wsHandler)
    }

  val port = 8080

  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  bindingFuture.onComplete {
    case Success(binding) =>
      val localAddress = binding.localAddress
      println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) =>
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }

}
