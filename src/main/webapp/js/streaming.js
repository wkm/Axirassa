window.onload = function() {
	var xmlhttp = new XMLHttpRequest;
	var lastindex = 0;
	
	xmlhttp.open("GET", "http://localhost:8080/stream/", true);
	xmlhttp.onreadystatechange = function() {
		console.log("stage changed ---> ", xmlhttp.readyState);
		var newindex = xmlhttp.responseText.length;
		var text = xmlhttp.responseText.substr(lastindex, newindex);
		console.log("text: ", text);
		console.log("total bytes: ", newindex);
		lastindex = newindex;
		
		alert(text);
	}
	xmlhttp.send(null);
};
