package io.armory.plugin.example.echo.notificationagent

import com.fasterxml.jackson.module.kotlin.readValue
import com.netflix.spinnaker.echo.api.events.Event
import com.netflix.spinnaker.echo.api.events.Metadata
import com.netflix.spinnaker.echo.api.events.NotificationAgent
import com.netflix.spinnaker.echo.api.events.NotificationParameter
import com.netflix.spinnaker.kork.plugins.tck.PluginsTck
import com.netflix.spinnaker.kork.plugins.tck.serviceFixture
import com.squareup.okhttp.mockwebserver.MockResponse
import dev.minutest.rootContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import strikt.api.expect
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class HTTPNotificationAgentIntegrationTest : PluginsTck<EchoPluginsFixture>() {
  fun tests() = rootContext<EchoPluginsFixture> {
    context("an echo integration test environment") {
      serviceFixture {
        EchoPluginsFixture()
      }

      defaultPluginTests()

      test("agent handles an notification event, passes event to statically configured endpoint and dynamically configured path") {
        receiver.start(RECEIVER_PORT)

        receiver.enqueue(MockResponse().setResponseCode(200))

        val response = mockMvc.post("/") {
          contentType = MediaType.APPLICATION_JSON
          content = mapper.writeValueAsString(Event().apply {
            details = Metadata().apply {
              source = "orca"
              type = "orca:pipeline:complete"
              application = "my-test-app"
            }
            content = mapOf(
              "execution" to mapOf(
                "type" to "PIPELINE",
                "application" to "my-test-app",
                "notifications" to listOf(
                  mapOf(
                    "level" to "pipeline",
                    "type" to "http",
                    "when" to listOf("pipeline.complete"),
                    // The notification agent defines the key (see HTTPNotificationAgent#getParameters()).
                    // The value (in a non-test context!) is user-defined within Spinnaker's UI.
                    "path" to "/spinnaker-notifications"
                  )
                )
              )
            )
          })
        }.andReturn().response

        expect {
          that(response.status).isEqualTo(200)

          that(receiver.takeRequest()) {
            get { path }.isEqualTo("/spinnaker-notifications")
            get { body.readUtf8() }.isNotNull()
          }
          that(receiver.requestCount).isEqualTo(1)
        }

        after {
          receiver.shutdown()
        }
      }

      test("agent metadata (used for UI generation) is included in /notifications/metadata response") {
        val response = mockMvc.get("/notifications/metadata").andReturn().response

        expect {
          that(response.status).isEqualTo(200)
          that(mapper.readValue<List<NotificationMetadata>>(response.contentAsByteArray)) {
            get { find { it.notificationType == "http" } }
              .isNotNull()
              .and {
                get { uiType }.isEqualTo("BASIC")
                get { parameters.size }.isEqualTo(1)
              }
          }
        }
      }
    }
  }

  data class NotificationMetadata(val parameters: List<NotificationParameter>, val notificationType: String, val uiType: String)
}
