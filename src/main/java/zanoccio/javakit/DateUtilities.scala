
package zanoccio.javakit;

import java.util.Date;
import java.util.TimeZone;

object DateUtilities {
  def gmtTime = toGmtTime(new Date())

  /**
   * Based off of: http://snippets.dzone.com/posts/show/5288
   */
  def toGmtTime(date : Date) = {
    val tz = TimeZone.getDefault();
    var converted = new Date(date.getTime() - tz.getRawOffset());

    if (tz.inDaylightTime(converted)) {
      val dstDate = new Date(converted.getTime() - tz.getDSTSavings());

      // check to make sure we have not crossed back into standard time
      // this happens when we are on the cusp of DST (7pm the day before
      // the change for PDT)
      if (tz.inDaylightTime(dstDate))
        converted = dstDate;
    }

    converted;
  }
}
