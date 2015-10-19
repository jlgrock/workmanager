'use strict';

angular.module('workmanagerApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
