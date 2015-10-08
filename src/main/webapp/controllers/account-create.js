(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('accountCreateCtrl',
        function ($scope, $http, $log, stateKeeper, commonUtils) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            /**
             * Define data for binding
             */
            $scope.possibleRoles = ['ADMIN', 'USER'];

            $scope.save = function () {
                $log.log("save add [" + $scope.userparams.fname + ", " + $scope.userparams.lname + ", "
                    + $scope.userparams.email + "," + $scope.selectedRole + "]");

                var data = {
                    fname: $scope.userparams.fname,
                    lname: $scope.userparams.lname,
                    email: $scope.userparams.email,
                    role: $scope.selectedRole
                };

                $http.put('accounts', data).then(
                    function () {
                        commonUtils.goto("/accounts");
                    },
                    function (response) {
                        $log.error("Unable to add User ");
                        commonUtils.processErrorResponse(response);
                    }
                );
            };

            $scope.setRole = function (role) {
                $scope.selectedRole = role;
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
        }
    );
})();