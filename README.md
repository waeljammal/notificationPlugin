![CI](https://github.com/spinnaker-plugin-examples/notificationPlugin/workflows/CI/badge.svg)
![Latest Kork](https://github.com/spinnaker-plugin-examples/notificationPlugin/workflows/Latest%20Kork/badge.svg?branch=master)
![Latest Echo](https://github.com/spinnaker-plugin-examples/notificationPlugin/workflows/Latest%20Echo/badge.svg?branch=master)

## `NotificationAgent` Extension Point

Spinnaker allows users to configure notifications regarding the state of their deployment workflows. 
This is one of Spinnaker’s key features. You can ask, for example, to be notified via email 
or Slack about the failure of an individual stage or the success of a pipeline.

In the past, adding a new notification type required changes to Spinnaker’s open source repositories. 
This could be challenging or even impossible. Each new notification type required handcrafted UI code; 
organizations that wanted to send notifications via custom or internal services had few options.

With the `NotificationAgent`  extension point, you can add new notification types to Spinnaker without touching its core codebase.

The `NotificationAgent` interface is a new Spinnaker extension point. The interface and the changes
it relies on will not be available until Spinnaker 1.22.0 has been released. If
you want to install a plugin that uses the `NotificationAgent` extension point, you can use `master-latest-unvalidated`, Spinnaker's
nightly build.

You can develop a `NotificationAgent`-based plugin using [Echo@v2.31.0](https://github.com/spinnaker/echo) or later.

## HTTP Notification Plugin

This repo contains an example HTTP notification plugin for 
developers to modify and extend. It's not meant for production use. 

### Configuration

To install and configure the HTTP notification plugin you'll need to include this snippet in
your Spinnaker config:

```yaml
spinnaker:
  extensibility:
    plugins:
      Armory.NotificationAgent:
        enabled: true
        config:
          url: <fill-me-in>
    repositories:
      examplePluginRepository:
        url: https://raw.githubusercontent.com/spinnaker-plugin-examples/examplePluginRepository/master/repositories.json
```

This snippet needs to be accessible by Echo. 
Your exact path for configuring this plugin depends on the Spinnaker configuration
tool you're using (Halyard, the operator, Kleat, etc.). 
Please see the plugin installation guides on [spinnaker.io](https://spinnaker.io/guides/user/plugins). 

## Known bugs

The `NotificationAgent` interface handles most pipeline notifications. However, it does not handle notifications 
configured in Manual Judgment stages.

## Future work

Echo passes Spinnaker events to `NotificationAgent#sendNotifications`.
Plugin developers are responsible for mapping these events into messages
understandable by their target notification system (and end-users!).

There are lots of notification event types (`pipeline.failed`,
`stage.succeeded`, etc.), and they aren't well typed. 

Ideally this repo would include a test harness that tested each event type such that
developers could easily validate their plugin. 
