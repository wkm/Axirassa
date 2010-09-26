/**
 * Axirassa Components JavaScript Library
 * Copyright 2010 - Zanoccio, LLC. All Rights Reserved.
 */

var ax = new function() {
	
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
	};
	
	function percentTicks(n) {
		return n+"%";
	};
	
	this.opener_toggle = function(id) {
		var obj = $($(id+"_body"));
		if(obj.visible() == false)
			obj.show();
		else
			obj.hide();
	};
	
	this.agentcontrol = function(id, source) {
		// mark that we're downloading data
		$(id).addClassName('axp_loading');
		$(id).update('Downloading data...');
		
		new Ajax.Request(source, {
			method: 'get',
			onSuccess: function(transport) {
				$(id).update('Processing...');
				
				var data = transport.responseText.evalJSON();
				var length = data['length'];
				if (length > 0) {
					$(id).update('Generating charts...');
					
					// create sub graph boxes
					var detailsnode = $(id + "_details_body");
					var index = 0;
					
					data['labels'].map(function(label){
						$(detailsnode).insert(new Element('div', {
							'class': 'chart axp',
							id: id + "_chart_" + index
						}));
						index++;
					});
					
					var times = data['times'];
					var rawdata = data['data'];
					var chardata;
					
					var visible = $(detailsnode).visible();
					$(detailsnode).show();
					
					// refactor data, create charts
					for (var i = 0; i < length; i++) {
						// thread over dates
						var datasz = times.length;
						chartdata = new Array(datasz);
						
						for (var j = 0; j < datasz; j++) {
							chartdata[j] = [times[j], rawdata[i][j][1]];
						}
						
						Flotr.draw($(id + "_chart_" + i), [{
							data: chartdata,
							color: "#D44917"
						}], {
							shadowSize: 0,
							lines: {
								show: true,
								fill: true
							},
							xaxis: {
								tickFormatter: function(n){
									return formattime(new Date(n / 1));
								}
							},
							
							yaxis: {
								min: 0,
								max: 100,
								tickFormatter: function(n){
									return percentTicks(n);
								}
							}
						});
					}
					
					if(!visible)
						$(detailsnode).hide();
					
					$(id).update('Aggregating data...');
					chartdata = new Array(datasz);
					for(var i = 0; i < datasz; i++) {
						var total = 0;
						for (var j = 0; j < length; j++) {
							total += rawdata[j][i][0];
						}
						chartdata[i] = [times[i], total];
					}
					
					$(id).update('');
					$(id).removeClassName('axp_loading');
					
					Flotr.draw($(id), [{
						data: chartdata,
						color: "#D44917"
					}],
					{
						shadowSize: 0,
						lines: {
							show: true,
							fill: true
						},
						xaxis: {
							tickFormatter: function(n){
								return formattime(new Date(n / 1));
							}
						},
						yaxis: {
							min: 0,
							max: length * 100,
							tickFormatter: function(n){
								return percentTicks(n);
							}
						}
					})
				}
				
			
//				$(id).update('');
//				$(id).removeClassName('axp_loading');
			}
		});
	};
};
