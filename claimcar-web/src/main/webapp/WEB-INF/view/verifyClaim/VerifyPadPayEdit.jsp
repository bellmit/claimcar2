<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>垫付申请核赔处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
		    <a class="btn  btn-primary">保单详细信息</a>
		    <a class="btn  btn-primary">报案详细信息</a>
		    <a class="btn  btn-primary">查勘详细信息</a>
		    <a class="btn  btn-primary">定损详细信息</a>
		    <a class="btn  btn-primary">人伤跟踪信息</a>
		    <a class="btn  btn-primary">历次审核意见</a>
		    <a class="btn  btn-primary">单证上传</a>
		    <a class="btn  btn-primary">诉讼</a>
		    <a class="btn  btn-primary">查看估损更新轨迹</a>
		    <a class="btn  btn-primary" id="checkRegistMsg">案件备注</a>
		    <a class="btn  btn-primary mt-5" onclick="payCustomOpen('N')">收款人信息维护</a>
		    <a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
		    </div><br/>
		</div>
		<br />
		<p>
		<div class="table_cont">
			<form id="defossform" role="form" method="post" name="fm">
				<!-- 核赔基本信息 -->
	        	<%@include file="VerifyClaimEdit_VerifyInfo.jsp" %>
				<div class="table_wrap">
					<div class="table_title f14">基本信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-1">立案号</label>
								<div class="form_input col-3">3078686868686786767767</div>
								<label class="form_label col-1">报案号</label>
								<div class="form_input col-3">2794732947947392747</div>
								<label class="form_label col-1">保单号</label>
								<div class="form_input col-3">37598437932742972947</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">被保险人</label>
								<div class="form_input col-3">测试人员dan</div>
								<label class="form_label col-1">归属机构</label>
								<div class="form_input col-3">广州本部4S店总汇</div>
								<label class="form_label col-1">车牌号码</label>
								<div class="form_input col-3">粤AD3432</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">号牌底色</label>
								<div class="form_input col-3">蓝色</div>
								<label class="form_label col-1">出险日期</label>
								<div class="form_input col-3">2015年07月11日</div>
								<label class="form_label col-1">出险地点</label>
								<div class="form_input col-3">广州市天河北路</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">交强责任类型</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" placeholder="预付类型自定义标签" />
								</div>
								<label class="form_label col-2">交警联系电话</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="" value="" />
								</div>
								<label class="form_label col-2">通知书发出单位</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="" value="" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">交警通知书编号</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="" value="" />
								</div>
								<label class="form_label col-2">通知日期</label>
								<div class="form_input col-2">
									<input type="text" class="Wdate"  id="dgDateMin" name=" " 
										onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" style="width:97%"/>
								</div>
								<label class="form_label col-2">交警姓名</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="" value="" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">是否发起追偿</label>
								<div class="form_input col-3">
									<app:codeSelect codeType="YN10" type="radio" name="" value=""/>
								</div>
							</div>
						</div>
					</div>
				</div>
			
				
				<div class="table_wrap">
				<div class="table_title f14">人员损失信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="table_cont">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th width="10%">
										<!-- 点击增加、删除按钮的时候，人员损失信息的两张表同时增加同时删除 -->
											<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addPayInfo('PersLossTbody','plSize')"></button>
										</th>
										<th>姓名<font class="must">*</font></th>
										<th>人伤类型<font class="must">*</font></th>
										<th>伤情类型</th>
										<th>年龄</th>
										<th>性别<font class="must">*</font></th>
										<th>单位/住址</th>
										<th>车牌号</th>
										<th>医疗机构</th>
										<th>身份证号</th>
									</tr>
								</thead>
								<tbody class="text-c" id="PersLossTbody">
									<!-- <input type="hidden" id="plSize" value="${fn:length(prpLTmpCItemKindVos)}"> -->
									
								</tbody>
								</table>
							</div>
							<p>
							<div class="table_cont">
							<table class="table table-bordered table-bg">
								<thead class="text-c">
									<tr>
										<th>费用名称</th>
										<th>险别</th>
										<th>垫付抢救费用金额<font class="must">*</font></th>
										<th>收款人<font class="must">*</font></th>
										<th>例外标志</th>
										<th>例外原因</th>
										<th>收款方帐号<font class="must">*</font></th>
										<th>开户银行<font class="must">*</font></th>
									</tr>
								</thead>
								<tbody class="text-c" id="PersLossPeeTbody">
									
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				
				<div class="table_wrap">
				<div class="table_title f14">备注信息</div>
					<div class="formtable">
						<div class="col-12">
								<input type="textarea" class="textarea">
						</div>
					</div>
				</div>
			<!-- 审批意见 -->
            <%@include file="VerifyClaimEdit_VerifyAdvice.jsp" %>

			</form>
		</div>
		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="save" onclick="save('submitLoss')" type="submit" value="冲销"> 
			<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="取消">
		</div>
		<!-- 案件备注功能隐藏域 -->
		<input type="hidden" id="nodeCode" value="DLCar"> 
		<input type="hidden" id="registNo" value="${param.registNo}"> 
		<input type="hidden" id="flag" value="${flag }">
	</div>

	<script type="text/javascript">
		$(function() {

		});

		//新增费用信息行
		var tabSize = 0;
		function addPayInfo(tbodyName, sizeName) {
			var $tbody = $("#" + tbodyName + "");
			var params = {
					"flag" : '1',
					"trIndex" : tabSize,
			};
			var url = "/claimcar/verifyClaim/addPersRow.ajax";
			$.post(url, params, function(result) {
				$tbody.append(result);
				tabSize = tabSize + 1;
				$("#" + sizeName + "").val(tabSize);
			});
			var params = {
					"flag" : '2',
					"trIndex" : tabSize,
				};
			$.post(url, params, function(result) {
				$("#PersLossPeeTbody").append(result);
			});
		}
		//删除信息行
		function delPayInfo(element, sizeName) {
			var $parentTr = $(element).parent().parent();
			var trIndex = $parentTr.attr('id');
			//获取下一表格中索引值和被删除行索引相同的对应的行并删除
			$("#PersLossPeeTbody").find("tr").each(function(){
				if($(this).attr('id')==trIndex){
					$(this).remove();
				}
			});
			tabSize = tabSize - 1;
			$parentTr.remove();
		}
	</script>
</body>

</html>