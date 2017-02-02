var spaApp = angular.module("spaApp", ["ngRoute"]);

console.log("spaApp module created");

spaApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
            templateUrl: "events.html",
            controller: "spaController"
        })
        .when("/contacts", {
            templateUrl: "contacts.html",
        })
        .when("/profile", {
            templateUrl: "profile.html"
            ,controller: "spaController"
        })
});

spaApp.controller('spaController', function($scope, $http) {
    console.log("Initializing spaController");
    $scope.eventList = [];
    $scope.numEventAttendeesArray = [];
    $scope.user = {};

    $scope.showStuff = true;

    getPicture = function () {
        console.log("Going on a perilous journey for pictures.");
        $http.get("get-picture.json")
        .then(
            function successCallback (response) {
                console.log("Found something");
                $scope.imageSource = response.data.imageString;
            },

            function errorCallback (response) {
                console.log("No pictures =(");
            });
    };
    $scope.contactList = [];
    getContacts = function () {
        console.log("Going to get paul's contacts");
        var wrapper = {"email": "paul@gmail"};
        $http.post("get-user-contacts.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Yay!");
                $scope.contactList = response.data;
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("You have no friends. But only because of an error!");
            });
    };

    getUser = function() {
        console.log("Going to get user information using hardcoded email paul@gmail");
        var wrapper = {"email": "paul@gmail"};
        $http.post("get-user-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("got user data");
                $scope.user = response.data;
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Unable to get user info");
            });
    };

    $scope.getAttendees = function (id) {
        console.log("Looking for attendees.");
        console.log("id = " + id);
        var eventId = {"eventId":id};
        $http.post("get-event-attendees.json", eventId)
        .then(
            function successCallback (response) {
                console.log("Got data perhaps");
                $scope.userList = response.data;
                console.log($scope.userList);
            },

            function errorCallback(response) {
                console.log("No data?");
            });
    };
    //$scope.imageSource = "data:image/jpg;base64," + the_code_64_encoded_version_of_the_image;
    //We're removing the header because it's already in the file

    $scope.userList = [];
    $scope.getEvents = function () {
        console.log("Getting events");
        $http.get("/get-open-events.json")
        .then(
            function successCallback (response) {
                console.log("Got data");
                $scope.eventList = response.data;
                console.log($scope.eventList);
            },
            function errorCallback(response) {
                console.log("No data");
            });
    };


    $scope.getEvents();
    getUser();
    getPicture();
    getContacts();
});

spaApp.controller('controller1', function($scope) {
    console.log("initializing controller 1");
});
