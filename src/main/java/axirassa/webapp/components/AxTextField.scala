
package axirassa.webapp.components

import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.corelib.components.TextField

class AxTextField extends AxAbstractTextInput {
  @Component(
    publishParameters = "annotationProvider,clientId,disabled,nulls,translate,value",
    parameters = Array("validate=inherit:validate"))
  var txtfield : TextField = _

  @Parameter(defaultPrefix = "validate")
  @Property
  var validate : String = _

  def getTextField = txtfield

  override def getControlName = txtfield.getControlName

  override def isDisabled = txtfield.isDisabled

  override def isRequired = txtfield.isRequired

  override def getClientId = txtfield.getClientId

}
