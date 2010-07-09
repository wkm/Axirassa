<#--
	attr.ftl
	
	A set of macros for performing common action with tag attributes
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

<#macro attr_scripting>
	<@attr "onclick" parameters.onclick />
	<@attr "ondblclick" parameters.ondblclick />
	<@attr "onmousedown" parameters.onmousedown />
	<@attr "onmouseup" parameters.onmouseup />
	<@attr "onmouseover" parameters.onmouseover />
	<@attr "onmousemove" parameters.onmousemove />
	<@attr "onmouseout" parameters.onmouseout />
	<@attr "onfocus" parameters.onfocus />
	<@attr "onblur" parameters.onblur />
	<@attr "onkeypress" parameters.onkeypress />
	<@attr "onkeydown" parameters.onkeydown />
	<@attr "onkeyup" parameters.onkeyup />
	<@attr "onselect" parameters.onselect />
	<@attr "onchange" parameters.onchange />
</#macro>