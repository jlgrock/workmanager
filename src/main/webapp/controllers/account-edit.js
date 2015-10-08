(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('accountEditCtrl',
        function ($scope, $routeParams, $http, $log, commonUtils, stateKeeper) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;
            $scope.userparams = {};

            $scope.goto = commonUtils.goto;

            /**
             * Define data for binding
             */
            $scope.possibleRoles = ['ADMIN', 'USER'];
            $scope.id = $routeParams.id;

            $scope.load = function (id) {
                $log.debug("edit user=" + $scope.id);

                $http.get('accounts/' + $scope.id).then(
                    function(response){
                        var person = response.data;
                        $scope.userparams.id = person.id;
                        $scope.userparams.fname = person.fname;
                        $scope.userparams.lname = person.lname;
                        $scope.userparams.email = person.email;
                        $scope.selectedRole = person.role;
                    },
                    function(){
                        $log.error("Unable to delete User " + id);
                        commonUtils.processErrorResponse(response);
                    }
                );
            };


            $scope.save = function() {
                $log.log("save edit user=" + $scope.id);

                var id = $scope.userparams.id;
                var data = {
                    fname: $scope.userparams.fname,
                    lname: $scope.userparams.lname,
                    email: $scope.userparams.email,
                    role: $scope.selectedRole
                };

                $http.post('accounts/' + id, data).then(
                    function () {
                        commonUtils.goto( "/accounts" );
                    }, function (response) {
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
            $scope.load();
        }
    );
})();