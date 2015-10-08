(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('loginCtrl',
        function ($rootScope, $scope, $http, $log, $cookies, security, commonUtils, stateKeeper) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            $scope.credentials = {};

            $scope.forgotPassword = false;

            $scope.rememberMe = angular.isDefined($cookies.get("userName"));

            if (angular.isDefined($cookies.get("userName"))) {
                $scope.credentials.email = $cookies.get("userName");
            }

            $scope.login = function() {
                if ($scope.rememberMe) {
                    $cookies.put("userName", $scope.credentials.email);
                }
                security.authenticate($scope.credentials,
                    function (authenticated) {
                        if (authenticated) {
                            $rootScope.authenticated = true;
                            $log.log("Login succeeded")
                            $scope.goto("/");
                        } else {
                            $rootScope.authenticated = false;
                            commonUtils.printError("Login failed");
                            $scope.goto("/security");
                        }
                    }
                );
            };

            $scope.clickRememberMe = function() {
                if(!$scope.rememberMe) {
                    $cookies.remove("userName");
                }
            };

            $scope.resetPassword = function () {
                var config = {};
                config.params = {email: $scope.credentials.forgottenEmail};
                $http.get('accounts/reset', config).then(function () {
                    $rootScope.authenticated = false;
                    $scope.goto("/");
                }, function (response) {
                    commonUtils.processErrorResponse(response);
                });
            };

            $scope.toggleForgotPassword = function() {
                $scope.forgotPassword = !$scope.forgotPassword;
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
        }
    );
})();