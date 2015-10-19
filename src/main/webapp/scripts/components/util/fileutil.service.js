(function() {
    /**
     * Config
     */
    var moduleName = 'workmanagerApp';
    module = angular.module(moduleName);

    module.service('FileUtil', function ($http, $log, $q, $timeout, $location, $window) {
        var uploadFileToUrl = function (arg) {
            var str;
            if (!angular.isDefined(arg.file)) {
                str = "Cannot upload a file when no file is attached";
                $log.error(str);
                throw str;
            }
            if (!angular.isDefined(arg.uploadUrl)) {
                str = "Cannot upload a file when no uploadUrl is defined";
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
                if (angular.isDefined(arg.failure) && angular.isFunction(arg.failure)) {
                    arg.failure();
                }
            });
        };

        var downloadAttachment = function(id) {
            var defer = $q.defer();

            $timeout(function() {
                $window.location = 'attachments/' + id + '/download';

            }, 1000)
                .then(function() {
                    defer.resolve('success');
                }, function() {
                    defer.reject('error');
                });
            return defer.promise;
        };

        this.uploadFileToUrl = uploadFileToUrl;
        this.downloadAttachment = downloadAttachment;

    });
})();
