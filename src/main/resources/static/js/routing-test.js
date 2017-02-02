var testApp = angular.module("testApp", ["ngRoute"]);

console.log("testApp module created");

testApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
//            template: "<h2>test 1</h2>"
            templateUrl: "test1.html",
            controller: "controller1"
        })
        .when("/test2", {
//            template: "<h2>test 2</h2>"
            templateUrl: "test2.html",
            controller: "controller2"
        })
        .when("/test3", {
//            template: "<h2>test 3</h2>"
            templateUrl: "test3.html",
            controller: "controller3"
        })
});

testApp.controller('testController', function($scope, $http) {
    console.log("Initializing testController");
    $scope.testVar = "Testing Angular JS Routing";
    $scope.testItemList = [];

    $scope.getTestPeople = function () {
        console.log("Getting peeps");
        $http.get("/test-person-list.json")
        .then(
            function successCallback (response) {
                console.log("Awesome!");
                $scope.testItemList = response.data;
            },
            function errorCallback(response) {
                console.log("Unable to get data. Very sadface. So tears");
            });

    };
    $scope.getTestPeople();
});

testApp.controller('controller1', function($scope) {
    console.log("initializing controller 1");
    $scope.testItemList = [];
    $scope.testItemList[0] = {"firstName":"Paul", "lastName":"Dennis", "profession":"Starship Captain"};
});

testApp.controller('controller2', function($scope, $rootScope) {
    console.log("initializing controller 2");
    $scope.testGlobalValue = "Hello there";
    $rootScope.testGlobalValue = $scope.testGlobalValue;
});

testApp.controller('controller3', function($scope) {
    console.log("initializing controller 3");
});