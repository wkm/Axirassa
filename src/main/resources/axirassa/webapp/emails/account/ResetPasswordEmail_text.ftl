[#ftl]
[#import "/components.ftl" as lib]

[@lib.BaseTextEmail]
Did you request your password reset?
	
If so, please click this link to choose a new password:
	
${axlink}
	
If you didn't request your password reset, please disregard this e-mail and your password will remain unchanged.

Thank you.
[/@lib.BaseTextEmail]