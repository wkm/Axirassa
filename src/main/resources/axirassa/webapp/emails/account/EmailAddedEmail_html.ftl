[#ftl]
[#import "/components.ftl" as lib]
[#compress]
	[@lib.BaseHtmlEmail]
		<h2>E-mail Added</h2>
		<p>
			Hi, we've added this e-mail to your Axirassa account.
		</p>
		<p>
			Please click the link below to verify this e-mail address:
		</p>
		
		[@lib.HtmlEmailLink link="${axlink}" /]
	[/@lib.BaseHtmlEmail]
[/#compress]