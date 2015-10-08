(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('assignmentCompleteCtrl',
        function ($scope, $log, $http, $routeParams, stateKeeper, commonUtils) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            $scope.file = null;
            $scope.assignmentId = parseInt($routeParams.assignmentId, 10);

            if (isNaN($scope.assignmentId)) {
                commonUtils.printError("Cannot process with invalid assignment Id");
                return;
            }

            $scope.goto = commonUtils.goto;

            $scope.$watch('file', function (newVal) {
                if (newVal) {
                    $log.log(newVal);
                }
            });

            $scope.upload = function() {
                var params = {
                    id: $scope.assignmentId
                };
                commonUtils.uploadFileToUrl({
                    uploadUrl: '/assignments/' + $scope.assignmentId,
                    file: $scope.file,
                    success: angular.bind(this, $scope.goto, '/batches'),
                    params: params
                });
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
        }
    );
})();