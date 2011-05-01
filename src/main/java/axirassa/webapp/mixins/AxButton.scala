
package axirassa.webapp.mixins

import axirassa.data.AxButtonStyle
import scala.reflect.BeanProperty

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.MarkupWriter
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.MixinAfter
import org.apache.tapestry5.annotations.Parameter

import axirassa.webapp.data.AxButtonStyle

@Import(stylesheet = Array("context:/css/axbutton.css"))
@MixinAfter
class AxButton {

  @BeanProperty
  var tight : Boolean = _

  @Parameter(value = "default", defaultPrefix = BindingConstants.LITERAL)
  var styling : AxButtonStyle = _

  def beginRender(writer : MarkupWriter) {
    val sb = new StringBuilder("button")

    if (tight) {
      System.out.println("TIGHT")
      sb.append(" axbtight")
    }

    styling match {
      case AxButtonStyle.Default =>
      case AxButtonStyle.Dark => sb.append(" axbdark")
    }

    writer.element("div", "class", sb.toString())
    writer.element("div", "class", "innerbutton")
  }

  def afterRender(writer : MarkupWriter) {
    writer.end() // innerbutton
    writer.end() // button
  }
}
