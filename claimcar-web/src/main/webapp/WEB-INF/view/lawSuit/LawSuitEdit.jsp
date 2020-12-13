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
							<input type="text" class="input-text" readonly="readonly" id="registNo"
								name="prpLLawSuitVo.registNo" value="${registvo.registNo }" />
						</div>
						<label class="form-label col-1 text-c"> 报案日期 </label>
						<c:set var="reportTime">
									<fmt:formatDate value="${registvo.reportTime }" pattern="yyyy-MM-dd"/>
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
									<fmt:formatDate value="${registvo.damageTime}" pattern="yyyy-MM-dd"/>
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
					<span id="dete_1">诉讼任务</span>
				</div>

				<div class="tabCon" id="detes_1">
					<div class="table_cont">
						<div class="formtable">
							<form id="form" name="form" class="form-horizontal" role="form"
								  method="post">
								<input type="hidden" name="prpLLawSuitVo.nodeCode" value="123456" id="nodeCode">
								<input type="hidden" name="prpLLawSuitVo.registNo" value="${registvo.registNo }"/>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c"> 原告 </label>
									<div class="formControls col-9">
										<input type="text" class="input-text" datatype="*"
											   name="prpLLawSuitVo.plainTiff" value="${LLawSuitVo.plainTiff }"/> <font
											color="red">*</font>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c"> 被告 </label>
									<div class="formControls col-9">
										<input type="text" class="input-text" datatype="*"
											   name="prpLLawSuitVo.accused" value="${LLawSuitVo.accused }"/> <font
											color="red">*</font>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">接到传票日期</label>
									<div class="formControls col-2">
										<input type="text" hidden="hidden" class="Wdate" id="rgtDateMin"
											   name="reportTimeStart" value="${LLawSuitVo.subpoenaTime}"/>
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
											<app:codeSelect type="select" clazz="must" codeType="LawsuitWay"
															name="prpLLawSuitVo.lawsuitWay"/>
										</span>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">车牌号码</label>
									<div class="formControls col-2">
										<input type="text" class="input-text" name="prpLLawSuitVo.licenseNo"
											   value="${licenseNo }"
											   datatype="/(^[\u4e00-\u9fa5]{1}(([A-Z]{1})|([0-9]{2}))[A-Z_0-9]{5}$)|(^新[0]{6}$)/"
											   nullmsg="请填写车牌号码" errormsg="请输入正确的车牌号"/> <font color="red">*</font>
									</div>
									<label class="form-label col-2 text-c">法院案件号</label>
									<div class="formControls col-2">
										<input type="text" class="input-text"
											   name="prpLLawSuitVo.courtNo" value=""/>
									</div>
									<label class="form-label col-2 text-c">诉讼金额</label>
									<div class="formControls col-2">
										<input type="text" class="input-text"
											   name="prpLLawSuitVo.amount" value="" datatype="/^[0-9]+(\.[0-9]{2})?$/"
											    errormsg="请输入数字或两位小数"/>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">诉讼费</label>
									<div class="formControls col-2">
										<input type="text" class="input-text" name="prpLLawSuitVo.estAmount"
											   value="" datatype="/^[0-9]+(\.[0-9]{2})?$/"
											   errormsg="请输入数字或两位小数"/>
									</div>
									<label class="form-label col-2 text-c">处理方式</label>
									<div class="formControls col-2">
										<span class="select-box"> 
											<app:codeSelect type="select" clazz="must" codeType="HandleType"
															name="prpLLawSuitVo.handleType" datatype="*" onchange="changeHandleType('form')"/>
										</span>
									</div>
									<label class="form-label col-2 text-c">法院或仲裁机构名称</label>
									<div class="formControls col-2">
										<input type="text" class="input-text"
											   name="prpLLawSuitVo.ciName" value=""/>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">审判级别</label>
									<div class="formControls col-2">
										<span class="select-box"> 
											<app:codeSelect type="select" clazz="must" codeType="Ttriallevel"
															name="prpLLawSuitVo.ttriallevel" datatype="*"/>
										</span>
									</div>
									<label class="form-label col-2 text-c">律师事务所名称</label>
									<div class="formControls col-2">
										<span class="select-box">
											<select name="prpLLawSuitVo.firmname" class=" select ">
												<option value="">请选择</option>
												<c:forEach var="prpdLawFirmVo" items="${prpdLawFirmVos }"
														   varStatus="status">
													<option value="${prpdLawFirmVo.lawFirmName}">${prpdLawFirmVo.lawFirmName}</option>
												</c:forEach>
										</select>
										</span>
									</div>
									<label class="form-label col-2 text-c">案件经办律师</label>
									<div class="formControls col-2">
										<input type="text" class="input-text"
											   name="prpLLawSuitVo.lawyers" value=""/>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">律师费</label>
									<div class="formControls col-2">
										<input type="text" class="input-text" name="prpLLawSuitVo.attorneyfee"
											   value=""
											   errormsg="请输入数字或两位小数"/>
									</div>
									<label class="form-label col-2 text-c">经办律师评价</label>
									<div class="formControls col-2">
										<span class="select-box"> 
											<app:codeSelect type="select" clazz="must" codeType="Evaluation"
															name="prpLLawSuitVo.evaluation"/>
										</span>
									</div>

									<label class="form-label col-2 text-c">办结日期</label>
									<div class="formControls col-2">
										<input type="text" hidden="hidden" class="Wdate" id="rgtDateMinEnd"
											   name="reportTimeEnd" value=""/>
										<input type="text" class="Wdate" id="inValidDate1" datatype="*"
											   style="width: 97%;" name="prpLLawSuitVo.endTime"
											   value=""
											   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
									</div>
								</div>
								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c">诉讼案件结果</label>
									<div class="formControls col-2">
										<span class="select-box"> 
											<app:codeSelect type="select" clazz="must" codeType="LitigationResult"
															name="prpLLawSuitVo.litigationResult"/>
										</span>
									</div>
									<label class="form-label col-2 text-c">法律岗处理结果</label>
									<div class="formControls col-2">
										<span class="select-box">
											<app:codeSelect type="select" clazz="must" codeType="Legalduty"
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
											<app:codeSelect type="select" clazz="must" codeType="WinOrLostLawsuit"
															name="prpLLawSuitVo.winOrLostLawsuit"/>
										</span>
									</div>
									<label class="form-label col-2 text-c">我司出庭人员</label>
									<div class="formControls col-2">
										<input type="text" class="input-text"
											   name="prpLLawSuitVo.toCourtPerson" value="${LLawSuitVo.toCourtPerson }"/>
									</div>
									<label class="form-label col-2 text-c">是否出庭应诉</label>
									<div class="formControls col-2">
									<span class="select-box">
										<app:codeSelect type="select" clazz="must" codeType="IsToCourt"
														value="${LLawSuitVo.isToCourt }" name="prpLLawSuitVo.isToCourt"
														onchange="isToCourtListener('form')"/>
									</span>
									</div>
								</div>

								<div class="row mb-3 cl">
									<label class="form-label col-2 text-c"> 未出庭原因 </label>
									<div class="col-9">
											<textarea type="textarea w90" placeholder="500字以内"  nullmsg="未出庭原因"
													  name="prpLLawSuitVo.unCourtReason"  class="textarea"></textarea>
									</div>
								</div>

								<div class="table_title f14">诉讼相关内容</div>
								<div class="formtable">
									<div class="col-12">
										<textarea type="textarea" datatype="*0-500" ignore="ignore"
												  name="prpLLawSuitVo.contentes" class="textarea"></textarea>
									</div>
								</div>
								<div class="table_title f14">诉讼结案报告</div>
								<div class="formtable">
									<div class="col-12">
										<textarea class="textarea" datatype="*0-500" ignore="ignore"
												  name="prpLLawSuitVo.endReport"></textarea>
									</div>
								</div>
							</form>
						</div>
						<div class="text-c" id="forms">
							<br/> <a class="btn btn-primary " onclick="cleares('form')">重置</a>
							<a class="btn btn-primary ml-5" onclick="save('form')">保存</a>
							<a class="btn btn-primary" id="close" onclick="closeL()">关闭</a>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
			<script type="text/javascript">
				$(function () {
					$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
							"current", "click", "0");
				});
			</script>
			<script type="text/javascript">
				$(function () {
					var status = parent.$("#handleStatus").val();
					if (status == "3") {
						$("input").attr("disabled", "disabled");
						$("textarea").attr("disabled", "disabled");
						$("a").attr("disabled", "disabled");
						$("#close").removeAttr("disabled");
						$("select").attr("disabled", "disabled");
						$("button").attr("disabled", "disabled");

					}
					changeHandleType('form');
					isToCourtListener('form');
				});

				function cleares(id) {
					$("#" + id + "")[0].reset();
					changeHandleType('form');
					isToCourtListener('form');
				}

				function closeL() {
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
				}

				function add() {
					var sum = 1;
					var registNo = $("#registNo").val();
					var nodeCode = $("#nodeCode").val();
					$("#tabBares").find("span").each(function () {
						sum = sum + 1;
					});
					//$("#tabBares").append("<span id='dete_"+sum+"'>诉讼任务" + sum + "</span>");
					$("#tabBares").append("<span id='dete_" + sum + "'>诉讼任务" + "</span>");
					var formId = "form" + sum;
					var params = {
						"id": formId,
						"registNo": registNo,
						"nodeCode": nodeCode,
						"cacelId": sum,
					};
					var url = "/claimcar/lawSuit/add.ajax";
					$.post(url, params, function (result) {
						var pai = sum - 1;
						$("#adds").append(result);
						$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
								"current", "click", pai);
					});
				}

				//保存诉讼信息
				function save(id) {
					console.log('add');
					var ajaxEdit = new AjaxEdit($("#" + id + ""));
					ajaxEdit.targetUrl = "/claimcar/lawSuit/saveMessage.do";
					ajaxEdit.afterSuccess = function (result) {
						var $result = result.data;
						if ($result == "1") {
							layer.confirm('添加成功!', {
								btn: ['确定'] //按钮
							}, function (index) {
								/* $("#" + id + "").removeAttr("onclick");
								$("#" + id + "").removeClass("btn-primary"); */
								window.location.reload();
								//$("#" + id + "s").addClass("hidden");

								layer.close(index);
							});

						} else {
							layer.msg("操作失败！");
							return;
						}
					};
					//绑定表单
					ajaxEdit.bindForm();
					$("#" + id + "").submit();
				}

				function detes(id) {
					$("#dete_" + id).remove();
					$("#detes_" + id).remove();
					var sum = id - 2;
					$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
							"current", "click", sum);

					//删除数据
				}
			</script>

</body>

</html>
