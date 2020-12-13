<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联共保分摊信息</title>
</head>
<body>
<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form">
						<input hidden="hidden" name="flowTaskId" id="flowTaskId" value="${flowTaskId}"/>
						<input hidden="hidden" name="handlerStatus" id="handlerStatus" value="${handlerStatus}"/>
						<c:if test="${(handlerStatus eq '0' || handlerStatus eq '2') && nodeCode ne 'VClaim'}">
						<div class="btn-footer clearfix ">
							<a class="btn btn-primary "  id="calculate">计算分摊金额</a>
						</div>
						</c:if>
						<div class="row  cl">
					 	 	<label class="form_label col-2">联共保标志</label>
					 	 	<div class="formControls col-3">
								<label><app:codetrans codeType="CoinsFlag" codeCode="1"  /> </label> 
							</div>
							<label class="form_label col-2">手续费计入方式</label>
					 	 	<div class="formControls col-3">
								<%-- <label><app:codetrans codeType="CalculateType" codeCode="${calculateType}"/></label> --%>
								<input type="text" class="input-text" value="${calculateType}"  />
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
							1321
									<%-- <c:forEach var="prpLCoinsVo" items="${prpLCoinsList}" varStatus="status"> --%>
									<c:forEach var="coinsVo" items="${prpLCoinsList}">
										<tr class="text-c">
										123123
										<h3>${coinsVo.policyNo }</h3> 
											<%-- <td><app:codetrans codeType="PayRefReason" codeCode="${prpLCoinsVo.payReason}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].payReason" value="${prpLCoinsVo.payReason}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].compensateNo" value="${prpLCoinsVo.compensateNo}"/>
												<input hidden="hidden" name="prpLCoins[${status.index }].policyNo" value="${prpLCoinsVo.policyNo}"/></td>
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
											<input type="text" class="input-text" name="prpLCoins[${status.index }].shareAmt"
												value="${prpLCoinsVo.shareAmt}" datatype="amount" readonly />
											</td> --%>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
						<div class="btn-footer clearfix text-c">
						 <a class="btn btn-danger cl" id="close">关闭</a>
						</div>
					</form>
				</div>
			</div>

		</div>
</div>
		<script type="text/javascript">
		
		
		$("#calculate").click(function() {
			$("input[name$='shareAmt']").each(function(){
				var elementName = $(this).name;
				var index = elementName.split("[")[1].split("]")[0];
				$("#prpLCoinsVo["+index+"].shareAmt").val($(this).val());
			});
			//保存
			var formData = $("#form").serialize();
			$.ajax({
	            type: "POST",
	            url: "/claimcar/survey/save.do",
	            data:formData,
	            async: false,
	            success: function(data){
	            	if(data.status==200){
	            		layer.confirm("调查发起成功！", {
	        				btn: ['确定'] //按钮
	        			}, function(index){
	        				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			            	parent.layer.close(index); //再执行关闭 
	        			});
	            	}else{
	            		layer.alert(data.data);
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
			//var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			//parent.layer.close(index); //再执行关闭 
			window.close();
		});
		</script>
</body>
</html>