[#ftl]
[#import "/components.ftl" as lib]

[@lib.BaseTextEmail]
	Feedback:
	
	[#list feedback as item]
		${item.formattedDate} - ${item.comment}
		[#if item.user??]
			{ ${item.user.email} }
		[/#if]
		__________________________________________________
		${item.useragent}
		
		
		
	[/#list]
[/@lib.BaseTextEmail]