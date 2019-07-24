package example

import com.dimafeng.testcontainers.{CassandraContainer, ForAllTestContainer, GenericContainer, KafkaContainer, MultipleContainers, PostgreSQLContainer}
import org.scalatest.FeatureSpec

class TracedSystemSpec extends FeatureSpec with ForAllTestContainer {

  val kafka = new KafkaContainer
  val postgres = new PostgreSQLContainer
  val userService = new GenericContainer("user-service")
  val externalUserService = new GenericContainer("external-user-service")
  val zipkin = new GenericContainer("openzipkin/zipkin")

  override val container = MultipleContainers(kafka, postgres, userService, externalUserService, zipkin)

  feature("System startup") {

    scenario("User Service started") {

    }

    scenario("External User Service started") {

    }

    scenario("PostgreSQL started") {

    }

    scenario("Kafka started") {

    }

    scenario("Zipkin started") {

    }

  }


}
