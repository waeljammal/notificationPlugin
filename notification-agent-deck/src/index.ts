import { IDeckPlugin, Registry } from '@spinnaker/core';
import { HTTPNotificationType } from './HTTPNotificationType';

export const plugin: IDeckPlugin = {
  initialize: () => {
    Registry.pipeline.registerNotification({
      component: HTTPNotificationType,
      /** 
       * This key must match the "type" defined in the Echo plugin: 
       * https://github.com/spinnaker-plugin-examples/notificationPlugin/blob/master/notification-agent-echo/src/main/kotlin/io/armory/plugin/example/echo/notificationagent/HTTPNotificationAgentPlugin.kt#L29
       */
      key: 'http',
      label: 'HTTP',
    });
  },
};
