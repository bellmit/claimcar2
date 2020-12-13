<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>律师事务所信息维护</title>
</head>
<body>
	<form id="lawFirmEditForm">

		<div class="table_cont">
			<div class="table_wrap">
				<div class="formtable">
					<!-- 隐藏域 -->
					<input type="hidden" name="prpdLawFirmVo.id" value="${PrpdLawFirm.id}" /> 
					<input type="hidden" id="nodeCode" value="lawFirmNode" /> 
					<input type="hidden" id="payCustomId" name="prpdLawFirmVo.payCustomId" value="${payCustomId}" />
					<!-- 隐藏域 -->
					<div class="row cl">
						<label class="form_label col-2">律师事务所代码</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq 1}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmCode"
										value="${PrpdLawFirm.lawFirmCode}" datatype="*"
										readonly="readonly" maxlength="10"/>
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmCode"
										value="${PrpdLawFirm.lawFirmCode}" datatype="*" maxlength="10"/>
								</c:otherwise>
							</c:choose>
						</div>
						<span class="c-red col-1">*</span> <label class="form_label col-2">律师事务所名称</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq '1'}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmName"
										value="${PrpdLawFirm.lawFirmName }" datatype="*"
										readonly="readonly" />
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmName"
										value="${PrpdLawFirm.lawFirmName }" datatype="*"
										nullmsg="请填写律师事务所名称！" />
								</c:otherwise>
							</c:choose>

						</div>
						<span class="c-red col-1">*</span>

					</div>
					<div class="line mt-10 mb-10"></div>
					<div class="row cl">
						<label class="form_label col-2">律师事务所地址</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq '1'}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmAddress"
										value="${PrpdLawFirm.lawFirmAddress }" readonly="readonly" />
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.lawFirmAddress"
										value="${PrpdLawFirm.lawFirmAddress }" />
								</c:otherwise>
							</c:choose>
						</div>
						<label class="form_label col-3">律师事务所电话</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq '1'}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.mobileNo" value="${PrpdLawFirm.mobileNo}"
										readonly="readonly"  maxlength="30"/>
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.mobileNo" value="${PrpdLawFirm.mobileNo}" datatype="m|/^0\d{2,3}-\d{7,8}$/"  ignore="ignore"  maxlength="30"/>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="line mt-10 mb-10"></div>
					<div class="row cl">
                
						<label class="form_label col-2">负责人</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq '1'}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.principal"
										value="${PrpdLawFirm.principal }" readonly="readonly" />
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.principal"
										value="${PrpdLawFirm.principal}" />
								</c:otherwise>
							</c:choose>
						</div>
						<label class="form_label col-3">联系人</label>
						<div class="form_input col-2">
							<c:choose>
								<c:when test="${sign eq '1'}">
									<input type="text" class="input-text"
										name="prpdLawFirmVo.contacts" value="${PrpdLawFirm.contacts }"
										readonly="readonly" />
								</c:when>
								<c:otherwise>
									<input type="text" class="input-text"
										name="prpdLawFirmVo.contacts" value="${PrpdLawFirm.contacts }" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="line mt-10 mb-10"></div>
					<div class="row cl">
					<c:choose>
							<c:when test="${sign eq '1'}">
						            <input type="hidden" class="btn btn-primary"
									onclick="payCustomOpen('N','${PrpdLawFirm.payCustomId}')"
									value="银行信息" />
							</c:when>
							<c:when test="${sign eq '2'}">
							        <label class="form_label col-2">收款人信息</label>
						            <input type="button" class="btn btn-primary"
									onclick="payCustomOpenEdit()"
									value="银行信息" />
							</c:when>
							<c:otherwise>
								<label class="form_label col-2">收款人信息</label>
								<input type="button" class="btn btn-primary"
									onclick="payCustomSearchs('N')"
									value="银行信息" />
							</c:otherwise>
						</c:choose>

					</div>
					<div class="line mt-10 mb-10"></div>
				</div>
				<div class="btn-footer clearfix text-c">
					<c:choose>
						<c:when test="${sign eq '1'}">
							<input type="hidden" />
						</c:when>
						<c:otherwise>
							<a class="btn btn-primary" id="submit">保存</a>
						</c:otherwise>
					</c:choose>
				</div>

			</div>
		</div>

	</form>

	<script type="text/javascript">
		$("#submit").click(function() { //提交表单

			$("#lawFirmEditForm").submit();
		});

		$(function() {

			var ajaxEdit = new AjaxEdit($('#lawFirmEditForm'));
			ajaxEdit.targetUrl = "/claimcar/lawFirm/saveLawFirm.do";
			ajaxEdit.afterSuccess = function(result) {
				if (result.status == "200") {
				//	$("#submit").prop("disabled", false); //保存成功防止重复提交
				//document.getElementById("#submit").setAttribute("disabled", true);
				closeLayer();
				}
			};
			//绑定表单
			ajaxEdit.bindForm();
			

		});
		
		//保存成功后，关闭页面
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}
		
		function payCustomOpenEdit(){
			var url = "/claimcar/lawFirm/lawPayCustomEdit.do";
			var pIndex = null;
			if (pIndex == null) {
				pIndex = layer.open({
					type : 2,
					title : '收款人账户信息维护',
					shade : false,
					area : [ '100%', '100%' ],
					content : url,
					resize : true,
					end : function() {
						pIndex = null;
					}
				});
			}
		}
		
		//修改状态下，维护收款人信息
		function payCustomSearchs(flag){
 			// flag N-普通维护 Y-反洗钱信息补充 S-查勘环节（收款人类型固定并查询带入承保人信息）
            var url = "/claimcar/lawFirm/payCustomSearchList.do";
			var search_index=null;
			if (search_index == null) {//防止打开多个页面
				search_index = layer.open({
					type : 2,
					title : '收款人账户信息查询',
					shade : false,
					area : [ '90%', '90%' ],
					content : url,
					end : function() {
						search_index = null;
					}
				});
			}
		}
	</script>
</body>
</html>