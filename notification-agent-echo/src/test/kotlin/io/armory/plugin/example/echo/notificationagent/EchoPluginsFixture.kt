package io.armory.plugin.example.echo.notificationagent

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.netflix.spinnaker.echo.Application
import com.netflix.spinnaker.kork.plugins.SpinnakerPluginManager
import com.netflix.spinnaker.kork.plugins.internal.PluginJar
import com.netflix.spinnaker.kork.plugins.tck.PluginsTckFixture
//import com.squareup.okhttp.mockwebserver.MockWebServer
import java.io.File
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc

class EchoPluginsFixture : PluginsTckFixture, EchoTestService() {

  final override val plugins = File("build/plugins")

  final override val enabledPlugin: PluginJar
  final override val disabledPlugin: PluginJar
  final override val versionNotSupportedPlugin: PluginJar

  override val extensionClassNames: MutableList<String> = mutableListOf(
    HTTPNotificationAgent::class.java.name
  )

  final override fun buildPlugin(pluginId: String, systemVersionRequirement: String): PluginJar {
    return PluginJar.Builder(plugins.toPath().resolve("$pluginId.jar"), pluginId)
      .pluginClass(HTTPNotificationAgentPlugin::class.java.name)
      .pluginVersion("1.0.0")
      .manifestAttribute("Plugin-Requires", "echo$systemVersionRequirement")
      .extensions(extensionClassNames)
      .build()
  }

  @Autowired
  override lateinit var spinnakerPluginManager: SpinnakerPluginManager

  @Autowired
  lateinit var applicationContext: ApplicationContext

  @Autowired
  lateinit var mockMvc: MockMvc

  val mapper = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

//  final val handler = MockWebServer()

  init {
    plugins.delete()
    plugins.mkdir()
    enabledPlugin = buildPlugin("io.armory.echo.enabled.plugin", ">=1.0.0")
    disabledPlugin = buildPlugin("io.armory.echo.disabled.plugin", ">=1.0.0")
    versionNotSupportedPlugin = buildPlugin("io.armory.echo.version.not.supported.plugin", ">=1000.0.0")
//    handler.start(9000)
  }
}

@SpringBootTest(classes = [Application::class])
@ContextConfiguration(classes = [PluginTestConfiguration::class])
@TestPropertySource(properties = ["spring.config.location=classpath:echo-plugins-test.yml"])
@AutoConfigureMockMvc
abstract class EchoTestService

@TestConfiguration
internal class PluginTestConfiguration