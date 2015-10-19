/* globals $ */
'use strict';

angular.module('workmanagerApp')
    .directive('workmanagerAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
