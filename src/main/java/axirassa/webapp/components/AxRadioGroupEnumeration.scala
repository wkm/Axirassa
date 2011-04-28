
package axirassa.webapp.components

trait AxRadioGroupEnumeration {
    /**
     * @return the textual label to use for a radio group item.
     */
    def toLabel : String

    /**
     * @return whether this
     */
    def enabled : Boolean
}
