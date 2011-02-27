[#ftl]
[#import "/components.ftl" as lib]
[#compress]
	[@lib.BaseHtmlEmail]
		<h2>Did you request your password reset?</h2>
		<p> 
			If so, please click this link to choose a new password:
		</p>
		
		[@lib.HtmlEmailLink link="${axlink}" /]
		
		<p>
			If you didn't request your password reset, please disregard this e-mail
			and your password will remain unchanged.
		</p>
	[/@lib.BaseHtmlEmail]
[/#compress]