[#ftl]
[#import "/components.ftl" as lib]
[#compress]
	[@lib.BaseHtmlEmail]
		<h2>Primary E-mail Address Changed</h2>
		<p>
			Hi, this is a notification that your primary e-mail
			address has been changed from [@lib.EmailAddress "${oldPrimary}" /]
			to [@lib.EmailAddress "${newPrimary}" /].
		</p>
	[/@lib.BaseHtmlEmail]
[/#compress]