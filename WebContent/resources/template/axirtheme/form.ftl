<#include "macro/attr.ftl">
<#compress>
	<form 
		<@attr "id" parameters.id /> 
		<@attr "name" parameters.name />
		<@attr "onsubmit" parameters.onsubmit />
		<@attr "onreset" parameters.onreset />
		<@attr "action" parameters.action />
		<@attr_default "method" "post" parameters.method />
		<@attr "enctype" parameters.enctype />
		<@attr "class" parameters.cssClass />
		<@attr "style" parameters.cssStyle />
		<@attr "title" parameters.title />
		<@attr "accept-charset" parameters.acceptcharset />
	><table>
</#compress>