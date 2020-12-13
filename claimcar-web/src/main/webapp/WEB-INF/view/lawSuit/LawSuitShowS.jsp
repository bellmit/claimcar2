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
						<input type="text" class="input-text" readonly="readonly"
							value="${registvo.damageCode}" />
					</div>
				</div>

			</div>
		</div>
	</div>
	<div class="table_wrap">
		<div class="table_title f14">诉讼任务处理</div>
		<div id="tab_demo" class="HuiTab">
			<div class="panel-header cl" id="adds">
			<div class="tabBar cl" id="tabBares">
				<c:forEach var="LLawSuitVo" items="${prpLLawSuitVo }"
					varStatus="status">
						<span>诉讼任务${status.index+1 }</span>
							<c:if test="${status.last}">
      						 <input hidden="hidden" value="${status.count}" name="statuses"> 
   							 </c:if>
						
						</c:forEach>
					</div>
					<c:forEach var="LLawSuitVo" items="${prpLLawSuitVo }"
					varStatus="status">
					<div class="tabCon">
						<div class="table_cont">
							<div class="formtable">
								<form id="form${status.index+1 }" name="form" class="form-horizontal" role="form"
									method="post">
									<input type="hidden" name="prpLLawSuitVo.nodeCode"
										value="123456" id="nodeCode"> <input type="hidden"
										name="prpLLawSuitVo.registNo" value="${LLawSuitVo.registNo }" />
									<input type="hidden" name="prpLLawSuitVo.id" value="${LLawSuitVo.id }" >
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c"> 原告 </label>
										<div class="formControls col-9">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.plainTiff" value="${LLawSuitVo.plainTiff }" /> <font
												color="red">*</font>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c"> 被告 </label>
										<div class="formControls col-9">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.accused" value="${LLawSuitVo.accused }" /> <font color="red">*</font>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">接到传票日期</label>
										<div class="formControls col-2">
										<input type="text" hidden="hidden" class="Wdate" id="rgtDateMin"
										name="reportTimeStart" value="${startDate}"/>
										<c:set var="subpoenaTime">
									<fmt:formatDate value="${LLawSuitVo.subpoenaTime }" pattern="yyyy-MM-dd"/>
								</c:set>
								
											<input type="text" class="Wdate" id="inValidDate"
												style="width: 97%;" name="prpLLawSuitVo.subpoenaTime"
												value="${subpoenaTime }" readonly="readonly"
												onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',dateFmt:'yyyy-MM-dd'})" />
										</div>
										<label class="form-label col-2 text-c">诉讼类型</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.lawsuitType" class=" select " disabled>
													<option value="我方非当事人">我方非当事人</option>
													<option value="我方当事人">我方当事人</option>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">诉讼方式</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.lawsuitWay" class=" select " disabled>
													<option value="保险合同诉讼">保险合同诉讼</option>
													<option value="道路人身损害赔偿诉讼">道路人身损害赔偿诉讼</option>
													<option value="追偿诉讼">追偿诉讼</option>
											</select>
											</span>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">车牌号码</label>
										<div class="formControls col-2">
											<input type="text" class="input-text"
												name="prpLLawSuitVo.licenseNo" value="${LLawSuitVo.licenseNo }" readonly="readonly"/> <font
												color="red">*</font>
										</div>
										<label class="form-label col-2 text-c">法院案件号</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.courtNo" value="${LLawSuitVo.courtNo }" />
										</div>
										<label class="form-label col-2 text-c">诉讼金额</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.amount" value="${LLawSuitVo.amount }"/>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">可能承担诉讼金额</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.estAmount" value="${LLawSuitVo.estAmount }" />
										</div>
										<label class="form-label col-2 text-c">处理方式</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.handleType" class=" select " disabled>
													<option value="自办">自办</option>
													<option value="委托律师">委托律师</option>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">法院或仲裁机构名称</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.ciName" value="" />
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">审判级别</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.ttriallevel" class=" select " disabled>
													<option value="一审">一审</option>
													<option value="二审">二审</option>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">律师事务所名称</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.firmname" class=" select " disabled>
													<c:forEach var="prpdLawFirmVo" items="${prpdLawFirmVos }" varStatus="status">
														<option value="${prpdLawFirmVo.lawFirmName}">${prpdLawFirmVo.lawFirmName}</option>
													</c:forEach>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">案件经办律师</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly" readonly="readonly"
												name="prpLLawSuitVo.lawyers" value="${LLawSuitVo.lawyers }" />
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">律师费</label>
										<div class="formControls col-2">
											<input type="text" class="input-text" readonly="readonly"
												name="prpLLawSuitVo.attorneyfee" value="${LLawSuitVo.attorneyfee }"/>
										</div>
										<label class="form-label col-2 text-c">经办律师评价</label>
										<div class="formControls col-2">
											<span class="select-box"> <select
												name="prpLLawSuitVo.evaluation" class=" select " disabled>
													<option value="优秀">优秀</option>
													<option value="良">良</option>
													<option value="合格">合格</option>
													<option value="不合格">不合格</option>
											</select>
											</span>
										</div>
									</div>
									<div class="row mb-3 cl">
										<label class="form-label col-2 text-c">诉讼案件结果</label>
										<div class="formControls col-2">
											<span class="select-box"> <select disabled
												name="prpLLawSuitVo.litigationResult" class=" select ">
													<option value="判决">判决</option>
													<option value="调解">调解</option>
													<option value="撤诉">撤诉</option>
													<option value="正在处理">正在处理</option>
											</select>
											</span>
										</div>
										<label class="form-label col-2 text-c">法律岗处理结果</label>
										<div class="formControls col-2">
											<span class="select-box"> <select disabled 
												name="prpLLawSuitVo.legalduty" class=" select ">
													<option value="提交理算岗理算">提交理算岗理算</option>
													<option value="只赔付相关费用">只赔付相关费用</option>
													<option value="正常处理">正常处理</option>
													<option value="撤诉结案">撤诉结案</option>
													<option value="胜诉结案">胜诉结案</option>
													<option value="败诉结案">败诉结案</option>

											</select>
											</span>
										</div>
									</div>
									<div class="table_title f14">诉讼相关内容</div>
									<div class="formtable">
										<div class="col-12">
											<textarea type="textarea" name="prpLLawSuitVo.contentes" readonly="readonly"
												class="textarea" value="${LLawSuitVo.contentes }"></textarea>
										</div>
									</div>
									<div class="table_title f14">诉讼结案报告</div>
									<div class="formtable">
										<div class="col-12">
											<textarea type="textarea" name="prpLLawSuitVo.endReport" readonly="readonly"
												class="textarea" value="${LLawSuitVo.endReport }"></textarea>
										</div>
									</div>

									<div class="text-c">
										<br /> <a class="btn btn-primary"
											onclick="closeL()">关闭</a>
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
			});
		</script>
		<script type="text/javascript">
			var sum = parseInt($("[name='statuses']").val());

			function closeL() {
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
			}
			
		</script>
</body>

</html>