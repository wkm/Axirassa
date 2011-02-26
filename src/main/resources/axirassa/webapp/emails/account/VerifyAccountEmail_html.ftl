[#ftl]
[#import "/components.ftl" as lib]
[#compress]
	[@lib.BaseHtmlEmail]
		<h2>Welcome to Axirassa</h2>
		<p> 
			Please verify this e-mail address by clicking the link below:
		</p>
		
		[@lib.HtmlEmailLink link="${axlink}" /]
		
		<p>
			If you did not sign up for Axirassa please ignore this message.
		</p>
	[/@lib.BaseHtmlEmail]
[/#compress]