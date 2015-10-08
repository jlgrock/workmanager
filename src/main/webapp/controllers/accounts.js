(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('accountsCtrl',
        function ($scope, $http, $log, commonUtils, stateKeeper) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            /**
             * Define data for binding
             */
            $scope.people = [];

            $scope.loadPeople = function () {
                var httpRequest = $http.get('accounts')
                    .then(function (response) {
                        $scope.people = response.data;
                    }
                );
            };

            var findAndDeleteUserById = function(id) {
                var i = 0;
                for (;i<$scope.people.length; i=i+1) {
                    if ($scope.people[i].id === id) {
                        $scope.people.splice(i, 1);
                    }
                }
            };

            $scope.remove = function (id) {
                $log.log("remove user=" + id);
                if (confirm("Are you sure you want to remove this user from the system?")) {
                    $http.delete('accounts/' + id).then(function () {
                        findAndDeleteUserById(id);
                        $log.info("User " + id + " properly deleted");
                    }, function (response) {
                        $log.error("Unable to delete User " + id);
                        commonUtils.processErrorResponse($scope, response);
                    });
                }
            };

            $scope.resetPassword = function (id) {
                $log.log("reset password user=" + id);
                if (confirm("Are you sure you want to reset the password for this user?")) {
                    $http.post("accounts/" + id + "/reset").then(
                        function () {
                            stateKeeper.clearAll();
                        }, function (response) {
                            $log.error("Unable to reset password for user " + id);
                            commonUtils.processErrorResponse($scope, response);
                        }
                    );
                }
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
            $scope.loadPeople();
        }
    );
})();