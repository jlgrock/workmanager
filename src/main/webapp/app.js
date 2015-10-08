(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';

    /**
     * Module
     */
    var module;
    try {
        module = angular.module(moduleName);
    } catch (err) {
        // named module does not exist, so create one
        module = angular.module(moduleName, ['ngAnimate', 'ngCookies', 'ngRoute', 'ui.bootstrap', 'file-model']);
    }

    module.config(function ($routeProvider, $httpProvider) {

        $routeProvider.when("/", {
            redirectTo: "/batches"
        }).when('/batches', {
            templateUrl: 'templates/batches.html',
            controller: 'batchesCtrl'
        }).when('/hours/:assignmentId', {
            templateUrl: 'templates/hours.html',
            controller: 'hoursCtrl'
        }).when('/reporting', {
            templateUrl: 'templates/reporting.html',
            controller: 'reportingCtrl'
        }).when('/batch', {
            templateUrl: 'templates/batch-create.html',
            controller: 'batchCreateCtrl'
        }).when('/assignment/:batchId', {
            templateUrl: 'templates/assignment-create.html',
            controller: 'assignmentCreateCtrl'
        }).when('/assignment/:assignmentId/completedAssignments', {
            templateUrl: 'templates/completedAssignmentHistory.html',
            controller: 'uploadHistoryCtrl'
        }).when('/assignment/:assignmentId/completedReviews', {
            templateUrl: 'templates/completedAssignmentHistory.html',
            controller: 'uploadHistoryCtrl'
        }).when('/assignment/complete/:assignmentId', {
            templateUrl: 'templates/assignment-complete.html',
            controller: 'assignmentCompleteCtrl'
        }).when('/accounts', {
            templateUrl: 'templates/accounts.html',
            controller: 'accountsCtrl'
        }).when('/account', {
            templateUrl: 'templates/account.html',
            controller: 'accountCreateCtrl'
        }).when('/account/:id', {
            templateUrl: 'templates/account.html',
            controller: 'accountEditCtrl'
        }).when('/login', {
            templateUrl: 'templates/login.html',
            controller: 'loginCtrl'
        }).otherwise("/login");

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

        $httpProvider.defaults.withCredentials = true;

    });

    // if you end up on a page that isn't the login page (and you aren't logged in), redirect to the login page
    module.run(
        function($rootScope, security, $location) {

            // register listener to watch route changes
            $rootScope.$on( "$routeChangeStart", function(event, next, current) {
                if ( !angular.isDefined($rootScope.authenticated) || $rootScope.authenticated === null) {
                    security.authenticate();
                }
                if ( $rootScope.authenticated === false ) {
                    $location.path( "/login" );
                }
            })
        }
    );
})();