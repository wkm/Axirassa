[#ftl]
[#import "/components.ftl" as lib]
[#compress]
	[@lib.BaseHtmlEmail]
		<h2>Password Changed</h2>
		<p>
			Hi, this is a notification that your password
			was changed on Axirassa.
		</p>
	[/@lib.BaseHtmlEmail]
[/#compress]