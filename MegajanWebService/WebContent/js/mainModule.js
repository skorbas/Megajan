var mainModule = angular.module('mainModule', ['restWebService','ui']).config(function($routeProvider) {
	$routeProvider.when('/jobs', {controller:JobController, templateUrl:'joblist.html'}).
	when('/users', {controller:UserController, templateUrl:'userlist.html'}).
	when('/jobs/edit/:jobId', {controller:JobEditController, templateUrl:'jobedit.html'}).
	otherwise({redirectTo:'/jobs'});
	});

// checks system response status and in case of error displays error message to the user with stacktrace from the server
function serviceResponse( sysResp )
{
	if( sysResp.statusInfo.errorMsg == "OK" )
	{
 		return sysResp.dataContainer.data;
	}
	else
	{	
		alert( "Error on server: \n" + sysResp.statusInfo.errorMsg );
		return null;
	}	
}

// Controller providing data like users, statuses, types
function CommonDataController($scope, $location, MegajanSystem)
{	
	// create job filter that will be passed across scopes and used to initialise scope filters 
	$scope.jobFilter = new Object();
	$scope.jobFilter.jobId=null;
	$scope.jobFilter.status=null;
	$scope.jobFilter.internalType=null;
	$scope.jobFilter.serviceGroup=null;
	$scope.jobFilter.isValid = function(){
		return (this.jobId != null || this.status != null || this.internalType != null || this.serviceGroup != null);
	};

	
	var statResp = MegajanSystem.statusRes.query( function(){ 
		$scope.statuses = serviceResponse( statResp ); 
	});
	var internResp = MegajanSystem.internalTypeRes.query( function(){ 
		$scope.internalTypes = serviceResponse( internResp ); 
	});
	var srGroupResp = MegajanSystem.serviceGroupRes.query( function(){ 
		$scope.serviceGroups = serviceResponse( srGroupResp ); 
	});
	
};
	 
// Job controller
function JobController($scope, $location, MegajanSystem){
	
	$scope.refreshJobs = function()
	{	
		// angular js returns deserialized JavaScrtipt object from JSON string received from server
		var resp = MegajanSystem.jobRes.query( {id:$scope.jobFilter.jobId, 
			status:JSON.stringify($scope.jobFilter.status), 
			typeIntern:JSON.stringify($scope.jobFilter.internalType), 
			serviceGroup:JSON.stringify($scope.jobFilter.serviceGroup)}, function() {
			
			$scope.jobs = serviceResponse( resp );
		});
	};
	
	$scope.editJob = function(jobId)
	{
		$location.path('/jobs/edit/'+jobId);
	};
	
	// initial call
	if( $scope.jobFilter.isValid() ){
		$scope.refreshJobs();
	}
}

// Job edit controller
function JobEditController($scope, $location, $routeParams, MegajanSystem, $resource ){
	 
	var resp = MegajanSystem.jobRes.query( {id:$routeParams.jobId}, function() {
		
		var jobs =  serviceResponse( resp );
		//assert jbs.lenght == 1
		
		$scope.job = jobs[0]; // we are interested in the firest object on the list only
		
		// bind job's status with the status from main collection... - I expected to make it automatically
		// it seems Angular JS uses references for complex objects not equality...
		$scope.job.status = _.find($scope.statuses, function(status) {
	        return status.id == $scope.job.status.id;
	    });
		// bind job's typeIntern with the internType from main collection...
		$scope.job.typeIntern = _.find($scope.internalTypes, function(intType) {
	        return intType.id == $scope.job.typeIntern.id;
	    });
		// bind job's serviceGroup with the service group user from main collection...
		$scope.job.serviceGroupUser = _.find($scope.serviceGroups, function(srGroup) {
	        return srGroup.id == $scope.job.serviceGroupUser.id;
	    });
	});
	
	$scope.updateJob = function(job)
	{
		var updateJobRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/putJsonEntity',
				   {port:':8080'},
				   {save: {method:'PUT', params:{ entityQn:'@entityQn', entityObj:'@entityObj' } }});
		
		var resp = updateJobRes.save( { entityQn:'persistence.Job', entityObj:job }, function() {
			 
			//sync job with updated job object from the server
			job = serviceResponse( resp );	
		});
	};

}

// User controller
function UserController($scope, MegajanSystem){
	 
	$scope.refreshUsers = function(){
		var resp = MegajanSystem.userRes.query( function(){
			 
			//sync updated job object with the response from the server
			$scope.users = serviceResponse( resp );	
		});
	};
}