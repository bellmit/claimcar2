<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>追偿任务处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		
			<div class="top_btn">
				<a class="btn btn-primary" onclick="viewEndorseInfo('${registNo}')">保单批改记录</a>
				<a class="btn btn-primary" onclick="viewPolicyInfo('${registNo}')">出险保单</a>
				<a class="btn  btn-primary" onclick="imageMovieUpload('${taskVo.taskId}')">信雅达影像上传</a>
				<a class="btn  btn-primary" onclick="openTaskEditWin('报案详细信息','/claimcar/regist/edit.do?flowId=${taskVo.flowId}&flowTaskId=${taskVo.taskId}&taskInKey=${taskVo.taskInKey}&handlerIdKey=${taskVo.handlerIdKey}&registNo=${taskVo.registNo}')">报案详细信息</a>
				<!-- <a class="btn  btn-primary">照片浏览</a> --> <a class="btn  btn-primary" onclick="openTaskEditWin('垫付详细信息','/claimcar/padPay/padPayTaskId.do?flowId=${taskVo.flowId}&flowTaskId=${taskVo.taskId}&taskInKey=${taskVo.taskInKey}&handlerIdKey=${taskVo.handlerIdKey}&registNo=${taskVo.registNo}')" id="padpay">垫付详细信息</a>
			</div>
		
		<p>
	
		<div class="table_cont">
			<form id="forms" role="form" method="post" name="fm">
			    <input type="hidden" name="prplReplevyMainVo.riskCode" id="riskCode" value="${riskCode }" />
				<input type="hidden" name="prplReplevyMainVo.id" id="fid"
					value="${prplReplevyMain.id }" /> <input type="hidden"
					name="flowTaskId" value="${flowTaskId }" /> <input type="hidden"
					name="handlerStatus" value="${handlerStatus}" /> <input
					type="hidden" name="replevyType" value="${replevyType}" />
				<div class="table_wrap">
					<div class="table_title f14">基本信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-1">追偿计算号</label> 
								<div class="form_input col-3">
								<input type="text" class="input-text" name="prplReplevyMainVo.compensateNo"
									value="${compensateNo}" style="background: transparent; border: none;"
									readonly="readonly" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">报案号</label>
								<div class="form_input col-3">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.registNo" value="${registNo}"
										style="background: transparent; border: none;"
										readonly="readonly" id="registNo" />
								</div>
								<label class="form_label col-1">保单号</label>
								<div class="form_input col-3">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.policyNo" value="${policyNo}"
										style="background: transparent; border: none;"
										readonly="readonly" />
								</div>
								<label class="form_label col-1">立案号</label>
								<div class="form_input col-3">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.claimNo" value="${claimNo}"
										style="background: transparent; border: none;"
										readonly="readonly" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">追偿操作员</label>
								<div class="form_input col-3">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.handlerCode" value="${userCode}"
										style="background: transparent; border: none;"
										readonly="readonly" />
								</div>
								<label class="form_label col-1">业务归属机构</label>
								<div class="form_input col-3">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.comCode" value="${comCode}"
										style="background: transparent; border: none;"
										readonly="readonly" />
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="table_wrap">
					<div class="table_title f14">追偿项目</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-1">追偿类型</label>
								<div class="form_input col-2">
									<span class="select-box"> <app:codeSelect 
											codeType="RecPayType" type="select"  
											name="prplReplevyMainVo.replevyType"
											value="${prplReplevyMain.replevyType}" onchange="showInput()"
											id="selectType" /><font class="must">*</font>
									</span>
								</div>
								<label class="form_label col-2">被追偿人名称</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.repleviedName"
										value="${prplReplevyMain.repleviedName }" datatype="*1-20"
										ignore="ignore" errormsg="请勿输入超过20个字！" />
								</div>
							<%-- 	<label class="form_label col-2">本次计划追偿金额</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.sumPlanReplevy" id="sumPlanReplevy"
										value="${prplReplevyMain.sumPlanReplevy }" readonly="readonly" 
										onchange="defaultFee()"/>
								</div> --%>
							</div>
							<div class="row cl">
								<label class="form_label col-1">追偿日期</label>
								<div class="form_input col-2">
									<input type="text" class="Wdate" id="dgDateMin"
										name="prplReplevyMainVo.replevyDate"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dgDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" style="width: 97%"
										value="<fmt:formatDate value='${prplReplevyMain.replevyDate}' pattern='yyyy-MM-dd'/>" />
									<font class="must">*</font>
								</div>
								<label class="form_label col-2">追回日期</label>
								<div class="form_input col-2">
									<input type="text" class="Wdate" id="dgDateMax"
										name="prplReplevyMainVo.endDate"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dgDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" style="width: 97%"
										value="<fmt:formatDate value='${prplReplevyMain.endDate}' pattern='yyyy-MM-dd'/>" />
									<font class="must">*</font>
								</div>
								<div id="entrust">
									<label class="form_label col-2">委托追偿机构</label>
									<div class="form_input col-2">
										<input type="text" class="input-text"
											name="prplReplevyMainVo.entrustComName"
											value="${prplReplevyMain.entrustComName}" 
											required="required" datatype="*1-20" id="entrustComName" />
										<font class="must">*</font>
									</div>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">追偿原因</label>
								<div class="form_input col-10">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.replevyReason"
										value="${prplReplevyMain.replevyReason}" datatype="*1-50"
										ignore="ignore" errormsg="请勿输入超过50个字！" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">追偿进度</label>
								<div class="form_input col-10">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.progress"
										value="${prplReplevyMain.progress}" datatype="*1-50"
										ignore="ignore" errormsg="请勿输入超过50个字！" />
								</div>
							</div>
						</div>
					</div>
					<p>
					<div class="table_cont">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th width="10%">
										<button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add"  onclick="addPayInfor()" id="addPayInfo"></button>
									</th>
									<th>损失险别</th>
									<th>损失类别</th>
									<th>追回项名称</th>
									<th>计划追回金额</th>
									<th>实际追回金额</th>
								</tr>
							</thead>
							<tbody class="text-c" id="RecPayTbody">
								<input type="hidden" id="RecPayTSize" value="${fn:length(prplReplevyDetailVos)}">
								<%@include file="RecPayEdit_RecPayTbody.jsp"%>

							</tbody>
						</table>
					</div>
					<div class="formtable mt-15">
						<div class="table_cont">
							<div class="row cl">
								<label class="form_label col-2">总计划追回金额</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prplReplevyMainVo.sumPlanReplevy" id="sumPlanReplevy"
										value="${prplReplevyMain.sumPlanReplevy }" readonly="readonly" 
										onchange="defaultFee()"/>
								</div>
								<label class="form_label col-2">总追偿赔款</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" id="sumRealReplevy"
										name="prplReplevyMainVo.sumRealReplevy" onchange="defaultFee()"
										value="${prplReplevyMain.sumRealReplevy}" readonly="readonly" />
								</div>
								<label class="form_label col-2">总追偿费用</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" id="sumReplevyFee"
										name="prplReplevyMainVo.sumReplevyFee" onchange="defaultFee()"
										value="${prplReplevyMain.sumReplevyFee}" readonly="readonly" />
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="table_wrap">
					<div class="table_title f14">追偿报告</div>
					<div class="formtable">
						<div class="col-12">
							<textarea datatype="*" class="textarea w100" nullmsg="请输入定损意见！"
								ignore="ignore" name="prplReplevyMainVo.replevyText"
								maxlength="200" placeholder="请输入意见...">${prplReplevyMain.replevyText }</textarea>

						</div>
					</div>
				</div>


			</form>
		</div>
		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="submit" type="submit" onclick="saveRecPay('save')"
				value="暂存"> <input class="btn btn-primary ml-5"
				onclick="saveRecPay('commit')" id="commit" type="submit" value="提交">

		</div>
	</div>

	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	var saveTypes;
	$(function() {
		
	  testpadPay();
	  defaultFee();
		var flowTaskId = $("input[name='flowTaskId']").val();
		var ajaxEdit = new AjaxEdit($('#forms'));
		ajaxEdit.targetUrl = "/claimcar/recPay/saveRecPay.do?flowTaskId="
				+ flowTaskId + "";
		ajaxEdit.afterSuccess = function(result) {
			
			if(saveTypes == "save"){
				layer.msg("暂存成功");
				window.location.reload();
			}
			var id =eval(result.data);
			$("#fid").val(id);
			var sumRealReplevy=eval(result.statusText);
			$("#sumRealReplevy").val(sumRealReplevy);
			
			
			
			if(saveTypes == "commit"){
				
				commit();
			}
		};
		//绑定表单
		ajaxEdit.bindForm();

	});
		//费用合算
		$("prplReplevyDetailVo.planReplevy").blur(function() {
			var sumNum = 0.00;
			$("prplReplevyDetailVo.planReplevy").each(function() {
				if (!isBlank($(this).val())) {
					sumNum += parseFloat($(this).val());
				}
			});
			$("prplReplevyDetailVo.planReplevy").val(sumNum);

		});

		$("prplReplevyDetailVo.replevyFee").blur(function() {
			var sumNum = 0.00;
			$("prplReplevyDetailVo.replevyFee").each(function() {
				if (!isBlank($(this).val())) {
					sumNum += parseFloat($(this).val());
				}
			});
			$("prplReplevyDetailVo.replevyFee").val(sumNum);

		});

		$("prplReplevyDetailVo.realReplevy").blur(function() {
			var sumNum = 0.00;
			$("prplReplevyDetailVo.realReplevy").each(function() {
				if (!isBlank($(this).val())) {
					sumNum += parseFloat($(this).val());
				}
			});
			$("prplReplevyDetailVo.realReplevy").val(sumNum);

		});
		
		//验证此案件是否发起过垫付
		function testpadPay(){
			
			var registNo=$("#registNo").val();
			url="/claimcar/padPay/padPayView.ajax";
			params={
					"registNo" : registNo
					};
			$.ajax({
				url : url, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义	
					var result = eval(jsonData);
					if (result.status == "200") {
						if (isBlank(result.data)) {
							$("#padpay").prop("class","btn btn-disabled");
							$("#padpay").removeAttr("onclick");
							
						}
					}
				}
			});
		}

		//新增费用信息行

	     function addPayInfor(){
			var riskCode = $("#riskCode").val();
			var $tbody = $("#RecPayTbody");
			// 获取当前行号
			var size = $("#RecPayTSize").val();
			// 获取当前有多少行
            var params = {
				"riskCode" : riskCode,
				"size" : parseInt(size)
			};
			var url = "/claimcar/recPay/addReplevy.ajax";
			$.post(url, params, function(result) {
				$tbody.append(result);
				$("#RecPayTSize").val(parseInt(size) + 1);
			});
			
		};
			

		//删除信息行
		function delSubRisk(element) {
			var prpLReplevyVo = "prplReplevyMainVo.prplReplevyDetails";
			var index = $(element).attr("name").split("_")[1];// 下标
			var $parentTr = $(element).parent().parent();
			var $subRiskSize = $("#RecPayTSize");
			var subRiskSize = parseInt($subRiskSize.val(), 10);
			$subRiskSize.val(parseInt(subRiskSize) - 1);// 删除一条prplReplevyDetailVos_
			delTr(subRiskSize, index, "prplReplevyDetailVo_", prpLReplevyVo);
			$parentTr.remove();
			//calSumLossFee();
			calSumPlanReplevy();
			calSumReplevyFee();
			calSumRealReplevy();
		}
		
		function saveRecPay(saveType) {
			var items = $("#RecPayTbody input[name$='realReplevy']");
			var sumFeeRealReplevy = parseFloat(0.00);
			var sumFee = parseFloat(0.00);
			var flag = "1";
			$(items).each(function() {
				var chargeFee = $(this).val();
			    var lossCategoryTd = $(this).parent().siblings();
			    var lossCategory = lossCategoryTd.find("[name$='lossCategory']").val();
			    if("P" == lossCategory){//赔款
			    	if (chargeFee == "") {
						chargeFee = parseFloat(0.00);
					}
			    	sumFeeRealReplevy = sumFeeRealReplevy + parseFloat(chargeFee);
			    }else if("F" == lossCategory){//费用
			    	if (chargeFee == "") {
						chargeFee = parseFloat(0.00);
					}
			    	sumFee = sumFee + parseFloat(chargeFee);
			    }else{
			    	layer.alert("请选择损失类别!");
			    	flag = "0";
			    	return;
			    }
			});
			$("#sumReplevyFee").val(sumFee);
			$("#sumRealReplevy").val(sumFeeRealReplevy);
			if(flag == 1){
				$("#forms").submit();
				saveTypes = saveType;
			}
		}

		//提交
		function commit() {
			
			var flowTaskId = $("input[name='flowTaskId']").val();
			var params = $("#forms").serialize();
			$.ajax({
				url : "/claimcar/recPay/commitRecPay.do?flowTaskId="
						+ flowTaskId + "",
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params,
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
					if(eval(jsonData).status == "200" ){
						$("#submit").hide();
						$("#commit").hide();
						$("input").attr("disabled", "disabled");
						$("select").attr("disabled", "disabled");
						layer.msg("提交成功");
						window.location.reload();//提交成功刷新页面
					}else{
						layer.alert("提交失败！");
					}
				},
				error : function(jsonData) {
					layer.alert("提交失败！");
				}
			});
			ajaxEdit.bindForm();
		}

		//关闭
		function closeLayer() {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}

		function calSumPlanReplevy(field, checkFlag) {
			var items = $("#RecPayTbody input[name$='planReplevy']");
			var sumFee = parseFloat(0.00);
			$(items).each(function() {
				var chargeFee = $(this).val();
				if (chargeFee == "") {
					chargeFee = parseFloat(0.00);
				}
				sumFee = sumFee + parseFloat(chargeFee);
			});

			$("#sumPlanReplevy").val(sumFee);
		}

		function calSumReplevyFee(field, checkFlag) {
			var items = $("#RecPayTbody input[name$='replevyFee']");
			var sumFee = parseFloat(0.00);
			$(items).each(function() {
				var chargeFee = $(this).val();
				if (chargeFee == "") {
					chargeFee = parseFloat(0.00);
				}
				sumFee = sumFee + parseFloat(chargeFee);
			});

			$("#sumReplevyFee").val(sumFee);
		}

		function calSumRealReplevy(field, checkFlag) {
			var items = $("#RecPayTbody input[name$='realReplevy']");
			var sumFeeRealReplevy = parseFloat(0.00);
			var sumFee = parseFloat(0.00);
			$(items).each(function() {
				var chargeFee = $(this).val();
			    var lossCategoryTd = $(this).parent().siblings();
			    var lossCategory = lossCategoryTd.find("[name$='lossCategory']").val();
			    if("P" == lossCategory){//赔款
			    	if (chargeFee == "") {
						chargeFee = parseFloat(0.00);
					}
			    	sumFeeRealReplevy = sumFeeRealReplevy + parseFloat(chargeFee);
			    }else if("F" == lossCategory){//费用
			    	if (chargeFee == "") {
						chargeFee = parseFloat(0.00);
					}
			    	sumFee = sumFee + parseFloat(chargeFee);
			    }else{
			    	layer.alert("请选择损失类别!");
			    }
			});
			$("#sumReplevyFee").val(sumFee);
			$("#sumRealReplevy").val(sumFeeRealReplevy);
		}

		//当追偿类型为委托追偿时显示委托追偿机构输入框
		function showInput() {
			var item = $("#selectType option:selected").val();
			if (item == 2) {
				$("#entrustComName").attr("disabled", false);
				$("#entrustComName").attr("datatype","*1-20");
				$("#entrust").show();
			} else {
				$("#entrustComName").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("#entrustComName").attr("disabled", true);
				$("#entrust").hide();
			}
			if(item == 5){
				var registNo = $("#registNo").val();
				var params = {
						"registNo":registNo,
					};
					var url = "/claimcar/recPay/getSubPers.ajax";
					$.post(url, params, function(result) {
						var persVo = result.data;
						$("input[name='prplReplevyMainVo.repleviedName']").val(persVo.name);
						$("input[name='prplReplevyMainVo.sumPlanReplevy']").val(persVo.thisPaid);
						$("input[name='prplReplevyMainVo.replevyReason']").val("非机动车代位求偿，姓名"+persVo.name);
					});
			}
		}
		
		$(function(){
		   
			$("#selectType").find("option").each(function() {
				if ($(this).val() == "") {
					$(this).text("请选择");
				}
			});
			var replevyType = document.getElementById('selectType');//获取dom
			replevyType.setAttribute('datatype','*');//添加属性
			
			var replevyType = $("input[name='replevyType']").val();
			if (replevyType != "2") {
				$("#entrustComName").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("#entrustComName").attr("disabled", true);
				$("#entrust").hide();
			}
			var handlerStatus = $("input[name='handlerStatus']").val();
			
			if (handlerStatus == "3") {
				$("#submit").hide();
				$("#commit").hide();
				$("textarea").attr("disabled", "disabled");
				$("input").attr("disabled", "disabled");
				$("select").attr("disabled", "disabled");
				$("#addPayInfo").removeAttr("onclick");
			    $("button[name^='prplReplevyDetailVos']").each(function(){
					$(this).removeAttr("onclick");
				});
				
				
				//$(".btn").attr("style", "display:none");
			}
			
			var recPayTSize = $("#RecPayTSize").val();
			for(var i=0;i<recPayTSize;i++){
				var kindCode = $("input[name='prplReplevyMainVo.prplReplevyDetails["+i+"].kindCode']");
				var code = kindCode.options[kindCode.selectedIndex].val();
			}
		});
		//默认计划追偿金额，追偿金额，追偿费用为0
		function defaultFee(){
			var sumPlanReplevy = $("#sumPlanReplevy").val();
			var sumRealReplevy = $("#sumRealReplevy").val();
			var sumReplevyFee = $("#sumReplevyFee").val();
			if(sumPlanReplevy ==""){
				$("#sumPlanReplevy").val(0);
			}
			if(sumRealReplevy ==""){
				$("#sumRealReplevy").val(0);
			}
			if(sumReplevyFee ==""){
				$("#sumReplevyFee").val(0);
			}
		}
	</script>
</body>

</html>