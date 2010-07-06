<#include "attr.ftl">
<#macro formcontrol>
	<#if parameters.label?? >
		<label
			<@attr "for" parameters.id />
			>
			${parameters.label?html}
		</label>
	</#if>
	<#nested>
</#macro>