'use strict';

angular.module('workmanagerApp')
    .factory('Assignments', function ($resource) {
        var resourceUrl = 'assignments/:assignmentId';
        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method: 'PUT' },
            'updateHours': {
                url: resourceUrl + '/hours',
                method: 'POST',
                transformRequest: function(data) {
                    if (data === undefined || data.hours === undefined) {
                        return undefined;
                    }
                    var result = angular.stringify(data.hours);
                    return result;
                }
            },
            'clearHours': {
                url: resourceUrl + '/hours',
                method: 'DELETE'
            },
            'getCompletedAssignmentHistory': {
                url: resourceUrl + '/completed',
                method: 'GET',
                isArray: true
            }
        });
    }
);
