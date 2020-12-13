${pojo.getPackageDeclaration()}
<#assign classbody> 
<#include "PojoTypeDeclaration.ftl"/> {
	private static final long serialVersionUID = 1L; 
	
}
</#assign>
${pojo.generateImports()}
${classbody}