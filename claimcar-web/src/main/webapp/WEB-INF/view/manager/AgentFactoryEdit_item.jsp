<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<tr  class="text-c">
	<input type="hidden" name="agentFactoryVo[${agentId }].factoryId" value="${agentFactoryVo.factoryId }" id="agentfacId" />
	<input type="hidden" name="agentFactoryVo[${agentId }].factoryCode" value="${agentFactoryVo.factoryCode }" />
	<input type="hidden" name="agentFactoryVo[${agentId }].factoryName" value="${agentFactoryVo.factoryName }"  />
	<input type="hidden" name="agentFactoryVo[${agentId }].id" value="${agentFactoryVo.id }" />
	<td>
		<button type="button"
			class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao" onclick="delAgentItem(this)"
			 name="delAgentItem_${agentId }"></button>
	</td>
	<td>
	<%-- <c:if test="${empty agentFactoryVo.id }">
	<span class="select-box">
		<app:codeSelect codeType="" type="select" lableType="code-name"  onchange="agentCodeChange(this,'${agentId }')"
			name="agentname${agentId }" dataSource="${resultMap }" value="${agentFactoryVo.agentCode}" nullToVal="0" /> 
			</span>
	</c:if>
	<c:if test="${!empty agentFactoryVo.id }">
	<input type="text" class="input-text" value="${agentFactoryVo.agentName}"  readonly="readonly"/>
	</c:if> --%>
		<input type="text" class="input-text" value="${agentFactoryVo.agentName}"  readonly="readonly"/>
		<input type="hidden" name="agentFactoryVo[${agentId }].agentName" value="${agentFactoryVo.agentName}" />
	</td>
	<td><input type="text" class="input-text" name="agentFactoryVo[${agentId }].agentCode" value="${agentFactoryVo.agentCode}" readonly="readonly" datatype="*">
	</td>
	<!-- 工号 -->
	<td><input type="text" class="input-text" name="agentFactoryVo[${agentId }].userCode" value="${agentFactoryVo.userCode}"></td>
	
	<td><input type="text" class="input-text" name="agentFactoryVo[${agentId }].agentPhone" value="${agentFactoryVo.agentPhone}"></td>
	<td>
	<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont" onclick="showInsuredFac('${agentFactoryVo.id }','${agentFactoryVo.agentCode}','${agentFactoryVo.agentName}')">维护被保险人</button>
	</td>
</tr>