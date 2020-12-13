<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>损余回收处理</title>
</head>
<body>
	<div class="top_btn">
		<a class="btn  btn-primary" onclick="deflossView('${recLossType eq '1'?'car':'prop'}','${prpLRecLossVo.lossMainId}')">查看定损详细信息</a>
		<a class="btn  btn-primary" onclick="uploadCertifys('${prpLWfTaskVo.taskId}')">照片上传浏览</a>
	</div>
	<div class="fixedmargin page_wrap">
	<form action="#"  id="recLossform">
	<input type='hidden' value='${prpLWfTaskVo.handlerStatus}' id="handlerStatus">
	<input type='hidden' value='${prpLRecLossVo.prpLRecLossId}' name='prpLRecLossVo.prpLRecLossId'>
	<input type='hidden' value='${prpLRecLossVo.registNo}' name='prpLRecLossVo.registNo' id="registNo"/>
	<input type="hidden"  id="sumRecLossFee" name="prpLRecLossVo.recLossFee" value="${prpLRecLossVo.recLossFee}" />
		<!-- 基本信息开始-->
		<div class="table_wrap">
			<div class="table_title f14"><c:if test="${recLossType eq '1'}">车辆损失基本信息</c:if>
			<c:if test="${recLossType eq '2'}">财产损失基本信息</c:if></div>
			<div class="table_cont ">
				<div class="formtable">
					<div class="row  cl">
						<label class="form_input col-4">损余计算号（仅供财务查账用）:${prpLRecLossVo.prpLRecLossId}</label>
						<div class="form_input col-1">
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">报案号:</label>
						<div class="form_input col-2">
						<c:if test="${recLossType eq '1'}">${prpLDlossCarMainVo.registNo }</c:if>
			            <c:if test="${recLossType eq '2'}">${prpLdlossPropMainVo.registNo }</c:if>
						</div>
						<label class="form_label col-2">定损员:</label>
						<div class="form_input col-2">
						<c:if test="${recLossType eq '1'}"><app:codetrans codeType="UserCode" codeCode="${prpLDlossCarMainVo.handlerCode}" /></c:if>
			            <c:if test="${recLossType eq '2'}"><app:codetrans codeType="UserCode" codeCode="${prpLdlossPropMainVo.handlerCode}" /></c:if>
						</div>
						<label class="form_label col-2">核损员:</label>
						<div class="form_input col-2">
						<c:if test="${recLossType eq '1'}"><app:codetrans codeType="UserCode" codeCode="${prpLDlossCarMainVo.underWriteCode}" /></c:if>
			            <c:if test="${recLossType eq '2'}"><app:codetrans codeType="UserCode" codeCode="${prpLdlossPropMainVo.underWriteCode }" /></c:if>
						</div>
					</div>
					<c:if test="${recLossType eq '1'}">
					<div class="row  cl">
						<label class="form_label col-1">车牌号码:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.licenseNo}
						</div>
						<label class="form_label col-2">号牌底色:</label>
						<div class="form_input col-2">
						</div>
						<label class="form_label col-2">车型名称:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.modelName }
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">车架号:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.frameNo}
						</div>
						<label class="form_label col-2">VIN码:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.vinNo}
						</div>
						<label class="form_label col-2">发动机号:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.engineNo}
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">出险驾驶员:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.lossCarInfoVo.carOwner  }
						</div>
						<label class="form_label col-2">修理地点:</label>
						<div class="form_input col-2">
						${prpLDlossCarMainVo.defSite}
						</div>
						<c:if test="${prpLDlossCarMainVo.deflossCarType eq '1'}">
						<label class="form_label col-2">承保公司:</label>
						<div class="form_input col-2">
						<app:codetrans codeType="ComCode" codeCode="${prpLDlossCarMainVo.lossCarInfoVo.insureComName}" />
						</div>
						</c:if>
					</div>
					 <!-- 三者车才显示车辆承保信息 -->
					<c:if test="${prpLDlossCarMainVo.deflossCarType eq '3'}">
					 <div class="row cl">
						<label class="form_label col-1">商业承保机构:</label>
						<div class="form_input col-2">
						    <app:codetrans codeType="CIInsurerCompany" codeCode="${prpLDlossCarMainVo.lossCarInfoVo.biInsureComCode}" />
						</div>
						<label class="form_label col-2">交强承保机构:</label>
						<div class="form_input col-2">
						<app:codetrans codeType="CIInsurerCompany" codeCode="${prpLDlossCarMainVo.lossCarInfoVo.ciInsureComCode}" />
						</div>
					</div>	
					</c:if>
				   </c:if>
					<div class="row cl">
						<label class="form_label col-1">报案时间:</label>
						<div class="form_input col-2">
						<fmt:formatDate value='${prpLRegistVo.reportTime}' pattern='yyyy-MM-dd HH:mm'/>
						</div>
						<label class="form_label col-2">出险时间:</label>
						<div class="form_input col-2">
						<input type="hidden" value='<fmt:formatDate  value='${prpLRegistVo.damageTime}' pattern='yyyy-MM-dd HH:mm'/>' id="damageTime"/>
						<fmt:formatDate  value='${prpLRegistVo.damageTime}' pattern='yyyy-MM-dd HH:mm'/>
						</div>
						<label class="form_label col-2">出险地点:</label>
						<div class="form_input col-2">
						<app:codetrans codeType="AreaCode" codeCode="${prpLRegistVo.damageAreaCode}" />-
						${prpLRegistVo.damageAddress}
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 基本信息 结束 -->
		 
		<!-- 当前有效立案列表 开始 -->
		<div class="table_wrap">
			<div class="table_title f14">当前有效立案列表</div>
			<div class="table_cont table_list">
				<table class="table table-border table-hover">
					<thead class="text-c">
						<tr>
							<th>序号</th>
							<th>立案号</th>
							<th>保单号</th>
							<th>出险标志</th>
							<th>立案时间</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="prpLClaim" items="${prpLClaimList}" varStatus="status">
			 				<tr>
			 					<td>${status.count}</td>
			 					<td>${prpLClaim.claimNo}</td>
			 					<td>${prpLClaim.policyNo}</td>
			 					<td><app:codetrans codeType="RiskCode" codeCode="${prpLClaim.riskCode}" /></td>
			 					<td><fmt:formatDate value='${prpLClaim.claimTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
			 				</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 当前有效立案列表结束 -->
		
		<!-- 工作流参数隐藏域开始 -->
		<input type="hidden" id="flowId" name="wfTaskSubmitVo.flowId" value="${prpLWfTaskVo.flowId}" />
		<input type="hidden" id="flowTaskId" name="wfTaskSubmitVo.flowTaskId" value="${prpLWfTaskVo.taskId}" />
		<input type="hidden" id="subNodeCode" name="wfTaskSubmitVo.currentNode" value="${prpLWfTaskVo.subNodeCode}">
		<input type="hidden" id="nodeCode"  value="${prpLWfTaskVo.nodeCode}">
		<!-- 回收明细 开始 -->
		<div class="table_wrap">
			<div class="table_title f14">回收明细 </div>
			<div class="table_cont table_list">
				<table class="table table-border table-hover">
					<thead class="text-c">
						<tr>
							<th>序号</th>
							<th>损失险别</th>
							<th><c:if test="${recLossType eq '1'}">换件名称</c:if><c:if test="${recLossType eq '2'}">财产名称</c:if></th>
							<th>数量</th>
							<th>核损金额</th>
							<th>回收金额</th>
							<th>是否回收完毕<input type="checkbox" id="checkAll"></th>
							<th>回收编号(年份+流水号(10位))</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="prpLRecLossDetail" items="${prpLRecLossVo.prpLRecLossDetails}" varStatus="status">
			 				<tr>
			 					<td> ${status.count}
			 					<input type="hidden" name="prpLRecLossDetails[${status.index + size}].id" value="${prpLRecLossDetail.id}"/></td>
			 					<c:if test="${empty prpLRecLossDetail.kindCode}">
			 					<td>
			 					<input type="hidden" id="kindName" name="prpLRecLossDetails[${status.index + size}].kindName" value=""/>
			 					<span class="select-box">
									 <select name="prpLRecLossDetails[${status.index + size}].kindCode" onchange="selKindCode(this)" class="kindCode select">
									 <c:if test="${tci}"><option value="A">机动车损失保险</option></c:if>
									 <c:if test="${vci}"><option value="B">第三者责任保险</option></c:if>
									</select>
								</span>
			 					</td>
			 					</c:if>
			 					<c:if test="${not empty prpLRecLossDetail.kindCode}">
			 					<td><app:codetrans codeType="KindCode" codeCode="${prpLRecLossDetail.kindCode}" riskCode="${prpLRegistVo.riskCode}" /></td>
			 					</c:if>
			 					<td>${prpLRecLossDetail.itemName}</td>
			 					<td>${prpLRecLossDetail.recLossCount}</td>
			 					<td>${prpLRecLossDetail.sumVeriLoss}</td>
			 					<td><input type='text' maxlength="12" class="recLossFee input-text" name="prpLRecLossDetails[${status.index + size}].recLossFee" value="<c:if test='${empty prpLRecLossDetail.recLossFee}'>0</c:if><c:if test='${not empty prpLRecLossDetail.recLossFee}'>${prpLRecLossDetail.recLossFee}</c:if>" datatype="amount"/></td>
			 					<td><input type='checkbox' class='subBox' <c:if test="${prpLRecLossDetail.recLossInd eq '1'}">checked=checked</c:if> value='1' name="prpLRecLossDetails[${status.index + size}].recLossInd"/></td>
			 					<td><input type='text' class="input-text" name="prpLRecLossDetails[${status.index + size}].recLossNo" value='${prpLRecLossDetail.recLossNo}' readOnly/></td>
			 					<td><input type='text' class="input-text" title="${prpLRecLossDetail.remark}" maxlength="50" name="prpLRecLossDetails[${status.index + size}].remark" value='${prpLRecLossDetail.remark}'/></td>
			 				</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 回收明细 结束 -->
		
		<!-- 底部按钮 -->
		<br /> <br /> <br />
		<!-- 底部按钮 -->
		<div class="btn-footer clearfix text-c">
		    <input type="hidden" id="saveType" name="saveType" value=""/>
			<input type="button" class="btn btn-primary" id="saveBtn"
				onClick="saveReclossDetails()"  value="保存" />
		</div>
		<br /> <br /> <br /> <br />
		 </form>
	</div>
	<script type="text/javascript" src="${ctx }/js/flow/RecLossDetailEdit.js"></script>
</body>
</html>