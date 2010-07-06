<#include "macro/attr.ftl">
<#compress>
	<label
		<@attr "id" parameters.id />
		<@attr "class" parameters.cssClass />
		<@attr "style" parameters.cssStyle />
		<@attr "title" parameters.title />
		<@attr "for" parameters.for />
		<#if parameters.nameValue?? >
			<@s.property value="parameters.nameValue" />
		</#if>
		<@attr_list parameters.dynamicAttributes />
	/>
</#compress>