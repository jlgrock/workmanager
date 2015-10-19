 'use strict';

angular.module('workmanagerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-workmanagerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-workmanagerApp-params')});
                }
                return response;
            },
        };
    });