import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FeatureSpec, Matchers, WordSpecLike}
import org.scalatest.prop.TableDrivenPropertyChecks._
import requests.Response
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser.decode
import akka.pattern.ask
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class LocationServiceSpec()
  extends TestKit(ActorSystem("LocationServiceSpec"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val locationClient = system.actorOf(LocationClient.props(), "location-client")
  val proxyCacheActor = system.actorOf(ProxyCacheActor.props(), "proxy-cache")

  val result = List.empty

  "A Proxy Cache" must {
    "initialise proxy cache on start" in {
      proxyCacheActor ! "asd"
      expectMsg("asd")
    }
  }

//  "A Location client" must {
//
//    "return location for proxy server" in {
//      val proxies: Seq[ProxyServer] = getListOfProxies("US")
//      locationClient ! proxies.head
//    }
//
//  }


  //  def getLocationViaProxy(proxy: ProxyServer): Option[Location] = {
//    val attempt: Try[Either[Error, Location]] = Try {
//      val resp: Response = requests.get(locationServiceUrl, proxy = (proxy.host, proxy.port))
//      val location = decode[Location](resp.text())
//      println(location)
//      location
//    }
//    attempt match {
//      case Failure(exception) => None
//      case Success(value) => value match {
//        case Left(value) => None
//        case Right(value) => Some(value)
//      }
//    }
//  }

//  def getListOfProxies(country: String) = {
//    val resp = requests.get(s"https://www.proxy-list.download/api/v1/get?type=http&anon=elite&country=$country")
//    val proxyServers = resp.text().split("\n").toSeq.map(_.trim).filter(_ != "")
//    proxyServers.flatMap(maybeProxyServer => maybeProxyServer match {
//      case s"$host:$port" => Some(ProxyServer(host, port.toInt))
//      case _ => None
//    })
//  }

  val countryTruthTable = Table(
    ("Country", "Expected Country Code", "Expected Instance", "Expected Locale", "Expected Language"),
    ("AD", "AD", "", "", ""),
    ("AE", "AE", "", "", ""),
    ("AF", "AF", "", "", ""),
    ("AG", "AG", "", "", ""),
    ("AI", "AI", "", "", ""),
    ("AL", "AL", "", "", ""),
    ("AM", "AM", "restrict", "en-IE", "en"),
    ("AO", "AO", "", "", ""),
    ("AQ", "AQ", "", "", ""),
    ("AR", "AR", "restrict", "en-IE", "en"),
    ("AS", "AS", "", "", ""),
    ("AT", "AT", "", "", ""),
    ("AU", "AU", "", "", ""),
    ("AW", "AW", "", "", ""),
    ("AX", "AX", "", "", ""),
    ("AZ", "AZ", "", "", ""),
    ("BA", "BA", "restrict", "en-IE", "en"),
    ("BB", "BB", "", "", ""),
    ("BD", "BD", "restrict", "en-IE", "en"),
    ("BE", "BE", "", "", ""),
    ("BF", "BF", "", "", ""),
    ("BG", "BG", "block", "en-IE", "en"),
    ("BH", "BH", "", "", ""),
    ("BI", "BI", "", "", ""),
    ("BJ", "BJ", "", "", ""),
    ("BL", "BL", "", "", ""),
    ("BM", "BM", "", "", ""),
    ("BN", "BN", "", "", ""),
    ("BO", "BO", "restrict", "en-IE", "en"),
    ("BQ", "BQ", "", "", ""),
    ("BR", "BR", "", "", ""),
    ("BS", "BS", "", "", ""),
    ("BT", "BT", "", "", ""),
    ("BV", "BV", "", "", ""),
    ("BW", "BW", "", "", ""),
    ("BY", "BY", "", "", ""),
    ("BZ", "BZ", "", "", ""),
    ("CA", "CA", "", "", ""),
    ("CC", "CC", "", "", ""),
    ("CD", "CD", "", "", ""),
    ("CF", "CF", "", "", ""),
    ("CG", "CG", "", "", ""),
    ("CH", "CH", "", "", ""),
    ("CI", "CI", "", "", ""),
    ("CK", "CK", "", "", ""),
    ("CL", "CL", "restrict", "en-IE", "en"),
    ("CM", "CM", "restrict", "en-IE", "en"),
    ("CN", "CN", "", "", ""),
    ("CO", "CO", "", "", ""),
    ("CR", "CR", "", "", ""),
    ("CU", "CU", "", "", ""),
    ("CV", "CV", "", "", ""),
    ("CW", "CW", "", "", ""),
    ("CX", "CX", "", "", ""),
    ("CY", "CY", "", "", ""),
    ("CZ", "CZ", "block", "en-IE", "en"),
    ("DE", "DE", "com", "de-DE", "de"),
    ("DJ", "DJ", "", "", ""),
    ("DK", "DK", "", "", ""),
    ("DM", "DM", "", "", ""),
    ("DO", "DO", "restrict", "en-IE", "en"),
    ("DZ", "DZ", "", "", ""),
    ("EC", "EC", "restrict", "en-IE", "en"),
    ("EE", "EE", "", "", ""),
    ("EG", "EG", "", "", ""),
    ("EH", "EH", "", "", ""),
    ("ER", "ER", "", "", ""),
    ("ES", "ES", "", "", ""),
    ("ET", "ET", "", "", ""),
    ("FI", "FI", "", "", ""),
    ("FJ", "FJ", "", "", ""),
    ("FK", "FK", "", "", ""),
    ("FM", "FM", "", "", ""),
    ("FO", "FO", "", "", ""),
    ("FR", "FR", "", "", ""),
    ("GA", "GA", "", "", ""),
    ("GB", "GB", "", "", ""),
    ("GD", "GD", "", "", ""),
    ("GE", "GE", "", "", ""),
    ("GF", "GF", "", "", ""),
    ("GG", "GG", "", "", ""),
    ("GH", "GH", "", "", ""),
    ("GI", "GI", "", "", ""),
    ("GL", "GL", "", "", ""),
    ("GM", "GM", "", "", ""),
    ("GN", "GN", "", "", ""),
    ("GP", "GP", "", "", ""),
    ("GQ", "GQ", "", "", ""),
    ("GR", "GR", "", "", ""),
    ("GS", "GS", "", "", ""),
    ("GT", "GT", "", "", ""),
    ("GU", "GU", "", "", ""),
    ("GW", "GW", "", "", ""),
    ("GY", "GY", "", "", ""),
    ("HK", "HK", "", "", ""),
    ("HM", "HM", "", "", ""),
    ("HN", "HN", "restrict", "en-IE", "en"),
    ("HR", "HR", "", "", ""),
    ("HT", "HT", "", "", ""),
    ("HU", "HU", "block", "en-IE", "en"),
    ("ID", "ID", "", "", ""),
    ("IE", "IE", "com", "en-IE", "en"),
    ("IL", "IL", "", "", ""),
    ("IM", "IM", "", "", ""),
    ("CM", "CM", "restrict", "en-IE", "en"),
    ("IO", "IO", "", "", ""),
    ("IQ", "IQ", "", "", ""),
    ("IR", "IR", "block", "en-IE", "en"),
    ("IS", "IS", "", "", ""),
    ("IT", "IT", "block", "en-IE", "en"),
    ("JE", "JE", "", "", ""),
    ("JM", "JM", "", "", ""),
    ("JO", "JO", "", "", ""),
    ("JP", "JP", "restrict", "en-IE", "en"),
    ("KE", "KE", "", "", ""),
    ("KG", "KG", "restrict", "en-IE", "en"),
    ("KH", "KH", "", "", ""),
    ("KI", "KI", "", "", ""),
    ("KM", "KM", "", "", ""),
    ("KN", "KN", "", "", ""),
    ("KP", "KP", "", "", ""),
    ("KR", "KR", "restrict", "en-IE", "en"),
    ("KW", "KW", "", "", ""),
    ("KY", "KY", "", "", ""),
    ("KZ", "KZ", "", "", ""),
    ("LA", "LA", "", "", ""),
    ("LB", "LB", "", "", ""),
    ("LC", "LC", "", "", ""),
    ("LI", "LI", "", "", ""),
    ("LK", "LK", "", "", ""),
    ("LR", "LR", "", "", ""),
    ("LS", "LS", "", "", ""),
    ("LT", "LT", "", "", ""),
    ("LU", "LU", "", "", ""),
    ("LV", "LV", "restrict", "en-IE", "en"),
    ("LY", "LY", "", "", ""),
    ("MA", "MA", "", "", ""),
    ("MC", "MC", "", "", ""),
    ("MD", "MD", "", "", ""),
    ("ME", "ME", "restrict", "en-IE", "en"),
    ("MF", "MF", "", "", ""),
    ("MG", "MG", "", "", ""),
    ("MH", "MH", "", "", ""),
    ("MK", "MK", "", "", ""),
    ("ML", "ML", "", "", ""),
    ("MM", "MM", "", "", ""),
    ("MN", "MN", "", "", ""),
    ("MO", "MO", "", "", ""),
    ("MP", "MP", "", "", ""),
    ("MQ", "MQ", "", "", ""),
    ("MR", "MR", "", "", ""),
    ("MS", "MS", "", "", ""),
    ("MT", "MT", "restrict", "en-IE", "en"),
    ("MU", "MU", "restrict", "en-IE", "en"),
    ("MV", "MV", "", "", ""),
    ("MW", "MW", "restrict", "en-IE", "en"),
    ("MX", "MX", "", "", ""),
    ("MY", "MY", "", "", ""),
    ("MZ", "MZ", "", "", ""),
    ("NA", "NA", "", "", ""),
    ("NC", "NC", "", "", ""),
    ("NE", "NE", "", "", ""),
    ("NF", "NF", "", "", ""),
    ("NG", "NG", "restrict", "en-IE", "en"),
    ("NI", "NI", "restrict", "en-IE", "en"),
    ("NL", "NL", "", "", ""),
    ("NO", "NO", "", "", ""),
    ("NP", "NP", "restrict", "en-IE", "en"),
    ("NR", "NR", "", "", ""),
    ("NU", "NU", "", "", ""),
    ("NZ", "NZ", "", "", ""),
    ("OM", "OM", "", "", ""),
    ("PA", "PA", "", "", ""),
    ("PE", "PE", "", "", ""),
    ("PF", "PF", "", "", ""),
    ("PG", "PG", "", "", ""),
    ("PH", "PH", "block", "en-IE", "en"),
    ("PK", "PK", "restrict", "en-IE", "en"),
    ("PL", "PL", "", "", ""),
    ("PM", "PM", "", "", ""),
    ("PN", "PN", "", "", ""),
    ("PR", "PR", "", "", ""),
    ("PS", "PS", "restrict", "en-IE", "en"),
    ("PT", "PT", "block", "en-IE", "en"),
    ("PW", "PW", "", "", ""),
    ("PY", "PY", "restrict", "en-IE", "en"),
    ("QA", "QA", "", "", ""),
    ("RE", "RE", "", "", ""),
    ("RO", "RO", "", "", ""),
    ("RS", "RS", "", "", ""),
    ("RU", "RU", "block", "en-IE", "en"),
    ("RW", "RW", "", "", ""),
    ("SA", "SA", "", "", ""),
    ("SB", "SB", "", "", ""),
    ("SC", "SC", "", "", ""),
    ("SD", "SD", "", "", ""),
    ("SE", "SE", "", "", ""),
    ("SG", "SG", "", "", ""),
    ("SH", "SH", "", "", ""),
    ("SI", "SI", "", "", ""),
    ("SJ", "SJ", "", "", ""),
    ("SK", "SK", "restrict", "en-IE", "en"),
    ("SL", "SL", "", "", ""),
    ("SM", "SM", "", "", ""),
    ("SN", "SN", "", "", ""),
    ("SO", "SO", "restrict", "en-IE", "en"),
    ("SR", "SR", "", "", ""),
    ("SS", "SS", "", "", ""),
    ("ST", "ST", "", "", ""),
    ("SV", "SV", "", "", ""),
    ("SX", "SX", "", "", ""),
    ("SY", "SY", "", "", ""),
    ("SZ", "SZ", "", "", ""),
    ("TC", "TC", "", "", ""),
    ("TD", "TD", "", "", ""),
    ("TF", "TF", "", "", ""),
    ("TG", "TG", "", "", ""),
    ("TH", "TH", "", "", ""),
    ("TJ", "TJ", "", "", ""),
    ("TK", "TK", "", "", ""),
    ("TL", "TL", "", "", ""),
    ("TM", "TM", "", "", ""),
    ("TN", "TN", "", "", ""),
    ("TO", "TO", "", "", ""),
    ("TR", "TR", "", "", ""),
    ("TT", "TT", "", "", ""),
    ("TV", "TV", "", "", ""),
    ("TW", "TW", "", "", ""),
    ("TZ", "TZ", "", "", ""),
    ("UA", "UA", "", "", ""),
    ("UG", "UG", "", "", ""),
    ("UM", "UM", "", "", ""),
    ("US", "US", "", "", ""),
    ("UY", "UY", "", "", ""),
    ("UZ", "UZ", "", "", ""),
    ("VA", "VA", "", "", ""),
    ("VC", "VC", "", "", ""),
    ("VE", "VE", "", "", ""),
    ("VG", "VG", "", "", ""),
    ("VI", "VI", "", "", ""),
    ("VN", "VN", "", "", ""),
    ("VU", "VU", "", "", ""),
    ("WF", "WF", "", "", ""),
    ("WS", "WS", "", "", ""),
    ("YE", "YE", "", "", ""),
    ("YT", "YT", "", "", ""),
    ("ZA", "ZA", "", "", ""),
    ("ZM", "ZM", "", "", ""),
    ("ZW", "ZW", "", "", "")
  )

  forAll(countryTruthTable)((countryCode, expectedCountryCode, expectedInstance, expectedLocale, expectedLanguage) => {
    proxyCacheActor ! "ah"
//    val proxies = getListOfProxies(countryCode)
//    implicit val timeout = Timeout(5 seconds)
//
//    if(proxies.nonEmpty){
//      val location = (locationClient ? proxies.head).mapTo[Location]
//      location.onComplete {
//        case Failure(exception) => {
//          Console.err.println(s"Failed fetching location $countryCode. ${exception.getMessage}")
//        }
//        case Success(location) => {
//          println(s"| $countryCode | ${proxies.head.host}:${proxies.head.port} | ${location.countryName} | ${location.instance} | ${location.locale} | ${location.language} |")
//          println(s""" ("$countryCode", "${countryCode}", "${location.instance}", "${location.locale}", "${location.language}"),""")
//          assert(location.countryCode === expectedCountryCode, s"Country code for $countryCode")
//          assert(location.instance === expectedInstance, s"Instance for $countryCode")
//          assert(location.locale === expectedLocale, s"Locale for $countryCode")
//          assert(location.language === expectedLanguage, s"Language for $countryCode")
//          println(s"$location ✔️")
//        }
//      }
//
//    }

//    println(s"${proxies.size} proxies found for $countryCode")
//    locationClient ! proxies.head
//    assert(proxies.nonEmpty, s"Did not find proxy for country $countryCode")
  })

}
