(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('batchCreateCtrl',
        function ($scope, $log, $http, stateKeeper, commonUtils) {
            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            /**
             * Define data for binding
             */
            $scope.file = null;


            $scope.$watch('file', function (newVal) {
                if (newVal) {
                    $log.log(newVal);
                }
            });

            $scope.upload = function() {
                commonUtils.uploadFileToUrl({
                    file: $scope.file,
                    uploadUrl: '/batches',
                    success: angular.bind(this, $scope.goto, '/batches')
                });
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
        }
    );
})();