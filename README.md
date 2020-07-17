## Notification Agent Plugin

Spinnaker has a set of built-in types (e.g., Slack, email, SMS)
for pipeline notifications.

This plugin adds a new HTTP notification type as an example for plugin 
developers to modify and extend as they create integrations with their own 
notification systems. It's not meant for production use. It's meant for you
to copy as a starting point for creating your own notification plugin!

## Configuration

```
spinnaker:
  extensibility:
    plugins:
      Armory.NotificationAgent:
        enabled: true
        extensions:
          armory.httpNotificationService:
            enabled: true
            config:
              url: <fill-me-in>
```

## Known bugs

This plugin allows a developer to extend the `NotificationAgent` interface in
`Echo`. This interface handles most pipeline notifications. However, it does not handle notifications 
configured in Manual Judgment stages.
