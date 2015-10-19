'use strict';

angular.module('workmanagerApp')
    .controller('BatchesController', function ($scope, Batches, Assignments, FileUtil) {
        $scope.uploadError = null;

        $scope.downloadAttachment = FileUtil.downloadAttachment;

        /**
         * Define data for binding
         */
        $scope.searchparams = {};
        $scope.batches = [];
        $scope.file = null;
        $scope.modalId = null;

        var determineStatusClasses = function() {
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

        var dataChange = function(data) {
            $scope.batches=data;
            determineStatusClasses();
            $scope.clearAll();
        };


        $scope.clearAll = function() {
            $scope.uploadError = null;

            $scope.file = null;
            $scope.historyType = null;
            $scope.modalId = null;
            $scope.modalValue = null;
            $scope.history = null;

            $('#batch-create').modal('hide');
            $('#assignment-create').modal('hide');
            $('#assignment-complete').modal('hide');
            $('#history').modal('hide');
            $('#updateHours').modal('hide');
        };

        $scope.filterChange = function() {
            Batches.query({}, dataChange);
        };

        $scope.deleteAssignment = function(assignmentId) {
            Assignments.delete(
                {
                    'assignmentId': assignmentId
                },
                dataChange
            );
        };

        $scope.showCreateBatch = function() {
            $scope.file = null;
            $scope.modalId = null;
            $scope.showUserField = false;
            $scope.uploadFormTitle = "Create a Batch";
            $('#uploadForm').modal('show');

        };

        $scope.showCreateAssignment = function(batchId) {
            $scope.file = null;
            $scope.showUserField = true;
            $scope.modalId = batchId;
            $scope.uploadFormTitle = "Create an Assignment in Batch " + batchId;
            $('#uploadForm').modal('show');
        };

        $scope.showCompleteAssignment = function(assignmentId) {
            $scope.file = null;
            $scope.showUserField = false;
            $scope.modalId = assignmentId;
            $scope.uploadFormTitle = "Complete Assignment " + assignmentId;
            $('#uploadForm').modal('show');
        };

        $scope.showCompletedAssignmentHistory = function(assignmentId) {
            $scope.historyType="COMPLETED_ASSIGNMENT";
            $scope.modalId = assignmentId;
            Assignments.getCompletedAssignmentHistory({assignmentId: assignmentId}, function(result) {
                $scope.history = result;
            });

            $('#history').modal('show');
        };

        $scope.showCompletedReviewHistory = function(batchId) {
            $scope.historyType="COMPLETED_REVIEW";
            $scope.modalId = batchId;
            Batches.getCompletedReviewHistory({batchId: batchId}, function(result) {
                $scope.history = result;
            });
            $('#history').modal('show');
        };

        $scope.showSetHours = function(assignmentId,currentHours) {
            $scope.showHours=true;
            $scope.modalId=assignmentId;
            $scope.modalValue=currentHours;
            $('#updateHours').modal('show');
        };

        $scope.printUploadError = function() {
            $scope.error = "ERROR";
        };

        $scope.createBatch = function() {
            $scope.error = "ERROR";
            FileUtil.uploadFileToUrl({
                file: $scope.file,
                uploadUrl: '/batches',
                success: $scope.clearAll,
                falure: $scope.printUploadError
            });
        };

        $scope.createAssignment = function() {
            FileUtil.uploadFileToUrl({
                file: $scope.file,
                uploadUrl: '/assignments',
                success: $scope.clearAll,
                falure: $scope.printUploadError
            });
        };

        $scope.completeAssignment = function() {
            FileUtil.uploadFileToUrl({
                file: $scope.file,
                uploadUrl: '/assignments/' + $scope.modalId,
                success: $scope.clearAll,
                falure: $scope.printUploadError
            });
        };

        $scope.clearHours = function(assignmentId) {
            Assignments.clearHours(
                { assignmentId: assignmentId },
                function() {
                    $scope.clearAll();
                    $scope.filterChange();
                }
            );
        };

        $scope.submitHours = function(hours) {
            //TODO use modal id and $scope.hours
        };

        var startReview = function() {
            //goto('/batch/reviewComplete/' + assignment.id)
            //TODO
        };

        /**
         * Execute the things that need to be run at startup
         */
        $scope.filterChange();
    });
