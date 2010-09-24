
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

function percentTicks(n, max) {
	return n+"%";
}

var gib = Math.pow(2, 30);
var mib = Math.pow(2, 20);
var kib = Math.pow(2, 10);

function dataTicks(n, max) {
	var base;
	var unit;
	if(max > gib) {
		base = (n / gib);
		unit = "GiB";
	} else if(max > mib) {
		base = (n / mib);
		unit = "MiB";
	} else {
		base = (n / kib);
		unit = "KiB";
	}
	
	return base.toFixed(2) + unit;
}

//
// Chart Plotting
//
function plotchart(id, url, mode, color) {
	new Ajax.Request(url, {
		method: 'get',
		onSuccess: function(transport) {
			var dat = transport.responseText.evalJSON();
			
			var max;
			var tickfn;
			switch(mode) {
			case 'percent':
				max = 800;
				tickfn = percentTicks;
				break;
				
			case 'data':
				max = dat[0];
				dat = dat[1];
				tickfn = dataTicks;
				break;
			}
			
		
			Flotr.draw(
					$(id),
					[
					 {data: dat, color: color}
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
							max: max,
							tickFormatter: function(n) {
								return tickfn(n, max);
							}
						}
					}
			);
		}
	}
	);

}