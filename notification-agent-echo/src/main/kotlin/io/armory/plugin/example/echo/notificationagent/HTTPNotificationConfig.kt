package io.armory.plugin.example.echo.notificationagent

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("notification-agent")
class HTTPNotificationConfig {
  lateinit var url: String
}