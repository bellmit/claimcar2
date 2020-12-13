<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>账户信息展示页面</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<div class="table_cont">
			<form id="accountForm" role="form" method="post" name="fm">
			<input type="hidden"  name="payBankVo.id"  value="${payBankVo.id }" />
			<input type="hidden"  name="payBankVo.compensateNo"  value="${payBankVo.compensateNo }" />
			<input type="hidden"  name="compensateNo"  value="${compensateNo }" />
			<input type="hidden"  name="payBankVo.policyNo"  value="${payBankVo.policyNo }" />
			<input type="hidden"  name="payBankVo.registNo"  value="${payBankVo.registNo }" />
			<input type="hidden"  name="payBankVo.claimNo"  value="${payBankVo.claimNo }" />
			<input type="hidden"  name="payBankVo.lossType"  value="${payBankVo.lossType }" />
			<input type="hidden"  name="payBankVo.payeeId"  value="${payBankVo.payeeId }" />
			<input type="hidden"  name="payBankVo.insuredName"  value="${payBankVo.insuredName}" />
			<input type="hidden"  name="payBankVo.chargeCode"  value="${payBankVo.chargeCode}" />
			<input type="hidden"  name="payBankVo.payType"  value="${payBankVo.payType}" />
			<input type="hidden"  name="handleStatus" id="handleStatus" value="${handleStatus}" />
			<input type="hidden"  id="accountId" name="originalAccountNo"  value="${accountId}" />
			<input type="hidden"  name="payBankVo.errorType" value="${payBankVo.errorType }" />
			<input type="hidden"  name="payBankVo.isAutoPay"  value="${payBankVo.isAutoPay}" />
			<input type="hidden"  name="payBankVo.serialNo" value="${payBankVo.serialNo}" />
			<input type="hidden"   id="status" value="${status}" />
			<input type="hidden"  id="modFlag"  value="${modFlag }" />
			<input type="hidden"  id="isVerify" name="payBankVo.isVerify"  value="${isVerify}" />
			<input type="hidden"  id="verifyStatus" value="${payBankVo.verifyStatus}" />
				<div class="table_wrap">
					<div class="table_title f14">理赔账户信息修改</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
							    <label class="form_label col-2">申请人：</label>
								<div class="form_input col-2">
									<app:codetrans codeType="UserCode" codeCode="${appUser }"/>
								</div>
								<label class="form_label col-2">申请时间：</label>
								<div class="form_input col-2">
								    <fmt:formatDate value="${appTime}" pattern="yyyy-MM-dd" />
								</div>
								<label class="form_label col-2">支付失败原因：</label>
								<div class="form_input col-2">${errorMessage }</div>
							</div>
							<table border="1" class="table table-border">
								<thead class="text-c">
									<tr>
										<th>收付原因</th>
										<th>业务号</th>
										<th>计算书号</th>
										<th>保单号</th>
										<th>立案号</th>
										<th>被保险人名称</th>
										<th>被保险人代码</th>
										<th>核赔通过时间</th>
									</tr>
								</thead>
								<tbody class="text-c">
									<c:forEach var="payBank" items="${payBankVoList}" varStatus="status">
										<tr>
											<input type="hidden"  id="PayRefReason" value="${payBank.payType}" />
											<td><app:codetrans codeType="PayRefReason" codeCode="${payBank.payType }"/></td>
											<td>${payBank.settleNo }</td>
											<td>${payBank.compensateNo }</td>
											<td>${payBank.policyNo }</td>
											<td>${payBank.claimNo }</td>
											<td>${payBank.insuredName }</td>
											<td>${payBank.insuredCode }</td>
											<td><fmt:formatDate value="${payBank.vClaimTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="text-c mt-10">
				<c:choose>
				<c:when test="${status eq '0' or isVerify eq '1'}">
				    <input class="btn btn-primary" onclick="payCustomView()" id="look" type="button" value="查看银行信息" />
				</c:when>
				<c:otherwise>
					<input class="btn btn-primary ml-5" id="mod" onclick="modifiAccInfo()" value="修改银行信息" />
				</c:otherwise>
				</c:choose>
				</div>
				<div class="text-c mt-10">
				</div>
				<!-- 银行信息界面 -->
				<div style="display:none" id="BankInfo">
					<%@include file="AccountInfoEdit_BankModi.jsp"%>
				</div>
				<!-- 行号查询界面 -->
				<div style="display:none" id="BankNumQ">
					<%@include file="AccountInfoEdit_BankModi_Num.jsp"%>
				</div>
				
				<!-- 审核意见开始  -->
				<c:if test="${isVerify eq '1'}">
					<div class="table_title f14">
						审核意见<font class="must">*</font>
					</div>
					<div class="row cl">
						<textarea class="textarea h100" maxlength="250"
							name="payBankVo.verifyText" datatype="*1-200" 
							nullmsg="请输入审核意见！" placeholder="请输入...">${payBankVo.verifyText}</textarea>
					</div>
					<div class="row cl text-c" id="ceshi">
						<br />
						<input class="btn btn-primary" type="button" onclick="accountSubmit('0')" value="暂存" />
						<input class="btn btn-primary" type="button" onclick="accountSubmit('1')" value="审核通过" />
						<input class="btn btn-primary" type="button" onclick="accountSubmit('2')" value="退回" />
						<input type="hidden" name="payBankVo.verifyHandle" value=""/>
					</div>
				</c:if>
				<!-- 审核意见结束  -->
			</form>
		</div>
		
    <script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
		function accountSubmit(params){//提交
			$("input[name='payBankVo.verifyHandle']").val(params);
			$("#accountForm").submit();
		}	
	
		$(function() {
			init();
			ajaxEdit = new AjaxEdit($('#accountForm'));
//			ajaxEdit.targetUrl = "/claimcar/payCustom/savePayCustomInfo.do";
			ajaxEdit.targetUrl = "/claimcar/accountInfo/submitAccountVerify.do";
			ajaxEdit.afterSuccess = after;
			
			// 绑定表单
			ajaxEdit.bindForm();
		});
		
		function init(){
			var status = $("#status").val();
			var isVerify = $("#isVerify").val();
			
			var verifyStatus = $("#verifyStatus").val();
			if(verifyStatus=="2"||verifyStatus=="3"){//审核已完成处理
				$("textarea[name='payBankVo.verifyText']").attr("readonly","readonly");
				$("#ceshi").hide();
			}else{
				$("textarea[name='payBankVo.verifyText']").removeAttr("readonly");
				$("#ceshi").show();
			}
		}
		
		function after(result){//返回函数
			var payeeId = eval(result).data;
		    var success = eval(result).status;
			var text = eval(result).statusText;
			if(success == "200"){
				var handle = $("input[name='payBankVo.verifyHandle']").val();
				if(handle=="0"){
					layer.alert("暂存成功！");
				}else{
					var msg = "已完成审核！";
					if(handle=="2"){
						msg = "已退回申请，请处理！";
					}
					layer.confirm(text+msg, {
						closeBtn: 0,btn: ['确定'] // 按钮
					}, function(index){
						$("#verifyStatus").val("2");
						var compensateNo = $("input[name='compensateNo']").val();
						var handleStatus = $("input[name='handleStatus']").val();
						var registNo = $("input[name='payBankVo.registNo']").val();
						var isVerify = $("#isVerify").val();
						var payType = $("input[name='payBankVo.payType']").val();
						var accountId = $("#accountId").val();
						window.location.href= "/claimcar/accountInfo/accountInfoInit.do?compensateNo="+compensateNo+"&handleStatus="
						+handleStatus+"&registNo="+registNo+"&payeeId="+payeeId+"&isVerify="+isVerify+"&payType="+payType+"&oldAccountId="+accountId;
						//window.location.reload();// 刷新父窗口
					});
				}
			}
		}

		function modifiAccInfo() {
			var payeeId = $("input[name='payBankVo.payeeId']").val();
			var flag = "N";
			var registNo = $("input[name='payBankVo.registNo']").val();
			var nodeCode = "";
			var compensateNo = $("input[name='compensateNo']").val();
			var accountId = $("#accountId").val();
			var chargeCode = $("input[name='payBankVo.chargeCode']").val();
			var payType = $("input[name='payBankVo.payType']").val();
			var isAutoPay = $("input[name='payBankVo.isAutoPay']").val();
			var errorType = $("input[name='payBankVo.errorType']").val();
			var payRefReason = $("#PayRefReason").val();
			var serialNo = $("input[name='payBankVo.serialNo']").val();
			paycustom_url = "/claimcar/accountInfo/payCustomEdit.do?registNo="
					+ registNo + "&flag=" + flag + "&nodeCode=" + nodeCode + ""
					+ "&payId=" + payeeId + "&compensateNo=" + compensateNo
					+ "&accountId=" + accountId +"&chargeCode=" + chargeCode 
					+ "&payType=" + payType +"&isAutoPay=" + isAutoPay
					+ "&errorType=" +errorType
					+ "&payRefReason=" + payRefReason
					+ "&serialNo=" + serialNo;
			var title = '收款人账户信息维护';
			layer.open({
				type : 2,
				title : title,
				shade : false,
				area : [ '85%', '80%' ],
				content : paycustom_url,
				end : function() {
					window.location.reload();
				}
			});

		}

		function layerShowBankNum(title, id) {//弹出检索行号页面

			Prov = $("#checkBankProvCity_lv1  option:selected").text();
			City = $("#intermBankProvCity_lv2  option:selected").text();
			Bname = $("#BankName  option:selected").text();
			BQuery = $("#BankQueryText").val();

			$("#Province").val(
					$("#intermBankProvCity_lv1  option:selected").val());
			$("#City").val($("#intermBankProvCity_lv2  option:selected").val());

			if (Prov == "" || Bname == "" || City == "") {
				layer.msg("请完善开户省市及银行信息！");

			} else {
				document.getElementById("NumProv").innerText = Prov;
				document.getElementById("NumCity").innerText = City;
				document.getElementById("NumBankName").innerText = Bname;
				$("#NumBankQueryT").val(BQuery);
				index2 = layer.open({
					type : 1,
					title : '检索行号',
					area : [ '900px', '350px' ],
					fix : true, //不固定
					scrollbar : false,
					maxmin : false,
					content : $("#" + id)
				});
			}
		};

		function getBankNumTab(event, bankNumber, bankOutlets) {//点击选中行号信息行

			$($(event).children()[0]).children().each(function() {
				if (this.type == "radio") {
					if (!this.checked) {
						this.checked = true;
					} else {
						this.checked = false;
					}
				}
			});
			$("#NumBankQueryT").val(bankOutlets);
			BankNumber = bankNumber;
			BankOutlets = bankOutlets;
			//alert(BankNumber);
		};

		function layerHiddenBN() {//行号检索界面关闭			
			//回传参数到银行信息界面
			$("#BankQueryText").val("请输入检索名称");
			$("#BankNumber").val(BankNumber);
			$("#BankOutlets").val(BankOutlets);
			layer.close(index2);
		}
		function layerHiddenBank() {//银行信息界面点击“保存”关闭	
			//发送保存数据请求
			//关闭窗口
			layer.close(mIndex);
		}
		function layerCancelBank() {//点击取消关闭银行信息界面并清空输入信息
			layer.close(mIndex);
		}
		function saveAcc() {
			var params = $("#form").serialize();
			$.ajax({
				url : "/claimcar/accountInfo/saveData.do", // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(result) {// 回调方法，可单独定义
					if (result.status == "200") {
						messageText = '操作成功';
						layer.msg(messageText);
						$("#save").hide();
						$("#mod").hide();
					} else {
						success = false;
						messageText = '操作失败：' + result.statusText;
						layer.alert(messageText);
					}

					/* if(jsonData.getStatusText == "保存成功！"){
						 layer.msg(jsonData.getStatusText);
						 $("#save").hide();
						 $("#mod").hide(); 
					}else{
						 layer.msg("操作失败，请刷新页面！");
					} */

				}
			});

		}
		function payCustomView() {
			var payeeId = $("input[name='payBankVo.payeeId']").val();
			var registNo = $("input[name='payBankVo.registNo']").val();
			var compensateNo = $("input[name='compensateNo']").val();
			var payType = $("input[name='payBankVo.payType']").val();
			var url = "/claimcar/accountInfo/payCustomView.do?payeeId="
					+ payeeId+"&registNo="+registNo+"&compensateNo="+compensateNo+"&payType="+payType;
			var title = "查看原银行信息";
			layer.open({
				type : 2,
				title : title,
				shade : false,
				area : [ '1100px', '500px' ],
				content : url,
				end : function() {
				}
			});
		}
	</script>
</body>

</html>