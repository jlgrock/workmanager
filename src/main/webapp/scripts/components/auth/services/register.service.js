'use strict';

angular.module('workmanagerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


