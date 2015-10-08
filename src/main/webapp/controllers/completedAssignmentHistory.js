(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('uploadHistoryCtrl',
        function($scope, $http, $routeParams, $log, commonUtils, stateKeeper) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.assignments = [];

            $scope.goto = commonUtils.goto;

            $scope.downloadAttachment = commonUtils.downloadAttachment;

            /**
             * Define data for binding
             */
            $scope.assignmentId = parseInt($routeParams.assignmentId, 10);

            $scope.getAssignmentHistory = function() {
                $http.get('assignments/' + $scope.assignmentId + "/completed").then(
                    function (response) {
                        $log.debug("Retrieved Assignment " + $scope.assignmentId);
                        $scope.assignment = response.data
                    }, function (response) {
                        $log.error("Unable to get assignment " + $scope.assignmentId);
                        commonUtils.processErrorResponse(response);
                    }
                );
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
            $scope.getAssignmentHistory();

        }
    );
})();