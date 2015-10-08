(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('assignmentCreateCtrl',
        function ($scope, $log, $http, $routeParams, stateKeeper, commonUtils) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            $scope.file = null;
            $scope.people = [];
            $scope.batchId = parseInt($routeParams.batchId, 10);

            $scope.goto = commonUtils.goto;

            if (isNaN($scope.batchId)) {
                commonUtils.printError("Cannot process with invalid batch Id");
                return;
            }

            $scope.$watch('file', function (newVal) {
                if (newVal) {
                    $log.log(newVal);
                }
            });

            $scope.getUsers = function() {
                $http.get('/accounts').then(
                    function(response) {
                        $scope.people = response.data;
                    },
                    function(response) {
                        $log.error("Unable to get Users for Assignment");
                        commonUtils.processErrorResponse(response);
                    }
                )
            };

            $scope.upload = function() {
                var params = {
                    batchId: $scope.batchId,
                    userId: $scope.userSelected.id
                };
                commonUtils.uploadFileToUrl({
                    uploadUrl: '/assignments',
                    file: $scope.file,
                    success: angular.bind(this, $scope.goto, '/batches'),
                    params: params
                });
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
            $scope.getUsers();
        }
    );
})();