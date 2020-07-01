import { IDeckPlugin, Registry } from '@spinnaker/core';
import { HTTPNotificationType } from './HTTPNotificationType';

export const plugin: IDeckPlugin = {
  initialize: () => {
    Registry.pipeline.registerNotification({
      component: HTTPNotificationType,
      key: 'http',
      label: 'HTTP',
    });
  },
};
