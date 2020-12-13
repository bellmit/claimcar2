<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>调查任务查看</title>
</head>
<body>
	<div class="page_wrap">
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn  btn-primary" onclick="viewEndorseInfo('${surveyVo.registNo}')">保单批改记录</a>
			</div>
			<br />
		</div>
		<br />
		<p>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<div id="tab_demo" class="HuiTab">
						<div class="tabBar cl">
							<span>调查意见</span>
							<span>意见列表</span>
						</div>
					
						<!--选项卡一 -->
						<div class="tabCon">
							<div class="table_wrap">		
								<div class="table_title f14">当前调查任务</div>
								<div class="table_cont table_list">
									<table class="table table-border">
										<thead class="text-c">
											<tr>
												<th>是否自动触发</th>
												<th>发起人</th>
												<th>发起日期</th>
												<th>发起原因</th>
												<th>报案号</th>
												<th>评分日期</th>
												<th>评分环节</th>
												<th>是否包含人伤</th>
												<th>是否小额案件</th>
												<th>反欺诈评分</th>
												<c:if test="${headOffice eq '1'}">
													<th>辅助描述</th>
													<th>规则描述</th>
												</c:if>
											</tr>
										</thead>
										<tbody class="text-c">
											<tr>
												<td><app:codetrans codeType="YN10" codeCode="${surveyVo.isAutoTrigger }"/></td>
												<td><app:codetrans codeType="UserCode" codeCode="${surveyVo.createUser }"/></td>
												<td><fmt:formatDate value="${surveyVo.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<td>${surveyVo.reasonDesc }</td>
												<td>${surveyVo.registNo }</td>
												<td><fmt:formatDate value="${surveyVo.scoreTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<td><app:codetrans codeType="ScoreNode" codeCode="${surveyVo.scoreNode }"/></td>
												<td><app:codetrans codeType="YN10" codeCode="${surveyVo.isInjuryCases }"/></td>
												<td><app:codetrans codeType="YN10" codeCode="${surveyVo.isMinorCases }"/></td>
												<td>${surveyVo.fraudScore }</td>
												<c:if test="${headOffice eq '1'}">
													<td>${surveyVo.auxiliaryDesc }</td>
													<td>${surveyVo.ruleDesc }</td>
												</c:if>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<form  id="form" role="form" method="post"  name="form" >
							    <div class="table_wrap">
									<div class="formtable">
							     		<div class="row cl">
								     			<label class="form_label col-2 " >是否欺诈案件</label>
								                <div class="form_input col-3 ">
								                 <app:codeSelect codeType="YN10" type="radio"  clazz="mr-5" id="isFraud" name="prpLSurveyVo.isFraud"  value="${surveyVo.isFraud}" />
								                </div>
								                <div id="isHideFraudType">
									                 <label class="form_label col-2">欺诈类型</label>
													 <div class="form_input col-3 ">
														 <span class="select-box" style="width: 45%">
														 <app:codeSelect  codeType="FraudType" name="prpLSurveyVo.fraudType" value="${surveyVo.fraudType}" type="select" clazz="must"/>
													     </span>
									                 </div>
								                 </div>

								        </div>
							            <div class="row cl">
										         <label class="form_label col-2">是否整案减损</label>
												 <div class="form_input col-3">
									                 <app:codeSelect codeType="YN10" type="radio" id="impairmentCase" clazz="mr-5"  name="prpLSurveyVo.impairmentCase"  value="${surveyVo.impairmentCase}"/>
								                 </div>
								                 <div id="isHideAmount">
	  												<label class="form_label col-2">减损金额</label>
													 <div class="form_input col-3 ">
										               <input type="text" class="input-text" datatype="amount" ignore="ignore" name="prpLSurveyVo.amout" value="${surveyVo.amout}" style="width: 43%">
									                 </div>
								                 </div>
							            </div>
							            <div class="row cl">
								               <label class="form_label col-2">是否转外部调查机构处理</label>
									           <div class="form_input col-3">
										       <span class="radio-box">
											    <app:codeSelect codeType="YN10" type="radio" value="${surveyVo.externalSurvey}" name="prpLSurveyVo.externalSurvey" />
										       </span>
									           </div>
							            </div>
									</div>
								</div>
								<div class="table_wrap">
									<div class="table_title f14">调查意见</div>
									<div>
										<input hidden="hidden" id="flowTaskId" name="flowTaskId" value="${wfTaskVo.taskId}"/>
										<input hidden="hidden" id="handlerStatus" name="" value="${wfTaskVo.handlerStatus}"/>
										<input hidden="hidden" id="surveyId" name="prpLSurveyVo.id" value="${surveyVo.id}"/>
										<textarea class="textarea h100" name="prpLSurveyVo.opinionDesc" id="opinionDesc" nullmsg="请输入处理意见"  datatype="*1-1000" errormsg="处理意见长度最多为1000字符">${surveyVo.opinionDesc }</textarea>
									</div>
								</div>
								<div class="text-c">
									<br/>
									<c:if test="${wfTaskVo.handlerStatus =='2' }">
										<a class="btn btn-primary cl" id="submit">提交</a>
									</c:if>
								</div>
						   </form>
					   </div>
						  	
								
						<!-- 选项卡二 -->
						<div class="tabCon">
							<div class="table_wrap">		
								<div class="table_title f14">意见列表</div>
								<div class="table_cont table_list">
									<table class="table table-border">
										<thead class="text-c">
											<tr>
												<th>是否自动触发</th>
												<th>发起人</th>
												<th>发起日期</th>
												<th>报案号</th>
												<th>评分日期</th>
												<th>评分环节</th>
												<th>是否包含人伤</th>
												<th>是否小额案件</th>
												<th>反欺诈评分</th>
												<c:if test="${headOffice eq '1'}">
													<th>辅助描述</th>
													<th>规则描述</th>
												</c:if>
												<th>提交人</th>
												<th>提交时间</th>
												<th>调查意见内容</th>
											</tr>
										</thead>
										<tbody class="text-c">
											<c:forEach var="prpLSurveyVo" items="${prpLSurveyVos}" varStatus="status">
												<tr>
													<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isAutoTrigger }"/></td>
													<td><app:codetrans codeType="UserCode" codeCode="${prpLSurveyVo.createUser }"/></td>
													<td><fmt:formatDate value="${prpLSurveyVo.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td>${prpLSurveyVo.registNo }</td>
													<td><fmt:formatDate value="${prpLSurveyVo.scoreTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td><app:codetrans codeType="ScoreNode" codeCode="${prpLSurveyVo.scoreNode }"/></td>
													<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isInjuryCases }"/></td>
													<td><app:codetrans codeType="YN10" codeCode="${prpLSurveyVo.isMinorCases }"/></td>
													<td>${prpLSurveyVo.fraudScore }</td>
													<c:if test="${headOffice eq '1'}">
														<td>${prpLSurveyVo.auxiliaryDesc }</td>
														<td>${prpLSurveyVo.ruleDesc }</td>
													</c:if>
													<td><app:codetrans codeType="UserCode" codeCode="${prpLSurveyVo.handlerUser }"/></td>
													<td><fmt:formatDate value="${prpLSurveyVo.handlerTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td>${prpLSurveyVo.opinionDesc }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var flowTaskId = $("#flowTaskId").val();

		function changeFraudType(){
			 var val=$('input:radio[name="prpLSurveyVo.isFraud"]:checked').val();
			 if(val=="1"){
				 $("#isHideFraudType").show();
             }else{
            	 $("#isHideFraudType").hide();
            	 $('[name="prpLSurveyVo.fraudType"]').val("");
             }
		}
		$("#submit").click(function() {
			//管控
		    var val=$('input:radio[name="prpLSurveyVo.isFraud"]:checked').val();
            if(val==null){
            	layer.alert("请选择欺诈案件");
                return false;
            }
            var val=$('input:radio[name="prpLSurveyVo.impairmentCase"]:checked').val();
            if(val==null){
            	layer.alert("请选择整案减损");
                return false;
            }
            var val=$('input:radio[name="prpLSurveyVo.externalSurvey"]:checked').val();
            if(val==null){
            	layer.alert("请选择转外部调查机构处理");
                return false;
            }
			var ajaxEdit = new AjaxEdit($("#form"));
			ajaxEdit.targetUrl="/claimcar/survey/surveyAudit.do"; 
			ajaxEdit.afterSuccess=function(result){
				if(result.status=="200"){
					layer.msg("操作成功!");
					$.post("/claimcar/survey/init.do?flowTaskId="+flowTaskId, function() {
				 		window.location.reload();
					});
				}
			}; 
			//绑定表单
			ajaxEdit.bindForm();
			$("#form").submit(); 
	    });
		
		
		/**
		 * 未处理任务接收
		 */
		$(function (){
			$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			if($("#handlerStatus").val() != '2'){
				$("body textarea").attr("disabled", "disabled");
				$("body input").attr("disabled", "disabled");
				$("body select").attr("disabled", "disabled");
			}
			var handlerStatus = $("#handlerStatus").val();
			if (handlerStatus == '0') {
				layer.confirm('是否确定接收此任务?', {
					btn : [ '确定', '取消' ]
				}, function(index) {
					layer.close(index);
				 	$.post("/claimcar/survey/acceptSurvey.do?flowTaskId="+flowTaskId, function() {
				 		window.location.reload();
						layer.close(index); 
					});
				}, function() {
					$("body textarea").attr("disabled", "disabled");
				});
			}

	        var val=$('input:radio[name="prpLSurveyVo.isFraud"]:checked').val();
			 if(val=="1"){
				 $("#isHideFraudType").show();
            }else{
           	 $("#isHideFraudType").hide();
           	 $('[name="prpLSurveyVo.fraudType"]').val("");
            }
	        $('[name="prpLSurveyVo.isFraud"]').on("click",function(){
	        	changeFraudType();
	        });

		}); 
	</script>
</body>
</html>
