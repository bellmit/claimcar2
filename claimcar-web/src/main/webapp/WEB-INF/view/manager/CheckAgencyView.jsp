<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 公估机构显示 -->
<!DOCTYPE HTML>
<html>
<head>
<title>查看查勘机构信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<!-- 公估信息  开始 -->
	<div class="table_wrap">
		<div class="table_cont mt-30 md-30">
			<div class="formtable">
				<div class="row cl">
					<label class="form_label col-3">归属机构：</label>
					<div class="form_input col-2">
						<c:set var="comCode">
							<app:codetrans codeType="ComCode" codeCode="${prpdIntermMainVo.comCode}"/> 
						</c:set>
						<span>${comCode}</span>
					</div>

					<label class="form_label col-2">查勘机构代码：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.intermCode}</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">查勘机构名称：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.intermName}</span>						
					</div>

					<label class="form_label col-2">上级机构名称：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.upperCode}</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">开户银行：</label>
					<div class="form_input col-2">
						<c:set var="bankName">
							<app:codetrans codeType="BankCode" codeCode="${prpdIntermBankVo.bankName}"/> 
						</c:set>
						<span>${bankName}</span>
					</div>
					<label class="form_label col-2">账号：</label>
					<div class="form_input col-2">
						<span>${prpdIntermBankVo.accountNo}</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">开户人名称：</label>
					<div class="form_input col-2">
						<span>${prpdIntermBankVo.accountName}</span>
					</div>
					<label class="form_label col-2">成立日期：</label>
					<div class="form_input col-2">
						<span>
							<fmt:formatDate value="${prpdIntermMainVo.establishDate}" pattern="yyyy-MM-dd"/>
						</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">总部住所：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.officeAddress}</span>
					</div>
					<label class="form_label col-2">联系人：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.linker}</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">邮政编码：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.postOde}</span>
					</div>
					<label class="form_label col-2">传真：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.fax}</span>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-3">固定电话：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.telNo}</span>
					</div>
					<label class="form_label col-2">是否有效：</label>
					<div class="form_input col-2">
						<c:choose>
							<c:when test="${prpdIntermMainVo.validStatus eq '1'}">
								<span>有效</span>
							</c:when>
							<c:otherwise>
								<span>无效</span>
							</c:otherwise>
						</c:choose>
					</div>
				</div>z
				
				<div class="row cl">
					<label class="form_label col-3">电子邮箱：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.email}</span>
					</div>
					<label class="form_label col-2">支付标准：</label>
					<div class="form_input col-2">
						<span>${prpdIntermMainVo.payStandard}</span>
					</div>
				</div>
				
				<!-- 公估服务资费表 -->
				<div class="row cl FeeStandard mt-15 col-8 col-offset-2 mb-15">
					<table class="table table-bordered table-bg">
						<thead>
							<tr>
								<th style="width:50%">查勘服务类型</th>
								<th>资费标准</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="prpdIntermServerVo" items="${prpdIntermServerVos}" varStatus="status">
								<tr>
									<td>
										<c:set var="serviceType">
											<app:codetrans codeType="ServiceType" codeCode="${prpdIntermServerVo.serviceType}"/> 
										</c:set>
										<span>${serviceType}</span>
									</td>
									<td>${prpdIntermServerVo.feeStandard}</td>
								</tr>	
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- 公估服务资费表 结束 -->

				<div class="formtable mt-5">
					<div class="row cl">
						<label class="form_label col-3">查勘内容分类：</label>
						<div class="form_input col-7">						
							<span>${prpdIntermMainVo.checkRemark}</span>
						</div>
					</div>
				</div>
				<div class="formtable mt-5">
					<div class="row cl">
						<label class="form_label col-3">备注：</label>
						<div class="form_input col-7">
							<span>${prpdIntermMainVo.remark}</span>							
						</div>
					</div>
				</div>
				<div class="table_wrap text-c">
					<a class="btn btn-primary" id="back">返回</a>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	$("#back").click(function(){
		var a = parent.window.no();
		parent.layer.close(a);// 执行关闭
	});
	</script>

</body>
</html>