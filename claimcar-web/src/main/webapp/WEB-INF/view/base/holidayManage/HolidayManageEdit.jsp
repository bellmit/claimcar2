<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>休假申请查看</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		
		<form id="defossform" role="form" method="post" name="fm">
		    <!-- 隐藏域 -->
		    <input type="hidden" name="prpLUserHolidayVo.id" value="${prpLUserHolidayVo.id}"/>
		<!-- 隐藏域 -->
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_wrap">
				<div class="table_title f14">员工基本信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-2">员工代码</label>
								<div class="form_input col-2">
								    <input type="text" class="input-text"
									name="prpLUserHolidayVo.userCode"  value="${userCode }" 
									readonly="readonly" />
								</div>
								<label class="form_label col-2">员工姓名</label>
								<div class="form_input col-2">
								     <input type="text" class="input-text"
									name="prpLUserHolidayVo.userName"  value="${userName }" 
									readonly="readonly" />
								</div>
								<label class="form_label col-2">归属机构</label>
								<div class="form_input col-2">
								
								    <input type="text" class="input-text"  name="prpLUserHolidayVo.comCode" 
								    value="${comCode}" readonly="readonly" codeType="ComCode"/>
								     
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">手机号码1</label>
								<div class="form_input col-2">
								     <input type="text" class="input-text" readonly="readonly"
									name="prpLUserHolidayVo.mobileNo"  value="${prpLUserHolidayVo.mobileNo }"  />
								</div>
								<label class="form_label col-2">手机号码2</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLUserHolidayVo.phone" 
									value="${prpLUserHolidayVo.phone }" readonly="readonly"/>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 列表  -->
				
				
				<div class="table_title f14">岗位列表</div>
				<div class="formtable">
					<div class="table_cont">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th width="20%">序号</th>
									<th width="50%">岗位</th>
									<th width="30%">任务转交人员工号</th>
								</tr>
							</thead>
							<tbody class="text-c" id=gradeTbody>
								<input type="hidden" id="gradeTSize" value="${fn:length(prpLUserHolidayVo.prpLUserHolidayGrades)}"/> 
									<%@include file="HolidayManageEdit_GradeTbody.jsp" %>
							</tbody>
							</table>
					</div>
				</div>	
				
				
				<div class="table_title f14">目前休假列表</div>
				<div class="formtable">
					<div class="table_cont">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th>序号</th>
									<th>休假起期</th>
									<th>休假止期</th>
									<th>销假日期</th>
									<th>休假原因</th>
									<th>天数</th>
									<th>是否同意</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<tr>
									<td>
									    ${1 }
									</td>
									<td>
									<c:set var="startDate">
									    <fmt:formatDate value="${prpLUserHolidayVo.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:set>
										 <input type="text" class="input-text" value="${startDate  }" 
									     pattern="yyyy-MM-dd HH:mm:ss" readonly="readonly" />
									</td>
									<td>
									<c:set var="endDate">
									    <fmt:formatDate value="${prpLUserHolidayVo.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:set>
										 <input type="text" class="input-text" value="${endDate  }" 
									     pattern="yyyy-MM-dd HH:mm:ss" readonly="readonly" />
									</td>
									<td>
									<c:set var="leaveDate">
									    <fmt:formatDate value="${prpLUserHolidayVo.leaveDate }" pattern="yyyy-MM-dd"/>
									</c:set>
									     <input type="text" class="input-text" value="${leaveDate  }" 
									     pattern="yyyy-MM-dd" readonly="readonly" />
									</td>
									<td> <input type="text"   name="prpLUserHolidayVo.cause" readonly="readonly"
									    value="${prpLUserHolidayVo.cause }" style="width:99%"/>
									</td>
									<td>
									    <input type="text"   name="prpLUserHolidayVo.daysNum" readonly="readonly"
									    value="${prpLUserHolidayVo.daysNum }" style="width:99%"/>
									</td>
									<td>
									<c:choose>
									     <c:when test="${prpLUserHolidayVo.checkStatus ==3 }" >
										    已撤销
										</c:when>
										 <c:when test="${prpLUserHolidayVo.checkStatus ==1 }" >
										    <input type=radio name=yes disabled="disabled" checked="checked">是
										</c:when>
										<c:when test="${prpLUserHolidayVo.checkStatus ==0 }" >
                                            <input type=radio name=no disabled="disabled" checked="checked">否
										</c:when>
										<c:otherwise>
										    <input type="radio" name="yes" disabled="disabled">是
                                            <input type="radio" name="no" disabled="disabled">否
                                        </c:otherwise>
									</c:choose>
									</td>
								</tr>
			
								</tbody>
							</table>
					</div>
				</div>	
				
			</div>
		</form>

		<div class="text-c mt-10">
			
			<input class="btn btn-primary ml-5" onclick="closeLayer()" type="button" value="返回">
		</div>
	</div>

	<script type="text/javascript">
	var flag=$("inpurt[name='flag']");
	
		
		$("inpurt[name='flag']").click(function()
		{
			if(flag==1){
				$("#prpLUserHolidayVo.checkStatus").Attr("disabled",ture);
			}else if(flag==2){ 
				$("#prpLUserHolidayVo.checkStatus").attr("disabled",true);
				$("#prpLUserHolidayVo.checkStatus").attr("disabled",false);
			}

		});
		
		//提交表单
		$("#submit").click(function(){	
			
			$("#defossform").submit();
					
			});
		
		
		
		$(function (){
			var ajaxEdit = new AjaxEdit($('#defossform'));
			ajaxEdit.targetUrl = "/claimcar/holidayManage/saveholidayManage.do";
			ajaxEdit.afterSuccess=function(result){
				if(result.status=="200"){
					$("#submit").prop("disabled",true);	//保存成功防止重复提交
				}	
			}; 
			//绑定表单
			ajaxEdit.bindForm();
						
		});
		
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
		
		
	</script>
</body>

</html>