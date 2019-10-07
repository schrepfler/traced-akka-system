// Akka-Http

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.{ClientTransport, Http}
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

import scala.concurrent.Future

case class ProxyServer(host: String, port: Int)

case class Location(countryName: String, countryCode: String, instance: String, locale: String, language: String)

class LocationClient extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  implicit val locationDecoder: Decoder[Location] = deriveDecoder

  def receive = {

    case proxy: ProxyServer => {
      val httpsProxyTransport = ClientTransport.httpsProxy(InetSocketAddress.createUnresolved(proxy.host, proxy.port))
      val settings = ConnectionPoolSettings(context.system)
        .withConnectionSettings(ClientConnectionSettings(context.system)
          .withTransport(httpsProxyTransport))
      log.debug(s"requesting with $proxy")

      http.singleRequest(HttpRequest(uri = "https://api.staging.addisonglobal.net/location"), settings = settings)
        .pipeTo(self)(sender())
        .recover{
          case ex => {
            val s = self
            val se = sender()
            log.error(s"Request failure: ${ex.getMessage}")
            Location("N/A", "N/A", "N/A", "N/A", "N/A")
          }
        }.pipeTo(sender())
    }

    case HttpResponse(StatusCodes.OK, headers, entity, _) => {
      val location = Unmarshal(entity).to[Location]
      log.debug(s"Fetched location $location for ${sender()}")
      location
      }.pipeTo(sender())

    case resp@HttpResponse(code, _, _, _) => {
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
      Future(Location("N/A", "N/A", "N/A", "N/A", "N/A"))
    }.pipeTo(sender())

  }

}
