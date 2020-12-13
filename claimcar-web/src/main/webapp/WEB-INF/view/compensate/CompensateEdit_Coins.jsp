<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>联共保信息</title>
</head>
<body>
<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form">
						<input hidden="hidden" name="flowTaskId" id="flowTaskId" value="${flowTaskId}"/>
						<input hidden="hidden" name="handlerStatus" id="handlerStatus" value="${handlerStatus}"/>
						<input hidden="hidden" name="isCompensateNo" id="isCompensateNo" value="${isCompensateNo}"/>
						<c:if test="${(handlerStatus eq '0' || handlerStatus eq '2') && nodeCode ne 'VClaim'}">
						<div class="btn-footer clearfix ">
							<a class="btn btn-primary "  id="calculate">计算分摊金额</a>
						</div>
						</c:if>
						<div class="row  cl">
					 	 	<label class="form_label col-2">联共保标志</label>
					 	 	<div class="formControls col-3">
								<app:codetrans codeType="CoinsFlag" codeCode="${coinsFlag}"  />
							</div>
							<label class="form_label col-2">手续费计入方式</label>
					 	 	<div class="formControls col-3">
								<label><app:codetrans codeType="CalculateType" codeCode="${calculateType}"/></label>
							</div>
					 	</div>
						<div class="table_wrap">
						<div class="table_cont ">
							<table class="table table-border table-hover">
								<thead>
									<tr class="text-c">
										<th>赔付类型</th>
										<th>联/共保身份</th>
										<th>是否首席</th>
										<th>联/共保人机构代码</th>
										<th>联/共保人名称</th>
										<th>联/共保份额（%）</th>
										<th>币别</th>
										<th>分摊金额</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="prpLCoinsVo" items="${prpLCoinsList}" varStatus="status">
										<tr class="text-c">
												<input hidden="hidden" name="prpLCoins[${status.index }].payReason" value="${prpLCoinsVo.payReason}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].compensateNo" value="${prpLCoinsVo.compensateNo}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].policyNo" value="${prpLCoinsVo.policyNo}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].coinsFlag" value="${prpLCoinsVo.coinsFlag}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].calculateType" value="${prpLCoinsVo.calculateType}"/>
											<td>
											<app:codetrans codeType="PayRefReason" codeCode="${prpLCoinsVo.payReason}"/>
											</td>
											<td><app:codetrans codeType="CoinsType"  codeCode="${prpLCoinsVo.coinsType}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].coinsType" value="${prpLCoinsVo.coinsType}"/></td>
											<td><app:codetrans codeType="ChiefFlag"  codeCode="${prpLCoinsVo.chiefFlag}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].chiefFlag" value="${prpLCoinsVo.chiefFlag}"/></td>
											<td>${prpLCoinsVo.coinsCode}
												<input hidden="hidden" name="prpLCoins[${status.index }].coinsCode" value="${prpLCoinsVo.coinsCode}"/></td>
											<td>${prpLCoinsVo.coinsName}
												<input hidden="hidden" name="prpLCoins[${status.index }].coinsName" value="${prpLCoinsVo.coinsName}"/></td>
											<td>${prpLCoinsVo.coinsRate}
												<input hidden="hidden" name="prpLCoins[${status.index }].coinsRate" value="${prpLCoinsVo.coinsRate}"/></td>
											<td>${prpLCoinsVo.currency}
												<input hidden="hidden" name="prpLCoins[${status.index }].currency" value="${prpLCoinsVo.currency}"/></td>
											<td>
											<c:choose>
											<c:when test="${(handlerStatus eq '0' || handlerStatus eq '2') && nodeCode ne 'VClaim'}">
											<input type="text" class="input-text" name="[${status.index}].shareAmt" 
												datatype="amount" readonly />
												<input hidden="hidden" name="prpLCoins[${status.index }].shareAmt" value="${prpLCoinsVo.shareAmt}"/>
											</c:when>
											<c:otherwise>
											<input type="text" class="input-text" name="prpLCoins[${status.index }].shareAmt"
												value="${prpLCoinsVo.shareAmt}" datatype="amount" readonly />
											</c:otherwise>
											</c:choose>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
						<div class="btn-footer clearfix text-c">
						 <a class="btn btn-primary ml-5" id="close">关闭</a>
						</div>
					</form>
				</div>
			</div>

		</div>
</div>
		<script type="text/javascript">
		
		$("#calculate").click(function() {
			var isCompensateNo = $("#isCompensateNo").val();
			if(isCompensateNo!="1"){
				layer.alert("没有计算书不能计算分摊金额");
				return false;
			}
			$("input[name$='shareAmt']").each(function(){
				var elementName = $(this).prop("name");
				var index = elementName.split("[")[1].split("]")[0];
				$("input[name='["+index+"].shareAmt']").val($(this).val());
			});
			//保存
			var formData = $("#form").serialize();
			$.ajax({
	            type: "POST",
	            url: "/claimcar/compensate/saveCoins.ajax",
	            data:formData,
	            async: false,
	            success: function(data){
	            	if(data.status==200){
	        			var sumPaidAmt = $("input[name='prpLCompensate.sumPaidAmt']").val();
	        			var sumPaidFee = $("input[name='prpLCompensate.sumPaidFee']").val();
	        			sumPaidAmtBefore = sumPaidAmt;
	        			sumPaidFeeBefore = sumPaidFee;
	        			$("#coinsSize").val(1);
	            		layer.msg("保存成功");
	            	}            	
	            },
		 		error: function (XMLHttpRequest, textStatus, errorThrown) {
		 			if(XMLHttpRequest.responseText!=""){
						layer.alert(XMLHttpRequest.responseText);
					}else{
						layer.alert("操作失败，请刷新页面！");
					}
					layer.closeAll('loading');
		 		}
	        });
		});
		
		$("#close").click(function() {
			layer.closeAll("page");
		});
		</script>
</body>
</html>