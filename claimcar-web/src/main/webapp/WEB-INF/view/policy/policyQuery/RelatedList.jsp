<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>关联报案</title>
	</head>
	<body>
		<div>
			<div class="table_wrap">
            	<div class="table_title f14">提示：车牌号，被保险人，车型，车架号，发动机号任三项一致，或者车牌号，发动机号，VIN码/车架号，三项有两项相同，请确认是否关联报案</div>
				<div class="table_cont ">
					 <table class="table table-striped table-border table-hover">
					 		<thead class="text-c">
					 			<tr>
					 				<th>选择</th>
					 				<th>保单号</th>
					 				<th>车牌号</th>
					 				<th>被保险人</th>
					 				<th>承保机构</th>
					 				<th>车架号</th>
					 				<th>发动机号</th>
					 				<th>起保日期</th>
					 				<th>终保日期</th>
					 				<th>是否有效</th>
					 				<th>保单类型</th>
					 			</tr>
					 		</thead>
					 		<tbody class="text-c">
					 			<c:forEach var="policyInfoVo" items="${policyInfoVoList}" varStatus="status">
					 				<tr>
					 					<td>
					 						<c:if test="${policyInfoVo.validFlag eq 1 }">
						 						<input name="checkCode" checked="checked" id="checkCode${status.index}" group="ids" type="checkbox" 
						 						onclick="//checkPolicy();" value="${policyInfoVo.policyNo}" />
					 						</c:if>
					 						<c:if test="${policyInfoVo.validFlag ne 1 }">
					 							<input name="checkCode" id="checkCode${status.index}" group="ids" type="checkbox" 
					 							disabled="disabled"
					 						onclick="//checkPolicy();" value="${policyInfoVo.policyNo}" />
					 						</c:if>
					 					</td>
					 					<td>
					 						${policyInfoVo.policyNo}
					 						<input type="hidden" class="input-text ready-only" name  value="${policyInfoVo.policyNo}" />
					 					</td>
					 					<td>
					 						${policyInfoVo.licenseNo}
					 						<input type="hidden" class="input-text ready-only"  value="${policyInfoVo.licenseNo}" />
					 					</td>
					 					<td>
					 						${policyInfoVo.insuredName}
					 						<input type="hidden" class="input-text ready-only"  value="${policyInfoVo.insuredName}" />
					 					</td>
					 					<td>
					 						
					 						<app:codetrans codeType="ComCode" codeCode="${policyInfoVo.comCode}"/>
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.comCode}" />
					 					</td>
					 					<td>
					 						${policyInfoVo.frameNo}
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.frameNo}" />
					 					</td>
					 					<td>
					 						${policyInfoVo.engineNo}
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.engineNo}" />
					 					</td>
					 					<td>
					 						<fmt:formatDate value='${policyInfoVo.startDate}'  pattern='yyyy-MM-dd' />
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.startDate}" />
					 					</td>
					 					<td>
					 						<fmt:formatDate value='${policyInfoVo.endDate}'  pattern='yyyy-MM-dd' />
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.endDate}" />
					 					</td>
					 					<td>
					 						<c:choose>
					 							<c:when test="${policyInfoVo.validFlag eq 2}">未起保</c:when>
					 							<c:when test="${policyInfoVo.validFlag eq 1}">有效</c:when>
					 							<c:when test="${policyInfoVo.validFlag eq 0}">无效</c:when>
					 						</c:choose>
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.validFlag}" />
					 					</td>
					 					<td>
					 						${policyInfoVo.riskType}
					 						<input type="hidden" class="input-text ready-only" readonly="readonly" value="${policyInfoVo.riskType}" />
					 					</td>
					 				</tr>
					 				<!-- 
									<tr>
										<td>
											<input type="button" title="删除" onclick="delSubRisk(this)" name="subRiskVo_${status.index + size }" class="btn btn-default radius" value="-" />
										</td>
										<td>
											<input type="text" class="input-text ready-only" readonly="readonly" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].kindName" value="${carSubRiskVo.kindName }"/>
											<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].id" value="${carSubRiskVo.id }"/>
											<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].kindCode" value="${carSubRiskVo.kindCode }"/>
											<input type="hidden" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].registNo" value="${carSubRiskVo.registNo }"/>
										</td>
										<td>
											<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].count" value="${carSubRiskVo.count }"/>
										</td>
										<td>
											<input type="text" class="input-text" name="lossCarMainVo.prpLDlossCarSubRisks[${status.index + size  }].subRiskFee" value="${carSubRiskVo.subRiskFee }"/>
										</td>
									</tr> 
									-->
								</c:forEach>
					 		</tbody>
					 </table>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			function checkPolicy() {
				var policyNo = "";
				var selectedIds = getSelectedIds();
				if(selectedIds=="") {
					layer.alert("请选择保单信息");
					return false;
				}
				if(selectedIds.split(",").length>1) {
					layer.alert(onlyRecord);
					return false;
				}
				var idArray = selectedIds.split(",");
				alert("idArray.length:"+idArray.length);
				for (var i = 0; i < idArray.length; i++) {
					var id = idArray[i];
					var idA=id.split("&");
					alert("idA[0]:"+idA[0]);
					policyNo = policyNo + idA[0] + ",";
				}
				$("#policyNoIn").val(policyNo);
			}
		</script>
	</body>
</html>