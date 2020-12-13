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
				<app:codeSelect codeType="DealReason" type="select"
					 id="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" />
					<input type="hidden" name="dealReasoon" value="${prpLcancelTraceVo.dealReasoon }" >
				<font class="must">*</font>
			</div>
			<label class="form_label col-1">请填写原因</label>
			<div class="form_input col-2">
				<!-- <input type="text" class="input-text" placeholder="原因自定义标签" /> -->
				<app:codeSelect codeType="ApplyReason" type="select" id="applyReason"
					 value="${prpLcancelTraceVo.applyReason }" />

				<input type="hidden" name="applyReason" value="${prpLcancelTraceVo.applyReason }" >
				<font class="must">*</font>
			</div>
			<label class="form_label col-1">备注</label>
			<div class="form_input col-4">
				 <input type="text" class="input-text" name="remarks" value="${prpLcancelTraceVo.remarks }" id="remarks"/> 
			</div>
			<input type="hidden"  name="remarks" value="${prpLcancelTraceVo.remarks }" />
		</div>
		<!-- <div class="row cl">
			<div class="col-12">
				<input type="textarea" class="textarea">
				<font class="must">*</font>
			</div>
		</div> -->
	</div>
</div>

<%-- <c:if test="">审核退回意见输入框 -->--%>
<%-- <c:if test="${(handlerStatus!=3)||(workStatus!=6) }">
<div class="table_wrap">
	<div class="table_title f14">审核/退回原因</div>
	<div class="formtable">
		<div class="row cl">
			<div class="col-12">
			<c:if test="${(handlerStatus eq 2)}">
				<textarea type="textarea" class="textarea" name="description" id="description">${prpLcancelTraceVo.aandelCode } </textarea>
			</c:if>
			
			<c:if test="${handlerStatus!='2' }">
				<textarea type="textarea" class="textarea" name="description" id="description"></textarea>
			</c:if>
				<font class="must">*</font>
			</div>
		</div>
	</div>
</div>
</c:if> --%>
<c:if test="${(handlerStatus!=3)||(workStatus!=6) }">
	<div class="table_wrap">
		<div class="table_title f14">审核/退回原因</div>
		<div class="table_cont formtable">
			<div class="row cl">
				<label class="form_label col-1.3"><strong>审核/退回原因: </strong></label>
				<div class="form_input col-10" id="reasonCodes">
					<app:codeSelect codeType="claimCancelType"  name="reasonCode" nullToVal="a" value="${claimTextVoByTaskInUser.reasonCode }" type="radio"/>
				</div>
				<div class="form_input col-10" hidden="hidden" id="reasonCodess"> 
					<app:codeSelect codeType="claimCancelType"  name="aaa" nullToVal="a" value="${claimTextVoByTaskInUser.reasonCode }" type="radio"/>
				</div>
				<br>
			</div>
			<div class="row cl">
						<div class="col-12">
							<c:if test="${(handlerStatus eq 0)}">
								<textarea type="textarea" class="textarea" name="description" id="description"></textarea>
							</c:if>
					
							<c:if test="${handlerStatus!='0' }">
								<textarea type="textarea" class="textarea" name="description" id="description">${claimTextVoByTaskInUser.description } </textarea>
							</c:if>
						<font class="must">*</font>
					</div>
			</div>
		</div>
	</div>
	</c:if>
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