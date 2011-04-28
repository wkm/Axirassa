
package axirassa.webapp.components

import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.corelib.components.PasswordField

class AxPasswordField extends AxAbstractTextInput {
    @Component(publishParameters = "annotationProvider,clientId,disabled,nulls,translate,validate,value")
    var txtfield : PasswordField = _

    override def getControlName = txtfield.getControlName

    override def isDisabled = txtfield.isDisabled

    override def isRequired = txtfield.isRequired

    override def getClientId = txtfield.getClientId
}
