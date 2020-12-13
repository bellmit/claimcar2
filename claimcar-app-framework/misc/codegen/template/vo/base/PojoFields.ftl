<#foreach field in pojo.getAllPropertiesIterator()>
<#if pojo.getMetaAttribAsBool(field, "gen-property", true)>
	${pojo.getFieldModifiers(field)} ${pojo.getVoJavaTypeName(field, jdk5)} ${field.name}<#if pojo.hasFieldInitializor(field, jdk5)> = ${pojo.getVoFieldInitialization(field, jdk5)}</#if>;
</#if>
</#foreach>
