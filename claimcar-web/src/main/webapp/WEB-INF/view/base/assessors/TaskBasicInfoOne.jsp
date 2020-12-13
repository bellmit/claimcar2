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
					<input type="hidden" name="assessorMainVo.id" value="${assessorMainVo.id }">
					<input type="hidden" name="assessorMainVo.intermcode" value="${assessorMainVo.intermcode }">
			<div class="row cl">
				<label class="form_label col-1">任务号</label>
				<div class="form_input col-4">
					<input type="text"  class="input-text"  name="assessorMainVo.taskId" value="${assessorMainVo.taskId }" readonly="readonly">
				</div>
				<label class="form_label col-1">公估机构</label>
				<div class="form_input col-4">
					<input type="text"  class="input-text"  name="assessorMainVo.intermname" value="${assessorMainVo.intermname }" readonly="readonly">
					</div>
				<div class="form_input col-2">
					<a class="btn btn-zd " onClick="layerShow('${intermMainVo.id}')">银行信息</a>
				</div>
			</div>
			<br>
			<div class="row cl">
				<label class="form_label col-3"></label> <label class="form_label col-3">整案最高资费标准</label>
				<div class="form_input col-4"><input type="text"  class="input-text"  name="intermMainVo.payStandardMax " value="${intermMainVo.payStandardMax }" readonly="readonly"></div>
				<label class="form_label col-2"></label>
			</div>

			<div class="row cl">
				<label class="form_label col-4">公估服务类型</label> <label class="form_label col-2"></label> <label class="form_label col-3">资费标准</label> <label class="form_label col-3"></label>
			</div>

			<c:forEach var="prpdIntermServer" items="${intermMainVo.prpdIntermServers}" varStatus="status">
				<div class="row cl">
					<label class="form_label col-1"></label>
					<div class="form_input col-5">
						<input type="hidden" name="prpdIntermServer.serviceType" value="${prpdIntermServer.serviceType }" />
						
						<input type="text" class="input-text" readonly="readonly" value="<app:codetrans codeType="ServiceType" codeCode="${prpdIntermServer.serviceType}"/>" />
					</div>
					<label class="form_label col-1"></label>
					<div class="form_input col-5">
						<input type="text" class="input-text" name="prpdIntermServer.feeStandard" readonly="readonly" value="${prpdIntermServer.feeStandard}" />
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>



