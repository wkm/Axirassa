
package axirassa.webapp.components

import org.apache.tapestry5.BindingConstants
import org.apache.tapestry5.Field
import org.apache.tapestry5.FormValidationControl
import org.apache.tapestry5.MarkupWriter
import org.apache.tapestry5.ValidationDecorator
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Import
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.corelib.components.Form
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.Environment

import axirassa.webapp.services.validation.CustomValidationDecorator

@Import(library = Array("context:js/error.js"))
class AxForm extends FormValidationControl {

    @Inject
    var environment : Environment = _

    @Component(publishParameters = "autofocus")
    var form : Form = _

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    var title : String = _
    def getTitle = title

    @Parameter
    var hasTitle : Boolean

    def getHasTitle = {
        if (title != null)
            true
        else
            false
    }

    def beginRender(writer : MarkupWriter) {
        environment.push(classOf[ValidationDecorator], new CustomValidationDecorator(environment, writer))
    }

    def afterRender(writer : MarkupWriter) {
        environment.pop(classOf[ValidationDecorator])
    }

    override def recordError(errorMessage : String) {
        form.recordError(errorMessage)
    }

    override def recordError(field : Field, errorMessage : String) {
        form.recordError(field, errorMessage)
    }

    override def getHasErrors = form.getHasErrors

    override def isValid = form.isValid

    override def clearErrors() {
        form.clearErrors()
    }

}
