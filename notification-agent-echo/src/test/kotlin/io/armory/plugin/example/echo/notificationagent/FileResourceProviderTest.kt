package io.armory.plugin.example.echo.notificationagent

import com.netflix.spinnaker.kork.plugins.sdk.PluginSdksImpl
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext

class NotificationServiceTest : JUnit5Minutests {

  fun tests() = rootContext {

  }

  inner class Fixture {
    val subject = HTTPNotificationAgent(HTTPNotificationConfig("google.com"), PluginSdksImpl(listOf()))
  }
}

