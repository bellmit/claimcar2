/**
 * Custom VO class of PO ${pojo.getClassJavaDoc(pojo.getDeclarationName(), 0)}
 */ 
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()}Vo extends ${pojo.getDeclarationName()}VoBase<#if pojo.hasExtendsDeclaration()> ${pojo.getExtendsDeclaration()}</#if> ${pojo.getImplementsDeclaration()}