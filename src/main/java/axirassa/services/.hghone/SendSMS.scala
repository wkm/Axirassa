
package axirassa.services.phone;

class SendSMS(number : String, message : String)
    extends TropoSender(number, message) {

    override def getToken = "acba13447a0d0046a2691c590ab46ec2c6a672b151cfa1d42d6de32c26411c6ef9357cc70c5bb3348e7e0362"
}
