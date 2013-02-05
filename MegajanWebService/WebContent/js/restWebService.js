angular.module('restWebService', ['ngResource']).factory('MegajanSystem', function($resource){
 
   var MegajanSystem = new Object();
   MegajanSystem.jobRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/getJobs/',
		   {port:':8080'},
		   {query: {method:'GET', params:{} }});// isArray:true}});
   
   MegajanSystem.statusRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/getStatuses/',
		   {port:':8080'},
		   {query: {method:'GET', params:{}, }});// isArray:true}});
   
   MegajanSystem.internalTypeRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/getInternalTypes/',
		   {port:':8080'},
		   {query: {method:'GET', params:{}, }});// isArray:true}});
   
   MegajanSystem.serviceGroupRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/getServiceGroups/',
		   {port:':8080'},
		   {query: {method:'GET', params:{}, }});// isArray:true}});
   
   MegajanSystem.userRes = $resource('http://localhost:port/MegajanWebService/jaxrs/system/getUsers/',
		   {port:':8080'},
		   {query: {method:'GET', params:{}, }});// isArray:true}});
   
   MegajanSystem.updateService = $resource('http://localhost:port/MegajanWebService/jaxrs/system/putJsonEntity/',
		   {port:':8080', entityQn: '@entityQn' },
		   {save: {method:'PUT', params:{ entity: '@jsonObject' } }});
   
   return MegajanSystem;
});