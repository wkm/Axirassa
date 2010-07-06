<#--
	attr.ftl
	
	A set of macros for preforming common action with tag attributes
-->

<#-- <@attr "id" value /> -->
<#macro attr name param="null">
	<#if param != "null">${name}="${param}"</#if>
</#macro>

<#-- <@attr_default "name" "default" value /> -->
<#macro attr_default name default param="null">
	${name} = "${param?default(default)?html}"
</#macro>

<#-- <@attrlist map /> -->
<#macro attr_list parameters="null">
	<#if (parameters != "null") && (parameters??) && (parameters?size > 0)>
		<#assign keys = parameters.keySet() />
		<#list key as keys>
			${key}="${parameteres[key]?html}"
		</#list>
	</#if>
</#macro>