<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">费用</div>
	<div class="table_cont  table_list">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th>报案号</th>
					<th>保单号</th>
					<th>立案号</th>
					<th>计算书号</th>
					<th>险别</th>
					<th>被保险人</th>
					<th>已赔付金额</th>
					<th>任务详情</th>
					<th>费用金额</th>
					<th>摘要</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<c:forEach var="checksFee" items="${prpLAcheckMainVo.prpLCheckFees}" varStatus="status">
					<tr>
						<input type="hidden" name="checkFeeVo[${status.index}].id" value="${checksFee.id}"/>
						<td><a href="javascript:void(0)" onclick="showRegistINfo('${checksFee.registNo }')" >${checksFee.registNo }</a></td>
						<td>${checksFee.policyNo }
						<c:if test="${!empty checksFee.policyNoLink}"> </br>${checksFee.policyNoLink}</c:if>
						</td>
						<td>${checksFee.claimNo }</td>
						<td>${checksFee.compensateNo }</td>
						<td><app:codetrans codeType="KindCode" codeCode="${checksFee.kindCode }" riskCode="${checksFee.riskCode }" /></td>
						<td>${checksFee.insurename }</td>
						<c:choose>
						<c:when test="${checksFee.taskStatus eq '2' }">
						<td>已退票</td>
						</c:when>
						<c:otherwise>
						<td>${checksFee.payAmount }</td>
						</c:otherwise>
						</c:choose>
						<td>${checksFee.taskDetail }</td>
						<td>
							<input class="input-text" type="text" name="checkFeeVo[${status.index}].amount" onchange="sumAmountFee()" value="${checksFee.amount}" datatype="amount" readonly="readonly"/>
						</td>
						<td>
						   <input class="input-text" type="text" name="checkFeeVo[${status.index}].remark"  value="${checksFee.remark}" datatype="*1-1000" onchange="checkSpecialCharactor(this)"/>
						</td>
					</tr>
				</c:forEach> 
			</tbody>
		</table>
		<div class="formtable ">
			<div class="row cl" >
				<label class="form_label col-1 col-offset-2">总费用金额</label>
				<div class="form_input col-3" >
					<input type="text" style="border: none" id="sumChargeFee" name="prpLAcheckMainVo.sumAmount" class="input-text"
						value="<fmt:formatNumber type="number" value="${prpLAcheckMainVo.sumAmount }" pattern="0.00" maxFractionDigits="2" />" readonly="readonly"/>
				</div>
				<div class="form_input col-6">
				<lable class="form_label col-3 col-offset-2"></lable> 
					<input class="btn btn-primary radius" id="EXButton" onclick="auditTakskExportExcel('${prpLAcheckMainVo.taskId}','${handlerStatus}')" type="button" value="导出" />
				</div>
				
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
function showRegistINfo(registNo){
	url="/claimcar/regist/edit.do?registNo="+registNo;
	openTaskEditWin("报案处理", url, null);
}



function auditTakskExportExcel(taskId,workStatus){
	//layer.alert(taskId+"------------"+id);
	window.open("/claimcar/checkfee/auditTakskExportExcel.do?taskId="+taskId+"&workStatus="+workStatus);
};

</script>