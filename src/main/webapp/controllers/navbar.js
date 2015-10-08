(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('navbarCtrl',
        function ($rootScope, $scope, $http, $log, security, commonUtils) {
            $scope.goto = commonUtils.goto;

            $scope.logout = function () {
                $http.post('/logout').then(
                    function () {
                        $rootScope.authenticated = false;
                        $log.info("Successfully logged out");
                        $scope.goto("/login");
                    },
                    function () {
                        commonUtils.printError("Logout failed");
                    }
                );
            };

            /**
             * Execute the things that need to be run at startup
             */
            security.authenticate();

        }
    );
})();