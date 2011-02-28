[#ftl]
[#macro BaseHtmlEmail]
	<html>
		<body>
			<div style="font-family: 'lucida grande', verdana, sans-serif; width: 45em; margin: 20px 0 0 20px; border: 1px solid #ddd;">
				<h1 style="color:#fff;background:rgb(174,3,0);margin:0; padding-left: 100px; border-bottom: 1px solid #700;">axirassa</h1>
				<div style="padding-left:100px;min-height:25em;">
					[#nested]
				</div>
				<div style="padding: 5px 0 5px 100px; background: #fafafa; border-top: 1px solid #eee; font-size: 9pt; color: #333;">
					Sent to ${recipient}<br> 
					by <a href="http://axirassa.com">Axirassa, LLC.</a><br>
				</div>
			</div>
		</body>
	</html>
[/#macro]

[#macro BaseTextEmail]
[#nested]
_________________________________________________
Sent to ${recipient}
by Axirassa, LLC. 
[/#macro]

[#macro HtmlEmailLink link]
	<p style="font-family: monospace; background: #eee; border: 1px solid #ccc; width: 40em;">
		<a href="${link}">${link}</a>
	</p>
[/#macro]

[#macro EmailAddress email]
	<tt><a href="mailto:${email}">${email}</a></tt>
[/#macro]