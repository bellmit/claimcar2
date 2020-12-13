<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>预付（冲销）核赔处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<a class="btn  btn-primary">保单详细信息</a> 
			<a class="btn  btn-primary">报案详细信息</a>
			<a class="btn  btn-primary">单证上传</a> 
			<a class="btn  btn-primary">诉讼</a>
			<a class="btn  btn-primary" id="createRegistMessage(registNo,nodeCode)">案件备注</a> 
			<a class="btn  btn-primary">打印预付申请书</a> 
			<a class="btn  btn-primary">预付注销</a>
			<a class="btn  btn-primary" onclick="payCustomOpen('N')">收款人信息维护</a>
			<a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		</div>
		<br />
		<p>
		<div class="table_cont">
			<form id="defossform" role="form" method="post" name="fm">
			<!-- 核赔基本信息 -->
        	<%@include file="VerifyClaimEdit_VerifyInfo.jsp" %>
				
				<div class="table_wrap">
					<div class="table_title f14">预付基本信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-1">预付号</label>
								<div class="form_input col-3"> </div>
								<label class="form_label col-1">立案号</label>
								<div class="form_input col-3">6019373957935798142342</div>
								<label class="form_label col-1">预付类型</label>
								<div class="form_input col-3"><input type="text" class="input-text" placeholder="预付类型自定义标签" /></div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">本次预付金额合计</label>
								<div class="form_input col-3">1200.00</div>
								<label class="form_label col-1">已预付金额</label>
								<div class="form_input col-3">0.00</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="table_wrap">
				<div class="table_title f14">预付赔款</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="table_con">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="10%">
											<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPayInfo('IndemnityTbody')"></button>
										</th>
										<th>损失险别</th>
										<th>预付款项类型</th>
										<th>姓名<font class="must">*</font></th>
										<th>车牌号码</th>
										<th>预付金额<font class="must">*</font></th>
										<th>收款人</th>
										<th>例外标志</th>
										<th>例外原因</th>
										<th>收款人帐号<font class="must">*</font></th>
										<th>开户银行<font class="must">*</font></th>
									</tr>
								</thead>
								<tbody class="text-c" id="IndemnityTbody">
									<!-- <input type="hidden" id="indeSize" value="${fn:length(prpLTmpCItemKindVos)}"> -->
								</tbody>
							</table>
							</div>
						</div>
					</div>
				</div>
				
				<div class="table_wrap">
				<div class="table_title f14">预付费用</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="table_con">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="10%">
											<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPayInfo('FeeTbody')"></button>
										</th>
										<th>损失险别</th>
										<th>费用名称</th>
										<th>费用金额<font class="must">*</font></th>
										<th>收款人</th>
										<th>例外标志</th>
										<th>例外原因</th>
										<th>收款人帐号<font class="must">*</font></th>
										<th>开户银行<font class="must">*</font></th>
									</tr>
								</thead>
								<tbody class="text-c" id="FeeTbody">
									<!-- <input type="hidden" id="feeSize" value="${fn:length(prpLTmpCItemKindVos)}"> -->
								</tbody>
							</table>
							</div>
						</div>
					</div>
				</div>
			<!-- 审批意见 -->
            <%@include file="VerifyClaimEdit_VerifyAdvice.jsp" %>
			</form>
		</div>
		<div class="text-c">
			<br /> <input class="btn btn-primary " id="pend" onclick="save('save')" type="submit" value="暂存"> 
			<input class="btn btn-primary ml-5" id="save" onclick="save('submitLoss')" type="submit" value="提交"> 
			<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="取消">
		</div>
		<!-- 案件备注功能隐藏域 -->
		<input type="hidden" id="nodeCode" value="PrePay"> 
		<input type="hidden" id="registNo" value="${param.registNo}"> 
		<input type="hidden" id="flag" value="${flag }">
	</div>

	<script type="text/javascript">
		$(function() {

		});

		//新增费用信息行
		function addPayInfo(tbodyName) {
			var $tbody = $("#" + tbodyName + "");
			//var size = $("#" + sizeName + "").val();
			//var flag = $("#flag").val();
			var params = {
				//"size" : size,
				"bodyName" : tbodyName,
				//"flag" : flag,
			};
			var url = "/claimcar/verifyClaim/addPreRow.ajax";
			$.post(url, params, function(result) {
				$tbody.append(result);
				//$("#" + sizeName + "").val(size + 1);
			});
		}
		//删除信息行
		function delPayInfo(element, sizeName) {
			var $parentTr = $(element).parent().parent();
			//var size = $("#" + sizeName + "").val();
			//$("#" + sizeName + "").val(size - 1);// 删除一条
			$parentTr.remove();
		}
	</script>
</body>

</html>