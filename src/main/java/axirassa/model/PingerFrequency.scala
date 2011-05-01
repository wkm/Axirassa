package axirassa.model

class PingerFrequency(interval : Int) {
  def getInterval = interval

  def getLabel {
    interval match {
      case 1            => "second"
      case e if e < 60  => interval+"seconds"
      case e if e == 60 => "minute"
      case e            => (interval / 60)+" minutes"
    }
  }
}

object PingerFrequency {
  val MINUTE = new PingerFrequency(5)
  val MINUTES_5 = new PingerFrequency(5 * 60)
  val MINUTES_10 = new PingerFrequency(10 * 60)
  val MINUTES_15 = new PingerFrequency(15 * 60)
  val MINUTES_30 = new PingerFrequency(30 * 60)
  val MINUTES_60 = new PingerFrequency(60 * 60)
}