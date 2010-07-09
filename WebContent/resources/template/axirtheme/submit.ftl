<#include "macro/attr.ftl">
<#include "macro/formcontrol.ftl">
<@formcontrol ' submit'>
	<#if parameters.type?? && parameters.type=="button">
		<button type="submit"
			<@attr "id" parameters.id />
			<@attr "name" parameters.name />
			<#if parameters.nameValue??>
				value="<@s.property value="parameters.nameValue"/>"<#rt/>
			</#if>
			<@attr "class" parameters.cssClass />
			<@attr "style" parameters.cssStyle />
			<@attr "title" parameters.title />
			<@attr "tabindex" parameters.tabindex />
			
			<@attr_list parameters.dynamicAttributes />
			<@attr_scripting/>
		>
	<#else>
		<@s.property value="parameters.body"/>
		<#if parameters.type?? && parameters.type=="image">
			<input type="image"
				<@attr "alt" parameters.label />
				<@attr "src" parameters.src />
		<#else>
			<input type="submit"
		</#if>
		
		<@attr "id" parameters.id />
		<@attr "name" parameters.name />
		<@attr "value" parameters.nameValue />
		<@attr "class" parameters.cssClass />
		<@attr "style" parameters.cssStyle />
		<@attr "title" parameters.title />
		<@attr "tabindex" parameters.tabindex />
		
		<#if parameters.disabled?default(false)>
		 disabled="disabled"
		</#if>			
	</#if>
</@formcontrol>