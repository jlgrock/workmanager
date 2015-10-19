'use strict';

angular.module('workmanagerApp')
    .factory('Batches', function ($resource) {
        var resourceUrl = 'batches/:batchId';
        return $resource(
            resourceUrl,
            {
                //'batchName': '@batchName'
            },
            {
                'query': {
                    method: 'GET',
                    //params: {
                    //    'searchTerm': batchName
                    //},
                    isArray: true
                },
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' },
                'getCompletedReviewHistory': { url: resourceUrl + '/completed', method: 'GET', isArray: true },
                'complete': { url: resourceUrl + '/complete', method: 'POST' },
                'deleteCompleted': { url: resourceUrl + '/complete', method: 'DELETE' }
            }
        );
    });
