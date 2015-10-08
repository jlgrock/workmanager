(function() {
    /**
     * Config
     */
    var moduleName = 'com.github.jlgrock.informatix.workmanager';
    module = angular.module(moduleName);

    module.service('commonUtils', function ($http, $log, stateKeeper, $q, $timeout, $location, $window) {
        var uploadFileToUrl = function (arg) {
            if (!angular.isDefined(arg.file)) {
                var str = "Cannot upload a file when no file is attached";
                $log.error(str);
                throw str;
            }
            if (!angular.isDefined(arg.uploadUrl)) {
                var str = "Cannot upload a file when no uploadUrl is defined";
                $log.error(str);
                throw str;
            }

            var fd = new FormData();
            if (angular.isDefined(arg.params)) {
                var i = 0;
                var params = Object.keys(arg.params);
                for (; i < params.length; i = i + 1) {
                    fd.append(params[i], arg.params[params[i]]);
                }
            }

            //var blob = new Blob([$scope.file], {type: $scope.file.type});
            fd.append('file', arg.file);
            fd.append('name', arg.file.name);
            $http.post(arg.uploadUrl, fd, {
                transformRequest: angular.identity, //angular will try t serialize it, so we need to override that mechanism to leave the data intact.
                headers: { 'Content-Type': undefined } // let the browser set the content-type
            }).then(function () {
                if (angular.isDefined(arg.success) && angular.isFunction(arg.success)) {
                    arg.success();
                }
            }, function (response) {
                $log.error("Unable to upload file.");
                processErrorResponse(response);
            });
        };

        var download = function(urlLocation) {
            var defer = $q.defer();

            $timeout(function() {
                $window.location = urlLocation;

            }, 1000)
                .then(function() {
                    defer.resolve('success');
                }, function() {
                    defer.reject('error');
                });
            return defer.promise;
        };

        var downloadAttachment = function(id) {

            commonUtils.download('attachments/' + id + '/download').then(
                function () {
                    $log.info("Attachment " + id + " downloaded");

                }, function (response) {
                    $log.error("Unable to download attachment " + id);
                    processErrorResponse(response);
                }
            );
        };

        var goto = function(url){
            $location.path( url );
        };

        var processErrorResponse = function (response) {
            stateKeeper.clearAll();
            stateKeeper.error.status = response.status;
            if (response.data !== undefined && response.data.errorMsg !== undefined) {
                var str = response.data.errorMsg;
                stateKeeper.error.msg = str;
                $log.log(str);
            } else {
                var str = response.statusText;
                stateKeeper.error.msg = str;
                $log.log(str);
            }
        };

        var printError = function (str) {
            stateKeeper.clearAll();
            stateKeeper.error.msg = str;
            $log.log(str);
        };

        this.uploadFileToUrl = uploadFileToUrl;
        this.download = download;
        this.downloadAttachment = downloadAttachment;
        this.goto = goto;
        this.processErrorResponse = processErrorResponse;
        this.printError = printError;
    });
})();