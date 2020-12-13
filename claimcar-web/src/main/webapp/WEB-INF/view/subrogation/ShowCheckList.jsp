<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>案件互审</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
	<div class="table_title f14">互审确认信息</div>
	<div class="formtable">
		<div class="row mb-3 cl">
			<label class="form_label col-2">结算码</label>
			<div class="form_input col-4">${platCheckVoList.recoveryCode }</div>
			<label class="form_label col-2">追偿起始时间</label>
			<div class="form_input col-4"><app:date date="${platCheckVoList.recoverdStart }" format="yyyy-MM-dd HH:mm:ss"/></div>
		</div>
		<div class="row mb-3 cl">
			<label class="form_label col-2">追偿方报案号</label>
			<div class="form_input col-4">${platCheckVoList.recoverReportNo }</div>
			<label class="form_label col-2">责任对方报案号</label>
			<div class="form_input col-4">${platCheckVoList.compensateReportNo }</div>
		</div>

		<div class="row mb-3 cl">
			<label class="form_label col-2">追偿方保险公司</label>
			<c:set var="recoverComCode">
			<app:codetrans codeType="DWInsurerCode" codeCode="${platCheckVoList.recoverComCode }"/>
			</c:set>
			<div class="form_input col-4">${recoverComCode }</div>
			<label class="form_label col-2">追偿方承保地区</label>
			<c:set var="recoverAreaCode">
			<app:codetrans codeType="DWInsurerArea" codeCode="${platCheckVoList.recoverAreaCode }"/>
			</c:set>
			<div class="form_input col-4">${recoverAreaCode }</div>
		</div>
		<div class="row mb-3 cl">
			<label class="form_label col-2">责任对方保险公司</label>
			<c:set var="compensateComCode">
			<app:codetrans codeType="DWInsurerCode" codeCode="${platCheckVoList.compensateComCode }"/>
			</c:set>
			<div class="form_input col-4">${compensateComCode }</div>
			<label class="form_label col-2">责任对方承保地区</label>
			<c:set var="compensateAreaCode">
			<app:codetrans codeType="DWInsurerArea" codeCode="${platCheckVoList.compensateAreaCode }"/>
			</c:set>
			<div class="form_input col-4">${compensateAreaCode }</div>
		</div>

		<div class="row mb-3 cl">
			<label class="form_label col-2">代付/清付险种</label>
			<div class="form_input col-4"><app:codetrans codeType="DWCoverageType" codeCode="${platCheckVoList.coverageCode }"/> </div>
			<label class="form_label col-2">互审状态</label>
			<c:set var="checkStat">
			<app:codetrans codeType="CheckStats" codeCode="${platCheckVoList.checkStats }"/>
			</c:set>
			<div class="form_input col-4">${checkStat }</div>
		</div>

	</div>
	<div class="table_title">互审详细信息列表</div>
	<table id="resultDataTable"
		class="table table-border table-hover data-table" cellpadding="0"
		cellspacing="0">
		<thead>
			<tr class="text-c">
				<th>序号</th>
				<th>代付金额</th>
				<th>清付金额</th>
				<th>互审时间</th>
				<th>审核方类型</th>
				<th>互审状态</th>
				<th>互审意见</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="platCheckSubVo" items="${prpLPlatCheckSubVoList}"
				varStatus="vstatus">
				<tr class="text-c">
					<td>${vstatus.index+1}</td>
					<td>${platCheckSubVo.recoverAmount}</td>
					<td>${platCheckSubVo.compensateAmount}</td>
					<td> <app:date date="${platCheckSubVo.checkDate }" format="yyyy-MM-dd HH:mm:ss"/></div>
					</td>
					<c:if test="${platCheckSubVo.checkOwnType==1}">
					<td>追偿方</td>
					</c:if>
					<c:if test="${platCheckSubVo.checkOwnType==2}">
					<td>责任对方</td>
					</c:if>
					<c:set var="checkStats">
						<app:codetrans codeType="CheckStats" codeCode="${platCheckSubVo.checkStats}"/>
					</c:set>
					<td>${checkStats}</td>
					<td>${platCheckSubVo.checkOpinion}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	</div>

	<div class="line"></div>
	<div class="table_title f14">互审确认信息</div>

	<form id="form" name="form" class="form-horizontal" role="form"
		method="post">

		<div class="formtable">

			<input hidden="hidden" name="accountsNo"
				value="${platCheckVoList.recoveryCode }" />
			<div class="row mb-3 cl">
				<label class="form-label col-2 text-c">互审状态</label>
				<div class="col-3 mt-5">
					<span class="select-box"> <app:codeSelect
							codeType="CheckStats" type="select" id="checkStats"
							name="checkStats" lableType="code-name" clazz="must"/>
					</span>
				</div>
			</div>
			<div class="row mb-3 cl">
				<label class="form-label col-2 text-c">互审意见</label>
				<div class=" col-9 ">
						<textarea class="textarea" name="checkOpinion" id="checkOpinion"
							nullmsg="请输入处理意见" datatype="s1-2000" errormsg="处理意见长度最多为2000字符">${surveyVo.opinionDeac }</textarea>
				</div>

			</div>
		<p>
			<div class="row mb-3 cl">
				<div class="text-c">
					<input type="submit" class="btn btn-primary" id="checkButton" onclick="check()" value="互审确认" /> 
					<!-- <input type="button" class="btn btn-primary" onclick="closeL();" value="关闭" /> --> 
					</span><br />
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
		$(function() {
			$.Datatype.licenseNo = /^[\u4e00-\u9fa5-A-Z]{1}[A-Z]{1}[A-Z_0-9]{5}$/;
			
			var ajaxEdit = new AjaxEdit($('#form'));
			ajaxEdit.targetUrl = "/claimcar/subrogationEdit/checkQueryList"; 
			ajaxEdit.afterSuccess = saveAfter;
			//绑定表单
			ajaxEdit.bindForm();
		});

		function check() {
			$('#form').submit();
		}
		
		function saveAfter(){
			layer.msg("互审确认成功!");
			$("#checkButton").attr("disabled","disabled");
			$("#checkButton").addClass("btn-disabled");
		}
		
		function closeL(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭 
		}
		
	</script>
</body>
</html>