
package axirassa.services;

trait Service {
    def execute
}

class AxirassaServiceException(msg : String, cause : Exception = null)
    extends Exception(msg, cause)

class InvalidMessageClassException(expectedClass : Class[Any], receivedObject : AnyRef)
    extends AxirassaServiceException("Expected message of class "+expectedClass+" received object of type "+receivedObject.getClass)