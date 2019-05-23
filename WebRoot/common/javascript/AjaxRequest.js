// ===================================================================
// Author: Matt Kruse <matt@ajaxtoolbox.com>
// WWW: http://www.AjaxToolbox.com/
//
// NOTICE: You may use this code for any purpose, commercial or
// private, without any further permission from the author. You may
// remove this notice from your final code if you wish, however it is
// appreciated by the author if at least my web site address is kept.
//
// You may *NOT* re-distribute this code in any way except through its
// use. That means, you can include it in your product, or your web
// site, or any other form where the code is actually being used. You
// may not put the plain javascript up on your site for download or
// include it in your javascript libraries for download. 
// If you wish to share this code with others, please just point them
// to the URL instead.
// Please DO NOT link directly to my .js files from your site. Copy
// the files to your server and use them there. Thank you.
// ===================================================================

/**
 * The AjaxRequest class is a wrapper for the XMLHttpRequest objects which 
 * are available in most modern browsers. It simplifies the interfaces for
 * making Ajax requests, adds commonly-used convenience methods, and makes 
 * the process of handling state changes more intuitive.
 * An object may be instantiated and used, or the Class methods may be used 
 * which internally create an AjaxRequest object.
 */
 
 
 
/******************************************************************************************************************/
												//User Guide
												
												
												
												
//step1:      create a new object instance just like this:   var req = new AjaxRequest(); 												
//step2:      invoke a mehtod you want to,there are two method to invoke: 
//              1)which is just like 'AjaxRequest.serializeForm',you should invoke it by AjaxRequest.serializeForm()
//				  in your method (it is like a static method in java)	
//				2)which is just like 'req.onLoadedInternal',you should invoke it by req.onLoadedInternal(),req is 
//                the object instance that you have created
//step3:      set variable value,like invoke method,there is two ways :
//				1)'AjaxRequest.numActiveAjaxRequests' like static variable in java ,
//				  set value like this:AjaxRequest.numActiveAjaxRequests = '0'
//				2)'req.timeout' set value like this: 
//					req.timeout = null(req is the object instance that you have created)




												//Attention
//there are some important variables or methods that you may  use it frequently,please take more attention
/*
*                                                 variable
* 1)  req.url:     The url that the request will be made to, which defaults to the current url of the window
* 2)  req.method:  The method of the request, either GET (default), POST, or HEAD
* 3)  req.responseText:  After a response has been received, this will hold the text contents of 
*                        the response - even in case of error
* 4)  req.responseXML:  After a response has been received, this will hold the XML content
* 5)  req.onComplete:   A function reference assigned will be called when readyState=4
* 6)  req.onSuccess:   A function reference assigned will be called after onComplete, if the statusCode=200
* 7)  req.onError:  A function reference assigned will be called after onComplete, if the statusCode != 200


												 method	

* 1)  AjaxRequest.submit(theform, args):  'theform' is the form of your page,'args' is a array parameter
*										  and there must be any hidden value in you page in order to call the
*                                         onreadystatechange method like this:
*								<input type="hidden" name="onComplete" id="onComplete" value="onComplete"> 

* 2)  req.process:  the method you should call in your own method in order to submit a ruquest	

												//Examples
*submit a request	

getDetailInfo = function(proid,inglide){
	var detailRequest = new AjaxRequest();
	var url = '<html:rewrite page="/projectCaseAction.do"/>?method=toGetDetailMehtod';
	url += "&proid=" + proid;
	url += "&inglide=" + inglide;
	detailRequest.url = url;
	detailRequest.mehod="POST";
	detailRequest.onComplete = setDetailInfo;
	detailRequest.process();
}

*custom method
   
setDetailInfo = function(req){
	var xmlDOM=req.responseXML;
    if(xmlDOM.xml!=null){
    	var rowXML=xmlDOM.documentElement.selectNodes("projectcase");
	    for(var i=0 ;i<rowXML.length;i++){
	       var colNode=rowXML(i);
	       if(projectCaseForm.jnlno != null){
	       		projectCaseForm.jnlno.value = colNode.selectSingleNode("proid").text;
	       }
	    }
    }  
}


*submit request which like submit a form

submitAction = function(){
	AjaxRequest.submit(projectCaseForm,'');
}											
													
*/


/******************************************************************************************************************/ 
 
 
 
function AjaxRequest() {
	var req = new Object();
	
	// -------------------
	// Instance properties
	// -------------------

	/**
	 * Timeout period (in ms) until an async request will be aborted, and
	 * the onTimeout function will be called
	 */
	req.timeout = null;
	
	/**
	 *	Since some browsers cache GET requests via XMLHttpRequest, an
	 * additional parameter called AjaxRequestUniqueId will be added to
	 * the request URI with a unique numeric value appended so that the requested
	 * URL will not be cached.
	 */
	req.generateUniqueUrl = true;
	
	/**
	 * The url that the request will be made to, which defaults to the current 
	 * url of the window
	 */
	req.url = window.location.href;
	
	/**
	 * The method of the request, either GET (default), POST, or HEAD
	 */
	req.method = "GET";
	
	/**
	 * Whether or not the request will be asynchronous. In general, synchronous 
	 * requests should not be used so this should rarely be changed from true
	 */
	req.async = true;
	
	/**
	 * The username used to access the URL
	 */
	req.username = null;
	
	/**
	 * The password used to access the URL
	 */
	req.password = null;
	
	/**
	 * The parameters is an object holding name/value pairs which will be 
	 * added to the url for a GET request or the request content for a POST request
	 */
	req.parameters = new Object();
	
	/**
	 * The sequential index number of this request, updated internally
	 */
	req.requestIndex = AjaxRequest.numAjaxRequests++;
	
	/**
	 * Indicates whether a response has been received yet from the server
	 */
	req.responseReceived = false;
	
	/**
	 * The name of the group that this request belongs to, for activity 
	 * monitoring purposes
	 */
	req.groupName = null;
	
	/**
	 * The query string to be added to the end of a GET request, in proper 
	 * URIEncoded format
	 */
	req.queryString = "";
	
	/**
	 * After a response has been received, this will hold the text contents of 
	 * the response - even in case of error
	 */
	req.responseText = null;
	
	/**
	 * After a response has been received, this will hold the XML content
	 */
	req.responseXML = null;
	
	/**
	 * After a response has been received, this will hold the status code of 
	 * the response as returned by the server.
	 * req.status :
	 * 404 = "file not found"
	 * 200 ="success"
	 */
	req.status = null;
	
	/**
	 * After a response has been received, this will hold the text description 
	 * of the response code
	 */
	req.statusText = null;

	/**
	 * An internal flag to indicate whether the request has been aborted
	 */
	req.aborted = false;
	
	/**
	 * The XMLHttpRequest object used internally
	 */
	req.xmlHttpRequest = null;

	// --------------
	// Event handlers
	// --------------
	
	/**
	 * If a timeout period is set, and it is reached before a response is 
	 * received, a function reference assigned to onTimeout will be called
	 */
	req.onTimeout = null; 
	
	/**
	 * A function reference assigned will be called when readyState=1
	 */
	req.onLoading = null;

	/**
	 * A function reference assigned will be called when readyState=2
	 */
	req.onLoaded = null;

	/**
	 * A function reference assigned will be called when readyState=3
	 */
	req.onInteractive = null;

	/**
	 * A function reference assigned will be called when readyState=4
	 */
	req.onComplete = null;

	/**
	 * A function reference assigned will be called after onComplete, if 
	 * the statusCode=200
	 */
	req.onSuccess = null;

	/**
	 * A function reference assigned will be called after onComplete, if 
	 * the statusCode != 200
	 */
	req.onError = null;
	
	/**
	 * If this request has a group name, this function reference will be called 
	 * and passed the group name if this is the first request in the group to 
	 * become active
	 */
	req.onGroupBegin = null;

	/**
	 * If this request has a group name, and this request is the last request 
	 * in the group to complete, this function reference will be called
	 */
	req.onGroupEnd = null;

	// Get the XMLHttpRequest object itself
	req.xmlHttpRequest = AjaxRequest.getXmlHttpRequest();
	if (req.xmlHttpRequest==null) { return null; }
	
	// -------------------------------------------------------
	// Attach the event handlers for the XMLHttpRequest object
	//xmlHttpRequest.readyState:
	//--------------------------- 0 = not initialized
	//--------------------------- 1 = is reading
	//--------------------------- 2 = have read
	//--------------------------- 3 = is alternating
	//--------------------------- 4 = finished
	// -------------------------------------------------------
	req.xmlHttpRequest.onreadystatechange = 
	function() {
	   
		if (req==null || req.xmlHttpRequest==null) { return; }
		if (req.xmlHttpRequest.readyState==1) {
		    var obj=document.getElementById("toolTipLayer");
		    if(obj==null){
			    var toolTipLayer = document.createElement("DIV");
				toolTipLayer.id = "toolTipLayer";
				toolTipLayer.style.position = "absolute";
				toolTipLayer.style.top = "10px";
				toolTipLayer.style.right="10px"; 
				toolTipLayer.style.width="90px"
				toolTipLayer.style.height="18px";
				toolTipLayer.style.background="red";
				toolTipLayer.style.fontSize="13px";
				toolTipLayer.style.zIndex = "100";
				toolTipLayer.style.padding="3px 3px 3px 6px";
				toolTipLayer.innerHTML="<font color=\"#FFFFFF\">正在加载....</font>";
				toolTipLayer.style.display = ""; //这里也可以使用 visibility
				document.body.appendChild(toolTipLayer); 
			}else{
				obj.style.display = ""; //这里也可以使用 visibility
				document.body.appendChild(obj); 
			}
			req.onLoadingInternal(req); 
        }
         
		if (req.xmlHttpRequest.readyState==2) {  
			req.onLoadedInternal(req); 
		}
		if (req.xmlHttpRequest.readyState==3) {  
			req.onInteractiveInternal(req); 
		}
		if (req.xmlHttpRequest.readyState==4) {  
			req.onCompleteInternal(req); 
			var divobj=document.getElementById("toolTipLayer");
			var reqstatus=req.status;
			if(reqstatus==200){
			   divobj.innerHTML="<font color=\"#FFFFFF\">加载完成....</font>";
			   //隔时间关闭提示内容
			  colse();
			}else{
			   //当操作失败的时候，不进行提示内容的关闭
			   divobj.innerHTML="<font color=\"#FFFFFF\">加载失败.</font>";
			   document.body.appendChild(divobj);	
			   setTimeout("colse()",3000);	
			}
		}
	};
	
	
	// ---------------------------------------------------------------------------
	// Internal event handlers that fire, and in turn fire the user event handlers
	// ---------------------------------------------------------------------------
	// Flags to keep track if each event has been handled, in case of 
	// multiple calls (some browsers may call the onreadystatechange 
	// multiple times for the same state)
	req.onLoadingInternalHandled = false;
	req.onLoadedInternalHandled = false;
	req.onInteractiveInternalHandled = false;
	req.onCompleteInternalHandled = false;
	req.onLoadingInternal = 
		function() {
			if (req.onLoadingInternalHandled) { return; }
			AjaxRequest.numActiveAjaxRequests++;
			if (AjaxRequest.numActiveAjaxRequests==1 && typeof(window['AjaxRequestBegin'])=="function") {
				AjaxRequestBegin();
			}
			if (req.groupName!=null) {
				if (typeof(AjaxRequest.numActiveAjaxGroupRequests[req.groupName])=="undefined") {
					AjaxRequest.numActiveAjaxGroupRequests[req.groupName] = 0;
				}
				AjaxRequest.numActiveAjaxGroupRequests[req.groupName]++;
				if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==1 && typeof(req.onGroupBegin)=="function") {
					req.onGroupBegin(req.groupName);
				}
			}
			if (typeof(req.onLoading)=="function") {
				req.onLoading(req);
			}
			req.onLoadingInternalHandled = true;
		};
	req.onLoadedInternal = 
		function() {
			if (req.onLoadedInternalHandled) { return; }
			if (typeof(req.onLoaded)=="function") {
				req.onLoaded(req);
			}
			req.onLoadedInternalHandled = true;
		};
	req.onInteractiveInternal = 
		function() {
			if (req.onInteractiveInternalHandled) { return; }
			if (typeof(req.onInteractive)=="function") {
				req.onInteractive(req);
			}
			req.onInteractiveInternalHandled = true;
		};
	req.onCompleteInternal = 
		function() {
			if (req.onCompleteInternalHandled || req.aborted) { return; }
			req.onCompleteInternalHandled = true;
			AjaxRequest.numActiveAjaxRequests--;
			if (AjaxRequest.numActiveAjaxRequests==0 && typeof(window['AjaxRequestEnd'])=="function") {
				AjaxRequestEnd(req.groupName);
			}
			if (req.groupName!=null) {
				AjaxRequest.numActiveAjaxGroupRequests[req.groupName]--;
				if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==0 && typeof(req.onGroupEnd)=="function") {
					req.onGroupEnd(req.groupName);
				}
			}
			req.responseReceived = true;
			req.status = req.xmlHttpRequest.status;
			req.statusText = req.xmlHttpRequest.statusText;
			req.responseText = req.xmlHttpRequest.responseText;
			req.responseXML = req.xmlHttpRequest.responseXML;
			if (typeof(req.onComplete)=="function") {
				req.onComplete(req);
			}
			if (req.xmlHttpRequest.status==200 && typeof(req.onSuccess)=="function") {
				req.onSuccess(req);
			}
			else if (typeof(req.onError)=="function") {
				req.onError(req);
			}

			// Clean up so IE doesn't leak memory
			delete req.xmlHttpRequest['onreadystatechange'];
			req.xmlHttpRequest = null;
		};
	req.onTimeoutInternal = 
		function() {
			if (req!=null && req.xmlHttpRequest!=null && !req.onCompleteInternalHandled) {
				req.aborted = true;
				req.xmlHttpRequest.abort();
				AjaxRequest.numActiveAjaxRequests--;
				if (AjaxRequest.numActiveAjaxRequests==0 && typeof(window['AjaxRequestEnd'])=="function") {
					AjaxRequestEnd(req.groupName);
				}
				if (req.groupName!=null) {
					AjaxRequest.numActiveAjaxGroupRequests[req.groupName]--;
					if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==0 && typeof(req.onGroupEnd)=="function") {
						req.onGroupEnd(req.groupName);
					}
				}
				if (typeof(req.onTimeout)=="function") {
					req.onTimeout(req);
				}
			// Opera won't fire onreadystatechange after abort, but other browsers do. 
			// So we can't rely on the onreadystate function getting called. Clean up here!
			delete req.xmlHttpRequest['onreadystatechange'];
			req.xmlHttpRequest = null;
			}
		};

	// ----------------
	// Instance methods
	// ----------------
	/**
	 * The process method is called to actually make the request. It builds the
	 * querystring for GET requests (the content for POST requests), sets the
	 * appropriate headers if necessary, and calls the 
	 * XMLHttpRequest.send() method
	*/
	req.process = 
		function() {
			if (req.xmlHttpRequest!=null) {
				// Some logic to get the real request URL
				if (req.generateUniqueUrl && req.method=="GET") {
					req.parameters["AjaxRequestUniqueId"] = new Date().getTime() + "" + req.requestIndex;
				}
				var content = null; // For POST requests, to hold query string
				for (var i in req.parameters) {
					if (req.queryString.length>0) { req.queryString += "&"; }
					req.queryString += encodeURIComponent(i) + "=" + encodeURIComponent(req.parameters[i]);
				}
				if (req.method=="GET") {
					if (req.queryString.length>0) {
						req.url += ((req.url.indexOf("?")>-1)?"&":"?") + req.queryString;
					}
				}
				req.xmlHttpRequest.open(req.method,req.url,req.async,req.username,req.password);
				if (req.method=="POST") {
					if (typeof(req.xmlHttpRequest.setRequestHeader)!="undefined") {
						req.xmlHttpRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded;charset=utf-8');
					}
					content = req.queryString;
				}
				if (req.timeout>0) {
					setTimeout(req.onTimeoutInternal,req.timeout);
				}
				req.xmlHttpRequest.send(content);
			}
		};

	/**
	 * An internal function to handle an Object argument, which may contain
	 * either AjaxRequest field values or parameter name/values
	 */
	req.handleArguments = 
		function(args) {
			for (var i in args) {
				// If the AjaxRequest object doesn't have a property which was passed, treat it as a url parameter
				if (typeof(req[i])=="undefined") {
					req.parameters[i] = args[i];
				}
				else {
					req[i] = args[i];
				}
			}
		};

	/**
	 * Returns the results of XMLHttpRequest.getAllResponseHeaders().
	 * Only available after a response has been returned
	 */
	req.getAllResponseHeaders =
		function() {
			if (req.xmlHttpRequest!=null) {
				if (req.responseReceived) {
					return req.xmlHttpRequest.getAllResponseHeaders();
				}
				alert("Cannot getAllResponseHeaders because a response has not yet been received");
			}
		};

	/**
	 * Returns the the value of a response header as returned by 
	 * XMLHttpRequest,getResponseHeader().
	 * Only available after a response has been returned
	 */
	req.getResponseHeader =
		function(headerName) {
			if (req.xmlHttpRequest!=null) {
				if (req.responseReceived) {
					return req.xmlHttpRequest.getResponseHeader(headerName);
				}
				alert("Cannot getResponseHeader because a response has not yet been received");
			}
		};

	return req;
}
 
// ---------------------------------------
// Static methods of the AjaxRequest class
// ---------------------------------------

/**
 * Returns an XMLHttpRequest object, either as a core object or an ActiveX 
 * implementation. If an object cannot be instantiated, it will return null;
 */
AjaxRequest.getXmlHttpRequest = function() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		// Based on http://jibbering.com/2002/4/httprequest.html
		/*@cc_on @*/
		/*@if (@_jscript_version >= 5)
		try {
			return new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch (E) {
				return null;
			}
		}
		@end @*/
	}
	else {
		return null;
	}
};

/**
 * See if any request is active in the background
 */
AjaxRequest.isActive = function() {
	return (AjaxRequest.numActiveAjaxRequests>0);
};

/**
 * Make a GET request. Pass an object containing parameters and arguments as 
 * the second argument.
 * These areguments may be either AjaxRequest properties to set on the request 
 * object or name/values to set in the request querystring.
 */
AjaxRequest.get = function(args) {
	AjaxRequest.doRequest("GET",args);
};

/**
 * Make a POST request. Pass an object containing parameters and arguments as 
 * the second argument.
 * These areguments may be either AjaxRequest properties to set on the request 
 * object or name/values to set in the request querystring.
 */
AjaxRequest.post = function(args) {
	AjaxRequest.doRequest("POST",args);
};

/**
 * The internal method used by the .get() and .post() methods
 */
AjaxRequest.doRequest = function(method,args) {
	if (typeof(args)!="undefined" && args!=null) {
		var myRequest = new AjaxRequest();
		myRequest.method = method;
		myRequest.handleArguments(args);
		myRequest.process();
	}
};

/**
 * Submit a form. The requested URL will be the form's ACTION, and the request 
 * method will be the form's METHOD.
 * Returns true if the submittal was handled successfully, else false so it 
 * can easily be used with an onSubmit event for a form, and fallback to 
 * submitting the form normally.
 */
AjaxRequest.submit = function(theform, args) {
	var myRequest = new AjaxRequest();
	if (myRequest==null) { return false; }
	var serializedForm = AjaxRequest.serializeForm(theform);
	// 
	myRequest.method = theform.method.toUpperCase();
	// 
	myRequest.url = theform.action;
	//  
	myRequest.handleArguments(args);
	myRequest.queryString = serializedForm;
	//complete
	if(theform.onComplete != null){
		myRequest.onComplete = eval(theform.onComplete.value);
	}
	//success
	if(theform.onSuccess != null){
		myRequest.onSuccess = eval(theform.onSuccess.value);
	}
	//error
	if(theform.onError != null){
		myRequest.onError = eval(theform.onError.value);
	}
	myRequest.process();
	return true;
};

/**
 * Serialize a form into a format which can be sent as a GET string or a POST 
 * content.It correctly ignores disabled fields, maintains order of the fields 
 * as in the elements[] array. The 'file' input type is not supported, as 
 * its content is not available to javascript. This method is used internally
 * by the submit class method.
 */
AjaxRequest.serializeForm = function(theform) {
	var els = theform.elements;
	var len = els.length;
	var queryString = "";
	this.addField = 
		function(name,value) { 
			if (queryString.length>0) { 
				queryString += "&";
			}
			queryString += encodeURIComponent(name) + "=" + encodeURIComponent(value);
		};
	for (var i=0; i<len; i++) {
		var el = els[i];
		if (!el.disabled) {
			switch(el.type) {
				case 'text': case 'password': case 'hidden': case 'textarea': 
					this.addField(el.name,el.value);
					break;
				case 'select-one':
					if (el.selectedIndex>=0) {
						this.addField(el.name,el.options[el.selectedIndex].value);
					}
					break;
				case 'select-multiple':
					for (var j=0; j<el.options.length; j++) {
						if (el.options[j].selected) {
							this.addField(el.name,el.options[j].value);
						}
					}
					break;
				case 'checkbox': case 'radio':
					if (el.checked) {
						this.addField(el.name,el.value);
					}
					break;
			}
		}
	}
	return queryString;
};

/**
*custom method which is used for customer call
*@param           theform                    the form which you want to submit
*@param           args                       which you want to add to the url as a parameter
*@param           url                        the url you want to submit by ajax
*@param           method                     the method you submit by ajax
*@param           oncomplete                 A function reference assigned will be called when readyState=4 
*@param           onsuccess                  A function reference assigned will be called after onComplete, if 
* 											 the statusCode=200
*@param           onerror                    A function reference assigned will be called after onComplete, if 
* 											 the statusCode != 200
*@param           flag                       if you want add the control of the form to url,the set 'Y'
*/
AjaxRequest.customerInvokeMethod = function(theform,args,url,method,oncomplete,onsuccess,onerror,flag){
	var myRequest = new AjaxRequest();
	if(myRequest != null){
		if(method != null && method != ''){
			myRequest.method=method.toUpperCase();
		}else{
			myRequest.method=theform.method.toUpperCase();
		}
		if(url != null && url != ''){
			myRequest.url = url;
		}else{
			myRequest.url = theform.action;
		}
		if(oncomplete != null && oncomplete != ''){
			myRequest.onComplete = oncomplete;
		}
		if(onsuccess != null && onsuccess != ''){
			myRequest.onSuccess = eval(onsuccess);
		}
		if(onerror != null && onerror != ''){
			myRequest.onError = onerror;
		}
		if(flag != null && flag == 'Y'){
			var serializedForm = AjaxRequest.serializeForm(theform);
			myRequest.handleArguments(args);
			myRequest.queryString = serializedForm;
		}
		 
		myRequest.process();
	}
};

// -----------------------
// Static Class variables
// -----------------------

/**
 * The number of total AjaxRequest objects currently active and running
 */
AjaxRequest.numActiveAjaxRequests = 0;

/**
 * An object holding the number of active requests for each group
 */
AjaxRequest.numActiveAjaxGroupRequests = new Object();

/**
 * The total number of AjaxRequest objects instantiated
 */
AjaxRequest.numAjaxRequests = 0;

//用户关闭提示进度。
function colse(){
   var divobj=document.getElementById("toolTipLayer");
   divobj.style.display="none";
   document.body.appendChild(divobj);
}