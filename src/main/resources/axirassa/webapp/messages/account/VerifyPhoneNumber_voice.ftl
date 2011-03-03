[#ftl]
[#import "/components.ftl" as lib]
[@lib.BaseVoiceNotification]
	Please enter the following confirmation code to verify this phone number.
	
	[@lib.VoiceEmphasis code /]
	
	Again,
	
	[@lib.VoiceEmphasis code /]
[/@lib.BaseVoiceNotification]