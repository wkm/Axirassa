function errorHandler(message, ex) {
	dwr.util.setValue("error", "<font color='red'>Cannot connect to server. Initializing retry logic.</font>", {escapeHtml:false});
	setTimeout(function() { dwr.util.setValue("error", ""); }, 5000);
};
	  
function updatePollStatus(pollStatus) {
	dwr.util.setValue("pollStatus", pollStatus ? "<font color='green'>Online</font>" : "<font color='red'>Offline</font>", {escapeHtml:false});
};

window.onload=function()
{
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setErrorHandler(errorHandler);    
};