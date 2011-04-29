
package axirassa.services.phone;

class SendVoice(number : String, extension : String, message : String) 
	extends TropoSender(number, message) {
	
	override
	def getToken = "0d10e8fb91e4c747bcd5f41d2e8e164f81ffec5643a61f2c6119580304223b7bb30318d19a22816f92a649d8"
}
