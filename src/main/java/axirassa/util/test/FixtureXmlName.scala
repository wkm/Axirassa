
package axirassa.util.test;

object FixtureXmlName {
  val FIXTURES = new FixtureXmlName("fixtures")
  val FIXTURE = new FixtureXmlName("fixture")
  val NAME = new FixtureXmlName("name")
}
class FixtureXmlName(name : String) {
  override
  def toString = name
}
