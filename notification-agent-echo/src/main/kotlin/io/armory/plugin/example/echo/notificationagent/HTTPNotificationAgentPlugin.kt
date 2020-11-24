package io.armory.plugin.example.echo.notificationagent

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.netflix.spinnaker.echo.api.events.Event
import com.netflix.spinnaker.echo.api.events.NotificationAgent
import com.netflix.spinnaker.echo.api.events.NotificationParameter
import com.netflix.spinnaker.kork.plugins.api.httpclient.HttpClient
import com.netflix.spinnaker.kork.plugins.api.httpclient.Request
import com.netflix.spinnaker.kork.plugins.api.spring.PrivilegedSpringPlugin
import com.netflix.spinnaker.kork.plugins.sdk.httpclient.Ok3HttpClient
import okhttp3.OkHttpClient
import org.pf4j.PluginWrapper
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.EnableConfigurationProperties

class HTTPNotificationAgentPlugin(wrapper: PluginWrapper) : PrivilegedSpringPlugin(wrapper) {
  override fun registerBeanDefinitions(registry: BeanDefinitionRegistry) {
    registerBean(beanDefinitionFor(HTTPNotificationAgent::class.java), registry)
    registerBean(beanDefinitionFor(HTTPNotificationConfig::class.java), registry)
  }
}

@EnableConfigurationProperties
class HTTPNotificationAgent(config: HTTPNotificationConfig) : NotificationAgent {
  private val client: HttpClient
  private val AGENT_NAME = "http-notification-agent"
  private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  init {
    client = Ok3HttpClient("http-notification-agent", config.url, OkHttpClient(), mapper)
  }

  override fun getNotificationType() = "http"

  override fun sendNotifications(notification: MutableMap<String, Any>, application: String, event: Event, status: String) {
    val nc = notification.asNotificationConfig()
    client.post(Request(AGENT_NAME, nc.path ?: "").setBody(event))
  }

  override fun getParameters() = mutableListOf(NotificationParameter().apply {
    name = "path"
    label = "URL path"
    description = "Additional path to be appended to the HTTP URL when this notification is sent"
    type = NotificationParameter.ParameterType.string
    defaultValue = ""
  })

  private fun MutableMap<String, Any>.asNotificationConfig() = mapper.convertValue<NotificationConfig>(this)
}

data class NotificationConfig(val path: String?)
