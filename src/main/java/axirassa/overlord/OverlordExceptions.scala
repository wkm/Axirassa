
package axirassa.overlord

import org.w3c.dom.Document

abstract class OverlordException(msg : String, cause : Exception = null) extends Exception(msg, cause)

abstract class OverlordError(cause : Throwable) extends Error(cause)

//
// ERRORS
//
class ExceptionInMonitorError(e : Exception) extends OverlordError(e)

//
// EXCEPTIONS
//
class DuplicateGroupException(group : ExecutionGroup, doc : Document)
    extends OverlordException("A group named "+group.getCanonicalName+" is already defined in "+doc.getBaseURI)

class DuplicateTargetException(target : ExecutionTarget, doc : Document)
    extends OverlordException("A target with name "+target.getCanonicalName+" already exists in "+doc.getBaseURI)

class EmptyExecutionGroupException(name : String, doc : Document)
    extends OverlordException(name+" execution group is empty in "+doc.getBaseURI)

class InvalidOverlordNameExcepion(name : String, pattern : String)
    extends OverlordException("Names must match pattern "+pattern+" name given: "+name)

class NoExecutionTargetsException(doc : Document)
    extends OverlordException("No execution targets in configuration at: "+doc.getBaseURI)

class NoGroupsException(doc : Document)
    extends OverlordException("No execution groups found in "+doc.getBaseURI)

class NoOverlordConfigurationException(filename : String)
    extends OverlordException("Could not locate configuration file "+filename)

class OverlordParsingException(doc : Document, exception : Exception)
    extends OverlordException("Error while parsing "+doc, exception)

class OverlordTargetClassNotFoundException(target : String, doc : Document, e : ClassNotFoundException)
    extends OverlordException(target+" target class could not be found in "+doc.getBaseURI, e)

class UnknownExecutionTargetException(name : String, doc : Document)
    extends OverlordException("No target named "+name+" found in "+doc.getBaseURI)

class UnsupportedPlatformException(platform : String)
    extends OverlordException(platform+" is not a supported platform for Overlord")