
package axirassa.services.util

import java.util.HashMap
import axirassa.util.AutoSerializingObject
import java.io.IOException
import java.io.StringWriter
import java.util.Map

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException

trait Template {
    def location : String
    def fullLocation : String
}

trait TemplateType {
    def extension : String
}

object TemplateFactory {
    val TEMPLATE_ENCODING = "UTF-8"
}

abstract class TemplateFactory[T <: Template, TType <: TemplateType](baseDirectory : String) {
    val configuration = new Configuration()
    configuration.setWhitespaceStripping(true)
    configuration.setClassForTemplateLoading(classOf[TemplateFactory[_, _]], baseDirectory)

    def getTemplate(template : T, templateType : TType) =
        configuration.getTemplate(getTemplateLocation(template, templateType), TemplateFactory.TEMPLATE_ENCODING)

    private def getTemplateLocation(template : T, templateType : TType) =
        template.location+"_"+templateType.extension+".ftl"

    def getText(template : T, templateType : TType, attributes : Map[String, Object]) = {
        val writer = new StringWriter()
        getTemplate(template, templateType).process(attributes, writer)
        writer.toString()
    }
} 

class TemplateFillingMessage extends AutoSerializingObject {
    val attributeMap = new HashMap[String, AnyRef]

    def addAttribute(key : String, value : AnyRef) {
        attributeMap.put(key, value)
    }

    def addAttributes(attributes : Map[String, Object]) {
        attributeMap.putAll(attributes)
    }
}