
//
// Date Formatting
//
function formattime(date) {
  var minute = date.getMinutes();
  var hour = date.getHours();
  
  var ampm;
  if(hour == 0) {
    ampm = "am";
    hour = 12;
  } else if(hour >= 12) {
    ampm = "pm";
    hour -= 12;
  } else {
    ampm = "am";
  }
  
  if(hour < 10) {
    hour = "0"+hour;
  }
  if(minute < 10) {
    minute = "0"+minute;
  }
  
  return hour+":"+minute+ampm;
}

//
// Chart Plotting
//
function plotchart(id, url) {
	new Ajax.Request(url, {
		method: 'get',
		onSuccess: function(transport) {
			var dat = transport.responseText.evalJSON();
		
			Flotr.draw(
					$(id),
					[
					 {data: dat, color: '#ff9900'}
					],
					{
						grid: {color: '#666', backgroundColor: '#fff'},
						lines: {show:true, fill:true},
						
						shadowSize: 2,
						
						xaxis: {
							tickFormatter: function(n) {
							return formattime(new Date(n/1));
						}
						},
						yaxis: {
							min: 0, 
							max: 800,
							tickFormatter: function(n) {
								return n + "%";
							}
						}
					}
			);
		}
	}
	);

}