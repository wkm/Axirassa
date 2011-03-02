[#ftl]
[#import "/components.ftl" as lib]

[#compress]
	[@lib.BaseHtmlEmail]
		<h2>Feedback</h2>
		[#list feedback as item]
			<p>
				<span style="color: #333;">${item.formattedDate}</span> 
				&mdash; <b>${item.comment}</b>
				[#if item.user??]
					<i>~ ${item.user.email}</i>
				[/#if]
				<br/>
				<span style="font-size: 80%; color: #666;">${item.useragent}</span>
			</p>
		[/#list]	
	[/@lib.BaseHtmlEmail]
[/#compress]