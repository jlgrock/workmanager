'use strict';

angular.module('workmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('batches', {
                parent: 'site',
                url: '/batches',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'batches.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/batches/batches.html',
                        controller: 'BatchesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('batches');
                        return $translate.refresh();
                    }]
                }
            });
    });
