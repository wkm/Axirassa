
package axirassa.webapp.mixins

import org.apache.tapestry5.ClientElement
import org.apache.tapestry5.ComponentResources
import org.apache.tapestry5.annotations.Environmental
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.InjectContainer
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.javascript.JavaScriptSupport
import org.hornetq.utils.json.JSONException
import org.hornetq.utils.json.JSONObject

/**
 * Based on: http://jumpstart.doublenegative.com.au/jumpstart/examples/input/
 * ajaxvalidators1
 *
 * @author wiktor
 */
@Import(library = Array("context:/js/ax_ajaxvalidator.js"))
class AjaxValidator {
  @Inject
  var resources : ComponentResources = _

  @Environmental
  var jssupport : JavaScriptSupport = _

  @InjectContainer
  var element : ClientElement = _

  def afterRender() {
    val listenerURI = resources.createEventLink("ajaxValidate").toAbsoluteURI()
    val elementId = element.getClientId()

    val spec = new JSONObject()
    spec.put("uri", listenerURI)
    spec.put("elementId", elementId)

    jssupport.addScript("new AjaxValidator(%s)", spec.toString())
  }
}
