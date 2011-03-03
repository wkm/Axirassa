[#ftl]
[#macro BaseVoiceNotification]
[#compress]
	<speak>
		This is Ax-ih-rassa Server Monitoring.
		
		[#nested]
	</speak>
[/#compress]
[/#macro]

[#macro BaseSmsNotification]
[#compress]Axirassa Server Monitoring -- [#nested][/#compress]
[/#macro]

[#macro VoiceEmphasis text]
	<emphasis level="strong"><prosody rate="slow">${text}</prosody></emphasis>
[/#macro]