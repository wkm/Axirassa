
package axirassa.webapp.components

import java.util.List

import org.apache.tapestry5.ValueEncoder
import org.apache.tapestry5.annotations.Parameter
import org.apache.tapestry5.annotations.Property

import axirassa.webapp.utilities.LabeledObject

class AxRadioGroup {
    @Parameter(required = false)
    var label : String = _

    def getLabel = {
        if (label == null)
            ""
        else
            label
    }

    @Parameter(required = true, principal = true, autoconnect = true)
    @Property
    var value : LabeledObject = _

    @Parameter(required = true)
    @Property
    var values : List[LabeledObject] = _

    @Parameter(required = true, allowNull = false)
    @Property
    var encoder : ValueEncoder[_ <: Any] = _
} 
