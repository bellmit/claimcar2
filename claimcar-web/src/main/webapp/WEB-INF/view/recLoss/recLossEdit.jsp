<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>损余回收处理</title>
</head>
<body>
	<div class="page_wrap">
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn  btn-primary" onclick="viewEndorseInfo('${prpLWfTaskVoList[0].registNo}')">保单批改记录</a>
			</div>
			<br />
		</div>
		<br />
		<p>
	<form action="#"  id="recLossform">
	<input type='hidden' value='${prpLWfTaskVoList[0].handlerStatus}' id="handlerStatus">
	<input type="hidden" value='<fmt:formatDate  value='${prpLRegistVo.damageTime}' pattern='yyyy-MM-dd HH:mm'/>' id="damageTime"/>
	<input type="hidden" value='${prpLWfTaskVoList[0].prpLRecLossVo.recLossMainId}' name="prpLRecLossVo.recLossMainId"/>		
		<!-- 损余回收 开始-->
		<div class="table_wrap">
			<div class="table_title f14">损余回收</div>
			<div class="table_cont ">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_label col-1">操作人:</label>
						<div class="form_input col-2">
						<input type="text" class="input-text" id="operator"  readOnly name="prpLRecLossVo.operatorName" value="<app:codetrans codeType="UserCode" codeCode="${userCode}" />" />
						<input type="hidden" readOnly name="prpLRecLossVo.operatorCode" value="${userCode}" />
						</div>
						<label class="form_label col-2">回收地点:</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" maxlength="50" name="prpLRecLossVo.recLossPlace" value="${prpLWfTaskVoList[0].prpLRecLossVo.recLossPlace}" title="${prpLWfTaskVoList[0].prpLRecLossVo.recLossPlace}"/>
						</div>
						<label class="form_label col-2">回收人联系电话:</label>
						<div class="form_input col-2">
							<input type="text" class="input-text" maxlength="25" name="prpLRecLossVo.handlerPhone" value="${prpLWfTaskVoList[0].prpLRecLossVo.handlerPhone}" datatype="m" ignore="ignore"/>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">回收人:</label>
						<div class="form_input col-2">
						<input type="text" class="input-text" style="width: 95%" maxlength="15" id="handler" datatype="*" nullmsg="请输入回收人！" name="prpLRecLossVo.handlerName" value="${prpLWfTaskVoList[0].prpLRecLossVo.handlerName}" />
						<font class="must">*</font>
						</div>
						<label class="form_label col-2">回收日期:</label>
						<div class="form_input col-2">
						<input type="text" class="Wdate" id="recLossTime" datatype="*" nullmsg="请输入回收日期！"
										name="prpLRecLossVo.recLossDate"
										value="<fmt:formatDate value='${prpLWfTaskVoList[0].prpLRecLossVo.recLossDate}' pattern='yyyy-MM-dd HH:mm'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endime\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})"
										style="width: 94%">
						<input type="hidden" id="endime" >
						<font class="must">*</font>
						</div>
						<label class="form_label col-2">回收金额:</label>
						<div class="form_input col-2">
						<input type="text" class="input-text" id="allSumRecLossFee" readOnly/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 损余回收 结束 -->
		
		<!-- 回收信息 开始 -->
		<div class="table_wrap">
			<div class="table_title f14">回收信息</div>
			<div class="table_cont table_list">
				<table class="table table-border table-hover" id="table">
					<thead class="text-c">
						<tr>
							<th>序号</th>
							<th>报案号</th>
							<th>损失项</th>
							<th>损余回收明细</th>
							<th>回收总额</th>
							<th>是否回收完毕</th>
							<c:if test="${prpLWfTaskVoList[0].handlerStatus eq '0'}">
							<th>操作</th>
							</c:if>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="prpLWfTaskVo" items="${prpLWfTaskVoList}" varStatus="status">
			 				<tr>
			 					<td> ${status.count}
									<input type="hidden" name="wfTaskSubmitVo[${status.index + size}].flowId" value="${prpLWfTaskVo.flowId}" />
									<input type="hidden" name="wfTaskSubmitVo[${status.index + size}].flowTaskId" value="${prpLWfTaskVo.taskId}" />
									<input type="hidden" name="wfTaskSubmitVo[${status.index + size}].currentNode" value="${prpLWfTaskVo.subNodeCode}">
									<input type="hidden" name="wfTaskSubmitVo[${status.index + size}].comCode" value="${prpLWfTaskVo.comCode}">
									<input type="hidden" name="wfTaskSubmitVo[${status.index + size}].handleIdKey" value="${prpLWfTaskVo.handlerIdKey}">
								</td>
								<td>${prpLWfTaskVo.registNo}</td>
			 					<td>${prpLWfTaskVo.lossItemName}</td>
			 					<td><input type="button" class='btn btn-zd' name="inputDetailBtn" onclick="inputDetail('${prpLWfTaskVo.taskId}','${prpLWfTaskVo.lossItemName}')"  value="录入"></td>
			 					<td><input type="text" id='${prpLWfTaskVo.taskId}Fee' class="recLossFee input-text" readOnly value="${prpLWfTaskVo.prpLRecLossVo.recLossFee}"></td>
			 					<td><input type="text" id='${prpLWfTaskVo.taskId}Ind' class="recLossInd input-text" readOnly value="<app:codetrans codeType="IsValid" codeCode="${prpLWfTaskVo.prpLRecLossVo.recLossInd}" />"></td>
			 					<c:if test="${prpLWfTaskVoList[0].handlerStatus eq '0'}">
			 					<td><input type="button" class='btn btn-zd' onclick="deleteTr(this)" name="deleteButton" value="删除"></td>
			 					</c:if> 
			 				</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 回收信息 结束 -->
		
		<!-- 损余回收意见开始 -->
		<div class="table_wrap">
			<div class="table_title f14">损余回收意见</div>
			<div class="table_cont table_list ">
			<div class="row cl">
				<textarea class="textarea h100" id="description"
						name="claimTextVo.description" datatype="*" nullmsg="请输入损余回收意见！" maxlength="150"
						placeholder="请输入...">${claimTextVo.description}</textarea>
			    <font class="must">*</font>
			</div>
			</div>
		</div>
		<!-- 损余回收意见 结束 -->
		
		<!-- 意见列表 开始 -->
		<div class="table_wrap">
			<div class="table_title f14">意见列表</div>
			<div class="table_cont table_list ">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th>角色</th>
							<th>操作人员</th>
							<th>机构</th>
							<th>发表意见时间</th>
							<th>意见</th>
							<th>审核状态</th>
						</tr>
					</thead>
					<tbody class="" id="claimTextItem">
						<c:forEach var="claimTextVo" items="${claimTextVos}" varStatus="status">
						<%-- <input type="hidden" name="claimTextVo[${status.index}].id" value="${claimTextVo.id}"> --%>
							<tr class="text-c" id="claimTextTr">
								<td>损余回收人员</td>
								<td>${claimTextVo.operatorName}</td>
								<td><app:codetrans codeType="ComCode" codeCode="${claimTextVo.comCode}"/></td>
								<td><fmt:formatDate value='${claimTextVo.inputTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
								<td><div title="${claimTextVo.description }" >
									<c:choose> 
										<c:when test="${fn:length(claimTextVo.description ) > 6}"> 
											${fn:substring(claimTextVo.description, 0, 6)}......
										</c:when>
										<c:otherwise>  
											${claimTextVo.description }
										</c:otherwise>  
										</c:choose> 
										</div>
								</td>
								<td><app:codetrans codeType="AuditStatus" codeCode="${claimTextVo.status}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 意见列表 结束 -->
		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
		    <input type="hidden" id="saveType" name="saveType" value=""/><!--提交类型 1保存，2提交  -->
			<input type="button" class="btn btn-primary" id="saveBtn"
				onClick="saveRecloss('1')"  value="暂存" />
			<input type="button" class="btn btn-primary" id="submitBtn"
				onClick="saveRecloss('2')" value="提交" />
		</div>
		<br /> <br /> <br /> <br />
		 </form>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/RecLossEdit.js"></script>
</body>
</html>