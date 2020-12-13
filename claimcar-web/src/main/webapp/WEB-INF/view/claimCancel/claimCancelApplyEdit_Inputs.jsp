<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">注销/拒赔原因</div>
	<div class="formtable">
		<div class="row cl mb-5">
			<label class="form_label col-1">任务类型</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="任务类型自定义标签" /> -->
				<app:codeSelect codeType="DealReason" type="select" name="dealReasoon" id="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }"/>
				<!-- <font class="must">*</font> -->
				<c:if test="${(Status=='0') && (tuiHui != '1') }">
					<input type="hidden" name="applyReason" value="${prpLcancelTraceVo.applyReason }" >
					<input type="hidden" name="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" >
					<input type="hidden" name="remarks" value="${prpLcancelTraceVo.remarks }" >
					<input type="hidden" name="swindleSum" value="${prpLcancelTraceVo.swindleSum }" >
					<input type="hidden" name="swindleType" value="${prpLcancelTraceVo.swindleType }" >
					<input type="hidden" name="swindleReason" value="${prpLcancelTraceVo.swindleReason }" >
				</c:if>
			</div>
			<label class="form_label col-1">请填写原因</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="ApplyReason" type="select" id="applyReason" name="applyReason" value="${prpLcancelTraceVo.applyReason }"/>
				
				<!-- <font class="must">*</font> -->
			</div>
			<c:if test="${tuiHui eq '1'}">
			<input type="hidden" name="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" >
			</c:if>
			
			<label class="form_label col-1">备注</label>
			<div class="form_input col-4">
				 <input type="text" class="input-text" name="remarks" value="${prpLcancelTraceVo.remarks }" id="remarks"/> 
			</div>
			
	</div>
	<div class="row cl mb-5">
			<div id="zhapian" class="hidden">
			<label class="form_label col-1">欺诈标志</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="SwindleReason" id="swindleReason" type="select" name="swindleReason" value="${prpLcancelTraceVo.swindleReason }"/>
		
			</div>
			<label class="form_label col-1">欺诈类型</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="SwindleType" id="swindleType" type="select" name="swindleType" value="${prpLcancelTraceVo.swindleType }"/>
				
			</div>
			<label class="form_label col-2">欺诈挽回损失金额</label>
			<div class="form_input col-3">
				<input type="text" class="input-text" id="swindleSum" placeholder="欺诈挽回损失金额" name="swindleSum" value="${prpLcancelTraceVo.swindleSum }"/>
				<font class="form-text" color="red">*</font> 
				<%-- <app:codeSelect codeType="DealReason" type="select" name="swindleSum" id="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }"/>
				 --%>
			</div>
			</div>
			
	</div>
</div>

<%-- <c:if test="">
<!-- 审核退回意见输入框 -->
<div class="table_wrap">
	<div class="table_title f14">审核/退回原因</div>
	<div class="formtable">
		<div class="row cl">
			<div class="col-12">
				<input type="textarea" class="textarea" name="prpLcancelTrace.>
				<font class="must">*</font>
			</div>
		</div>
	</div>
</div>
</c:if> --%>

<div class="table_wrap">
	<div class="table_title f14">审核/退回意见列表</div>
	<div class="table_cont">
		<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>序号</th>
						<th>时间</th>
						<th>操作员</th>
						<th>岗位</th>
						<th>内容</th>
					</tr>
				</thead>
				<tbody class="text-c">
				<c:forEach var="prpLrejectClaimTextVo" items="${prpLrejectClaimTextVos}" varStatus="index">
					<tr class="text-c" id="claimTextTr">
						<td>${index.index+1}</td>
						<c:set var="operateDate">
								<fmt:formatDate value="${prpLrejectClaimTextVo.operateDate}"/>
							</c:set>
						<td>${operateDate }</td>
						 	<c:set var="operatorName">
					 	 	<app:codetrans codeType="UserCode" codeCode="${prpLrejectClaimTextVo.operatorName }"/> 
					 	 	</c:set>
						<td>${operatorName }</td>
						<td>${prpLrejectClaimTextVo.stationName }</td>
						<td>${prpLrejectClaimTextVo.description }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
	</div>
</div>