<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>诉讼处理</title>
</head>
<body>
	<div class="table_wrap">
		<div class="table_title f14">报案基本信息</div>
		<div class="table_cont">
			<div class="formtable">


				<%-- <input type="hidden"  name="subNodeCode"  value="${subNodeCode }" > --%>
				<div class="row mb-3 cl">
					<label class="form-label col-1 text-c"> 报案号 </label>
					<div class="formControls col-3">
						<input type="text" class="input-text" readonly="readonly"
							id="registNo" name="prpLLawSuitVo.registNo"
							value="${registvo.registNo }" />
					</div>
					<label class="form-label col-1 text-c"> 报案日期 </label>
					<c:set var="reportTime">
						<fmt:formatDate value="${registvo.reportTime }"
							pattern="yyyy-MM-dd" />
					</c:set>
					<div class="formControls col-3">
						<input type="text" class="input-text" readonly="readonly"
							value="${reportTime }" />
					</div>
					<label class="form-label col-1 text-c"> 客户等级 </label>
					<div class="formControls col-3">
						<input type="text" class="input-text" readonly="readonly"
							value="${registvo.customerLevel}" />
					</div>
				</div>

				<div class="row mb-3 cl">
				
					<label class="form-label col-1 text-c">出险时间</label>
					<c:set var="damageTime">
						<fmt:formatDate value="${registvo.damageTime}"
							pattern="yyyy-MM-dd" />
					</c:set>
					<div class="formControls col-3">
						<input type="text" class="input-text" readonly="readonly"
							value="${damageTime}" />
					</div>
					<label class="form-label col-1 text-c">出险地点</label>
					<div class="formControls col-3">
						<input type="text" class="input-text" readonly="readonly"
							value="${registvo.damageAddress}" />
					</div>
					<label class="form-label col-1 text-c">出险原因</label>
					<div class="formControls col-3">
					<c:set var="damageReason">
									<app:codetrans codeCode="${registvo.damageCode}" codeType="DamageCode" />
								</c:set>
							<input type="text" class="input-text" readonly="readonly"
								 value="${damageReason}"/>
						</div>
				</div>

			</div>
		</div>
	</div>
	<div class="table_wrap">
		<div class="table_title f14">诉讼任务处理</div>
		<span><button class="btn btn-primary " style="width: 10%"
				onclick="add()">添加</button></span>

		<div id="tab_demo" class="HuiTab">
			<div class="panel-header cl" id="adds">
			<div class="tabBar cl" id="tabBares">
				<c:forEach var="LLawSuitVo" items="${prpLLawSuitVo }"
					varStatus="status">
					<%-- 	<span>诉讼任务${status.index+1 }</span> --%>
						<span>诉讼任务</span>
							<c:if test="${status.last}">
      						 <input hidden="hidden" value="${status.count}" name="statuses"> 
   							 </c:if>
						
						</c:forEach>
					</div>
					<c:forEach var="LLawSuitVo" items="${prpLLawSuitVo }" varStatus="statues">
					<div class="tabCon">
						<div class="table_cont">
							<div class="formtable">
								<form id="form${statues.index+1 }" name="form" class="form-horizontal" role="form"
									method="post">
									<input type="hidden" name="prpLLawSuitVo.nodeCode"
										value="123456" id="nodeCode"> <input type="hidden"
										name="prpLLawSuitVo.registNo" value="${LLawSuitVo.registNo }" />
									<input type="hidden" name="prpLLawSuitVo.id" value="${LLawSuitVo.id }" >
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c"> 原告 </label>
										<div class="formControls col-9">
											<input type="text" class="input-text" datatype="*"
												name="prpLLawSuitVo.plainTiff" value="${LLawSuitVo.plainTiff }" /> <font
												color="red">*</font>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c"> 被告 </label>
										<div class="formControls col-9">
											<input type="text" class="input-text" datatype="*"
												name="prpLLawSuitVo.accused" value="${LLawSuitVo.accused }" /> <font color="red">*</font>
										</div>
									</div>
									
									<div class="row mb-3 cl">
									
										<label class="form-label col-2 text-c">接到传票日期</label>
										<div class="formControls col-2">
										<input type="text" hidden="hidden" class="Wdate" id="rgtDateMin"
										name="reportTimeStart" value="${LLawSuitVo.subpoenaTime}"/>
										<c:set var="subpoenaTime">
									<fmt:formatDate value="${LLawSuitVo.subpoenaTime }" pattern="yyyy-MM-dd"/>
								</c:set>
								
											<input type="text" class="Wdate" id="inValidDate"
												style="width: 97%;" name="prpLLawSuitVo.subpoenaTime"
												value="${subpoenaTime }" nullmsg="请输入接到传票日期" datatype="*"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
										</div>
										
										
										<label class="form-label col-2 text-c">诉讼主体地位</label>
										<div class="formControls col-2">
											<span class="select-box"> 
											<app:codeSelect type="select"  clazz="must" datatype="*"
										codeType="LawsuitType" value="${LLawSuitVo.lawsuitType }"
										name="prpLLawSuitVo.lawsuitType"/> 
											</span>
										</div>
										<label class="form-label col-2 text-c">诉讼案件类型</label>
										<div class="formControls col-2">
											<span class="select-box">
												<app:codeSelect type="select"  clazz="must" codeType="LawsuitWay" value="${LLawSuitVo.lawsuitWay }"
													name="prpLLawSuitVo.lawsuitWay"/> 
											</span>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">车牌号码</label>
										<div class="formControls col-2">
											<input type="text" class="input-text"
												name="prpLLawSuitVo.licenseNo" value="${LLawSuitVo.licenseNo }" datatype="/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/" nullmsg="请填写车牌号码" errormsg="请输入正确的车牌号" style="width: 90%"/> <font color="red">*</font>
										</div>
										<label class="form-label col-2 text-c">法院案件号</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" 
												name="prpLLawSuitVo.courtNo" value="${LLawSuitVo.courtNo }" />
										</div>
										<label class="form-label col-2 text-c">诉讼金额</label>
										<div class="formControls col-2">
											<input type="text" class="input-text"
												name="prpLLawSuitVo.amount" value="${LLawSuitVo.amount }" datatype="/^[0-9]+(\.[0-9]{2})?$/"
												   nullmsg="请输入诉讼金额" errormsg="请输入数字或两位小数"/>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">诉讼费</label>
										<div class="formControls col-2">
											<input type="text" class="input-text"
												name="prpLLawSuitVo.estAmount" value="${LLawSuitVo.estAmount }" datatype="/^[0-9]+(\.[0-9]{2})?$/"
												    errormsg="请输入数字或两位小数"/>
										</div>
										<label class="form-label col-2 text-c">处理方式</label>
										<div class="formControls col-2">
											<span class="select-box"> 
												<app:codeSelect type="select"  clazz="must" codeType="HandleType" value="${LLawSuitVo.handleType }"
													name="prpLLawSuitVo.handleType" datatype="*" onchange="changeHandleType('form${statues.index+1 }')"/>
											</span>
										</div>
										<label class="form-label col-2 text-c">法院或仲裁机构名称</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" 
												name="prpLLawSuitVo.ciName" value="${LLawSuitVo.ciName }" />
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">审判级别</label>
										<div class="formControls col-2">
											<span class="select-box"> 
											<app:codeSelect type="select"  clazz="must" codeType="Ttriallevel" value="${LLawSuitVo.ttriallevel }"
													name="prpLLawSuitVo.ttriallevel" datatype="*"/>
											</span>
										</div>
										<label class="form-label col-2 text-c">律师事务所名称</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.firmname" class=" select ">
												<option value="">请选择</option>
												<c:forEach var="prpdLawFirmVo" items="${prpdLawFirmVos }" varStatus="status">
													<c:if test="${LLawSuitVo.firmname == prpdLawFirmVo.lawFirmName}">

													<option value="${LLawSuitVo.firmname}" selected="selected">${LLawSuitVo.firmname}</option>
												</c:if>
												<c:if test="${LLawSuitVo.firmname != prpdLawFirmVo.lawFirmName}">
													<option value="${prpdLawFirmVo.lawFirmName}">${prpdLawFirmVo.lawFirmName}</option>
												</c:if>
												</c:forEach>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">案件经办律师</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" 
												name="prpLLawSuitVo.lawyers" value="${LLawSuitVo.lawyers }" />
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">律师费</label>
										<div class="formControls col-2">
											<input type="text" class="input-text"
												name="prpLLawSuitVo.attorneyfee" value="${LLawSuitVo.attorneyfee }"
												   errormsg="请输入数字或两位小数"/>
										</div>
										<label class="form-label col-2 text-c">经办律师评价</label>
										<div class="formControls col-2">
											<span class="select-box"> 
											<app:codeSelect type="select"  clazz="must" codeType="Evaluation" value="${LLawSuitVo.evaluation }"
													name="prpLLawSuitVo.evaluation"/> 
											</span>
										</div>
										
	                         <label class="form-label col-2 text-c">办结日期</label>
										<div class="formControls col-2">
										<input type="text" hidden="hidden" class="Wdate" id="rgtDateMinEnd"
										name="LLawSuitVo.endTime" value="${LLawSuitVo.endTime}"/>
										<c:set var="endTime">
									<fmt:formatDate value="${LLawSuitVo.endTime }" pattern="yyyy-MM-dd"/>
								</c:set>
								
											<input type="text" class="Wdate" id="inValidDate"
												style="width: 97%;" name="prpLLawSuitVo.endTime"
												value="${endTime }" datatype="*"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
										</div> 
										
									</div>
									
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">诉讼案件结果</label>
										<div class="formControls col-2">
											<span class="select-box"> 
											<app:codeSelect type="select"  clazz="must" codeType="LitigationResult" value="${LLawSuitVo.litigationResult }"
													name="prpLLawSuitVo.litigationResult"/> 
											</span>
										</div>
										<label class="form-label col-2 text-c">法律岗处理结果</label>
										<div class="formControls col-2">
											<span class="select-box">
											<app:codeSelect type="select"  clazz="must" codeType="Legalduty" value="${LLawSuitVo.legalduty }"
													name="prpLLawSuitVo.legalduty"/> 
											</span>
										</div>
										<label class="form-label col-2 text-c">判决/调解金额</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" name="prpLLawSuitVo.judgeAmount"
												   value="${LLawSuitVo.judgeAmount}" datatype="/^[0-9]+(\.[0-9]{2})?$/"
												   nullmsg="请填写判决/调解金额" errormsg="请输入数字或两位小数"/>
											<font color="red">*</font>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">胜败诉</label>
										<div class="formControls col-2">
										<span class="select-box">
											<app:codeSelect type="select"  clazz="must" codeType="WinOrLostLawsuit" nullToVal="1"
															name="prpLLawSuitVo.winOrLostLawsuit" value="${LLawSuitVo.winOrLostLawsuit}"/>
										</span>
										</div>
										<label class="form-label col-2 text-c">我司出庭人员</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" nullmsg="请录入我司出庭人员" datatype="*"
												   name="prpLLawSuitVo.toCourtPerson" value="${LLawSuitVo.toCourtPerson}" />
											<font color="red">*</font>
										</div>
										<label class="form-label col-2 text-c">是否出庭应诉</label>
										<div class="formControls col-2">
											<span class="select-box">
											<app:codeSelect type="select"  clazz="must" codeType="IsToCourt" name="prpLLawSuitVo.isToCourt"
															value="${LLawSuitVo.isToCourt }" onchange="isToCourtListener('form${statues.index+1 }')"/>
											</span>
										</div>
									</div>

									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c"> 未出庭原因 </label>
										<div class="col-9">
											<textarea type="textarea w90" placeholder="500字以内"  nullmsg="未出庭原因" datatype="*,*0-500"
													  name="prpLLawSuitVo.unCourtReason"  class="textarea"
													  value="${LLawSuitVo.unCourtReason }"/>${LLawSuitVo.unCourtReason }</textarea>
										</div>
									</div>

									<div class="table_title f14">诉讼相关内容</div>
									<div class="formtable">
										<div class="col-12">
											<textarea class="textarea" name="prpLLawSuitVo.contentes" datatype="*" ignore="ignore"
												class="textarea" value="${LLawSuitVo.contentes }">${LLawSuitVo.contentes }</textarea>
										</div>
									</div>
									<div class="table_title f14">诉讼结案报告</div>
									<div class="formtable">
										<div class="col-12">
											<textarea class="textarea" name="prpLLawSuitVo.endReport" datatype="*0-500" ignore="ignore" 
												class="textarea" value="${LLawSuitVo.endReport }">${LLawSuitVo.endReport }</textarea>
										</div>
									</div>

									<div class="text-c">
										<br /> <%-- <a class="btn btn-primary "  onclick="cleares('form${status.index+1 }')">重置</a>  --%>
										<a class="btn btn-primary ml-5" onclick="updateMessage('form${statues.index+1 }')">保存</a> 
										<a class="btn btn-primary" onclick="closeL()">关闭</a>
										<c:if test="${statues.index != 0 }">
											<a class="btn btn-primary" onclick="detes('${statues.index+1 }')">删除</a>
										</c:if>
									</div>
								</form>
							</div>
						</div>
					</div>
					
				</c:forEach>
			</div>
		</div>
		</div>
		<script type="text/javascript">
			$(function() {
				$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
						"current", "click", "0");
				changeHandleType("form${statues.index+1 }");
				isToCourtListener("form${statues.index+1 }");
			});
		</script>
		<script type="text/javascript">
			//var sum = parseInt($("[name='statuses']").val());

			function closeL() {
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
			}
			function add() {
				var registNo = $("#registNo").val();
				var nodeCode = $("#nodeCode").val();
				var sum = 1;
				var registNo = $("#registNo").val();
				var nodeCode = $("#nodeCode").val();
				$("#tabBares").find("span").each(function(){
					sum = sum + 1;
				});
				$("#tabBares").append("<span id='dete_"+sum+"'>诉讼任务" + "</span>");
				var formId = "form" + sum;
				var params = {
					"id" : formId,
					"registNo" : registNo,
					"nodeCode" : nodeCode,
					"cacelId" : sum,
				};
				var url = "/claimcar/lawSuit/add.ajax";
				$.post(url, params, function(result) {
					var pai = sum - 1;
					$("#adds").append(result);
					$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
							"current", "click", pai);
				});
			}
			//保存诉讼信息
			function save(id) {
			/* 	var a = $("#" + id + "").serialize();
				$.ajax({
					type : "POST",
					url : "/claimcar/lawSuit/saveMessage.do",
					data : a,
					success : function(data) {
						alert("提交成功！");

					}
				}); */
				var ajaxEdit = new AjaxEdit($("#" + id + ""));
				ajaxEdit.targetUrl="/claimcar/lawSuit/saveMessage.do"; 
				ajaxEdit.afterSuccess=function(result){
					var $result=result.data;
					if($result=="1"){
						layer.confirm('添加成功!', {
							btn : [ '确定' ]
						},
						function() {
							window.location.reload();
						
						});
					}else{
						layer.msg("操作失败！");
						return;
					}
				}; 
				//绑定表单
				ajaxEdit.bindForm();
				$("#" + id + "").submit();
			}
			
			function cleares(id){
				$("#"+id+"")[0].reset();
				changeHandleType("form${statues.index+1 }");
				isToCourtListener("form${statues.index+1 }");
			}
			//修改诉讼信息
			function updateMessage(id) {
				
				//发送ajaxEdit
				var ajaxEdit = new AjaxEdit($("#" + id + ""));
				ajaxEdit.targetUrl="/claimcar/lawSuit/updateMessage.do"; 
				ajaxEdit.afterSuccess=function(result){
					var $result=result.data;
					if($result=="1"){
						layer.alert("修改成功!");
					}else{
						layer.msg("操作失败！");
						return;
					}
				}; 
				//绑定表单
				ajaxEdit.bindForm();
				$("#" + id + "").submit();
				
				
				
				
			/* 	alert(id);
				var registNo = $("#registNo").val();
				var a = $("#" + id + "").serialize();
				alert(registNo + "kkkkk");
				$.ajax({
					type : "POST",
					url : "/claimcar/lawSuit/updateMessage.do",
					data : a,
					success : function(data) {
						alert("修改成功！");
					}
				}); */
			}
			
			function detes(id){
				$("#dete_"+id).remove();
				$("#detes_"+id).remove();
				var sum = id -2 ;
				$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
						"current", "click", sum);
				//删除数据
			//发送ajaxEdit
			var ajaxEdit = new AjaxEdit($("#form" + id + ""));
			ajaxEdit.targetUrl="/claimcar/lawSuit/deteleMessage.do"; 
			ajaxEdit.afterSuccess=function(result){
				var $result=result.data;
				if($result=="1"){
					layer.confirm('删除成功!', {
						btn: ['确定'] //按钮
					}, function(index){
						window.location.reload();
					});
					
				}else{
					layer.msg("操作失败！");
					return;
				}
			}; 
			//绑定表单
			ajaxEdit.bindForm();
			$("#form" + id + "").submit();
				}

			$(function(){
				var status=parent.$("#handleStatus").val();
				if(status=="3"){
					$("input").attr("disabled","disabled");
					$("textarea").attr("disabled","disabled");
					$("a").attr("disabled","disabled");
					$("select").attr("disabled","disabled");
				}
				changeHandleType("form${statues.index+1 }");
				isToCourtListener("form${statues.index+1 }");
			});
		</script>
</body>

</html>