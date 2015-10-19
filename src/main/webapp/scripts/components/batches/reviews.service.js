'use strict';

angular.module('workmanagerApp')
    .factory('Assignments', function ($resource, $http, $log) {
        return $resource('reviews/:batchId', {}, {

        });

        retObj.startReview = function(batchId) {
            $http.post('batches/' + batchId + "/complete").then(function () {
                $scope.getBatches();
                $scope.determineStatusClasses();
            }, function (response) {
                $log.error("Unable to start review for " + batchId);
                commonUtils.processErrorResponse(response);
            });
        };

        retObj.cancelReview = function(batchId) {
            $http.delete('batches/' + batchId + "/complete").then(function () {
                $scope.getBatches();
                $scope.determineStatusClasses();
                $log.info("Assignment " + batchId + " review deleted");
            }, function (response) {
                $log.error("Unable to delete review for " + batchId);
                commonUtils.processErrorResponse(response);
            });
        };

    }
);
