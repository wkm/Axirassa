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
	dwr.engine.setNotifyServerOnPageUnload(true);
	dwr.engine.setErrorHandler(errorHandler);
	
	addDataPoint(50);
	addDataPoint(60);
};

var x = 20;
function addDataPoint(date, responseTime, responseSize) {
	x = x + 10;	
	axplot.addDataPoint(x, responseTime);
	$("pollStatus").update("Last update: "+date);
}