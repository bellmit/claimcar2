/**
 * POJO Class ${pojo.getClassJavaDoc(pojo.getDeclarationName(), 0)}
 */
<#include "Ejb3TypeDeclaration.ftl"/>
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()}<#if pojo.hasExtendsDeclaration()> ${pojo.getExtendsDeclaration()}</#if> ${pojo.getImplementsDeclaration()}