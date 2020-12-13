<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">任务基本信息</div>
	<div class="table_cont reportInfo">
		<div class="formtable">
			<!--  -->
			        <input type="hidden" id="flowNodeCode" name="flowNodeCode" value="${taskVo.nodeCode }">
					<input type="hidden" id="flowNodeName" name="flowNodeName" value="${taskVo.subNodeCode }">
					<input type="hidden" id="flowTaskId" name="flowTaskId" value="${taskVo.taskId }">
					<input type="hidden" id="handlerStatus" name="handlerStatus" value="${handlerStatus }">
					<input type="hidden" id="audit" name="audit" >
					<input type="hidden" name="prpLAcheckMainVo.id" value="${prpLAcheckMainVo.id }">
					<input type="hidden" name="prpLAcheckMainVo.intermcode" value="${prpLAcheckMainVo.checkcode }">
			<div class="row cl">
				<label class="form_label col-1">任务号</label>
				<div class="form_input col-4">
					<input type="text"  class="input-text"  name="prpLAcheckMainVo.taskId" value="${prpLAcheckMainVo.taskId }" readonly="readonly">
				</div>
				<label class="form_label col-1">查勘机构</label>
				<div class="form_input col-4">
					<input type="text"  class="input-text"  name="prpLAcheckMainVo.checkname" value="${prpLAcheckMainVo.checkname }" readonly="readonly">
					</div>
				<div class="form_input col-2">
					<a class="btn btn-zd " onClick="layerShow('${checkBankMainVo.id}')">银行信息</a>
				</div>
			</div>
			<br>
			<div class="row cl">
				<label class="form_label col-3"></label> <label class="form_label col-3">整案最高资费标准</label>
				<div class="form_input col-4"><input type="text"  class="input-text"  name="checkBankMainVo.payStandardMax " value="${checkBankMainVo.payStandardMax }" readonly="readonly"></div>
				<label class="form_label col-2"></label>
			</div>

			<div class="row cl">
				<label class="form_label col-4">查勘服务类型</label> <label class="form_label col-2"></label> <label class="form_label col-3">资费标准</label> <label class="form_label col-3"></label>
			</div>

			<c:forEach var="prpdcheckServer" items="${checkBankMainVo.prpdcheckServers}" varStatus="status">
				<div class="row cl">
					<label class="form_label col-1"></label>
					<div class="form_input col-5">
						<input type="hidden" name="prpdcheckServer.serviceType" value="${prpdcheckServer.serviceType }" />
						
						<input type="text" class="input-text" readonly="readonly" value="<app:codetrans codeType="ServiceType" codeCode="${prpdcheckServer.serviceType}"/>" />
					</div>
					<label class="form_label col-1"></label>
					<div class="form_input col-5">
						<input type="text" class="input-text" name="prpdcheckServer.feeStandard" readonly="readonly" value="${prpdcheckServer.feeStandard}" />
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>



