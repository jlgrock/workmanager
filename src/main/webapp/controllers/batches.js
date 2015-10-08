(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.controller('batchesCtrl',
        function ($scope, $http, $log, stateKeeper, commonUtils) {

            /**
             * Handle State based info
             */
            $scope.error = stateKeeper.error;

            $scope.goto = commonUtils.goto;

            $scope.downloadAttachment = commonUtils.downloadAttachment;

            /**
             * Define data for binding
             */
            $scope.searchparams = {};
            $scope.batches = [];


            $scope.getBatches = function() {
                var url = '/batches/';
                if ($scope.searchparams.batchName) {
                    url += '?searchTerm=' + $scope.searchparams.batchName;
                }
                $http.get(url).then(
                    function (response) {
                        $scope.batches = response.data;
                        $scope.determineStatusClasses();
                    },
                    function(response) {
                        $log.error("Unable to get batch list");
                        commonUtils.processErrorResponse(response);
                    }
                );
            };

            $scope.determineStatusClasses = function() {
                var i = 0;
                for (; i < $scope.batches.length; i = i + 1) {
                    var assignments = $scope.batches[i].assignments;
                    var j = 0;
                    for (; j < assignments.length; j = j + 1) {
                        if ($scope.batches[i].assignments[j].status === "COMPLETED") {
                            $scope.batches[i].assignments[j].class = "success";
                        } else if ($scope.batches[i].assignments[j].status === "NEEDS_HOURS") {
                            $scope.batches[i].assignments[j].class = "info";
                        } else if ($scope.batches[i].assignments[j].status === "FAILED") {
                            $scope.batches[i].assignments[j].class = "danger";
                        } else if ($scope.batches[i].assignments[j].status === "UNSTARTED") {
                            $scope.batches[i].assignments[j].class = "warning";
                        } else {
                            $scope.batches[i].assignments[j].class = "";
                        }
                    }
                    if ($scope.batches[i].status === "REVIEWED") {
                        $scope.batches[i].class = "panel-success";
                    } else if ($scope.batches[i].status === "COMPLETED") {
                        $scope.batches[i].class = "panel-info";
                    } else if ($scope.batches[i].status === "UNCOMPLETED") {
                        $scope.batches[i].class = "panel-danger";
                    } else {
                        $scope.batches[i].class = "panel-default";
                    }
                }
            };

            $scope.deleteAssignment = function (batchId, assignmentId) {
                if (angular.isUndefined(batchId) || angular.isUndefined(assignmentId)) {
                    $log.error("Unable to delete User with undefined id");
                    return;
                }
                $log.log("remove assignment=" + assignmentId);
                if (confirm("Are you sure you want to remove this assignment from the system?")) {
                    $http.delete('assignments/' + assignmentId).then(function () {
                        $scope.getBatches();
                        $scope.determineStatusClasses();
                        $log.info("Assignment " + assignmentId + " properly deleted");
                    }, function (response) {
                        $log.error("Unable to delete User " + assignmentId);
                        commonUtils.processErrorResponse(response);
                    });
                }
                setInterval(function () {
                    $scope.$apply();
                }, 100);
            };

            $scope.clearHours = function(batchId, assignmentId) {
                $http.delete('assignments/' + assignmentId + "/hours").then(function () {
                    $scope.getBatches();
                    $scope.determineStatusClasses();
                    $log.info("Assignment " + assignmentId + " hours properly deleted");
                }, function (response) {
                    $log.error("Unable to delete hours for " + assignmentId);
                    commonUtils.processErrorResponse(response);
                });
            };

            $scope.startReview = function(batchId) {
                $http.post('batches/' + batchId + "/complete").then(function () {
                    $scope.getBatches();
                    $scope.determineStatusClasses();
                    $log.info("Assignment " + batchId + " review started");
                }, function (response) {
                    $log.error("Unable to start review for " + batchId);
                    commonUtils.processErrorResponse(response);
                });
            };

            $scope.cancelReview = function(batchId) {
                $http.delete('batches/' + batchId + "/complete").then(function () {
                    $scope.getBatches();
                    $scope.determineStatusClasses();
                    $log.info("Assignment " + batchId + " review deleted");
                }, function (response) {
                    $log.error("Unable to delete review for " + batchId);
                    commonUtils.processErrorResponse(response);
                });
            };

            /**
             * Execute the things that need to be run at startup
             */
            stateKeeper.clearAll();
            $scope.getBatches();

        });
})();