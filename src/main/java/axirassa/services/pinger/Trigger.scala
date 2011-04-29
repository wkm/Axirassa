package axirassa.services.pinger

import org.apache.http.NoHttpResponseException
import java.net.ConnectException
import java.net.SocketException
import axirassa.model.PingerEntity

trait Trigger {
    var pinger : PingerEntity = _

    override def toString = getClass.getSimpleName
}

case class ConnectionTimeOutTrigger(cause : SocketException)
    extends Trigger

case class CouldNotConnectTrigger(cause : ConnectException)
    extends Trigger

case class NoResponseTrigger(cause : NoHttpResponseException)
    extends Trigger

case class PingerErrorTrigger(cause : Throwable)
    extends Trigger

case class ProtocolErrorTrigger(cause : Throwable)
    extends Trigger

case class StatusCodeTrigger(code : Int)
    extends Trigger

case class TimeoutTrigger()
    extends Trigger

case class UnknownHostTrigger()
    extends Trigger