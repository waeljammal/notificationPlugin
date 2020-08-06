package io.armory.plugin.example.echo.notificationagent

import com.netflix.spinnaker.kork.plugins.tck.PluginsTck
import com.netflix.spinnaker.kork.plugins.tck.serviceFixture
import dev.minutest.rootContext

class HTTPNotificationAgentIntegrationTest : PluginsTck<EchoPluginsFixture>() {
  fun tests() = rootContext<EchoPluginsFixture> {
    context("an echo integration test environment and an echo plugin") {
      serviceFixture {
        EchoPluginsFixture()
      }

      defaultPluginTests()

//      test("HTTPNotificationAgent handles an http notification event") {
//        handler.enqueue(MockResponse().setResponseCode(200))
//
//        val message = mapper.writeValueAsString(Event().apply {
//          details = Metadata().apply {
//            source = "orca"
//            type = "orca:stage:complete"
//            application = "my-test-app"
//          }
//          content = mapOf(
//            "execution" to mapOf(
//              "type" to "PIPELINE",
//              "application" to "my-test-app",
//              "notifications" to listOf(
//                mapOf(
//                  "level" to "stage",
//                  "type" to "http",
//                  "when" to listOf("stage.complete")
//                )
//              )
//            )
//          )
//        })
//
//        val response = mockMvc.post("/") {
//          contentType = MediaType.APPLICATION_JSON
//          content = message
//        }.andReturn().response
//
//        expect {
//          that(response.status).isEqualTo(200)
//
//          that(handler.requestCount).isEqualTo(1)
//          that(handler.takeRequest()) {
//            get { body.readUtf8() }.isEqualTo(message)
//          }
//        }
//      }
    }
  }
}