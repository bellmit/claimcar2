<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>休假审核处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<form id="defossform" role="form" method="post" name="fm">
			<!-- 隐藏域 -->
			<input type="hidden" name="prpLUserHolidayVo.id" value="${prpLUserHolidayVo.id}" /> 
		    <input type="hidden" id="check" value="${prpLUserHolidayVo.checkStatus}" />
		    <input type="hidden" id="bl" value="${bl}" />
		    <input type="hidden" name="prpLUserHolidayVo.createTime" 
		         value="<fmt:formatDate value='${prpLUserHolidayVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
		    <input type="hidden" name="prpLUserHolidayVo.createUser" value="${prpLUserHolidayVo.createUser}" />  
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
										name="prpLUserHolidayVo.userCode" value="${prpLUserHolidayVo.userCode }"
										readonly="readonly" />
								</div>
								<label class="form_label col-2">员工姓名</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prpLUserHolidayVo.userName" value="${prpLUserHolidayVo.userName }"
										readonly="readonly" />
								</div>
								<label class="form_label col-2">归属机构</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prpLUserHolidayVo.comCode" value="${prpLUserHolidayVo.comCode}"
										readonly="readonly" codeType="ComCode" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">手机号码1</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prpLUserHolidayVo.mobileNo"
										value="${prpLUserHolidayVo.mobileNo }" readonly="readonly" />
								</div>
								<label class="form_label col-2">手机号码2</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										name="prpLUserHolidayVo.phone"
										value="${prpLUserHolidayVo.phone }" readonly="readonly" />
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
								<input type="hidden" id="gradeTSize"
									value="${fn:length(prpLUserHolidayVo.prpLUserHolidayGrades)}">
								<%@include file="HolidayManageEdit_GradeTbody.jsp"%>
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
									<td>${1 }</td>
									<td><c:set var="startDate">
											<fmt:formatDate value="${prpLUserHolidayVo.startDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:set> <input type="text" class="input-text"
										name="prpLUserHolidayVo.startDate" pattern="yyyy-MM-dd HH:mm:ss"
										readonly="readonly" value="${startDate}" /></td>
									<td><c:set var="endDate">
											<fmt:formatDate value="${prpLUserHolidayVo.endDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:set> <input type="text" class="input-text"
										name="prpLUserHolidayVo.endDate" pattern="yyyy-MM-dd HH:mm:ss"
										readonly="readonly" value="${endDate}" /></td>
									<td><c:set var="leaveDate">
											<fmt:formatDate value="${prpLUserHolidayVo.leaveDate }"
												pattern="yyyy-MM-dd" />
										</c:set> <input type="text" class="input-text"
										name="prpLUserHolidayVo.leaveDate" pattern="yyyy-MM-dd"
										readonly="readonly" value="${leaveDate}" /></td>
									<td><input type="text" name="prpLUserHolidayVo.cause"
										readonly="readonly" value="${prpLUserHolidayVo.cause }"
										style="width: 99%" /></td>
									<td><input type="text" name="prpLUserHolidayVo.daysNum"
										readonly="readonly" value="${prpLUserHolidayVo.daysNum }"
										style="width: 99%" /></td>
									<td>
									<c:choose>
										 <c:when test="${prpLUserHolidayVo.checkStatus ==1 }" >
										    <input type=radio name=yes disabled="disabled" checked="checked">是
										</c:when>
										<c:when test="${prpLUserHolidayVo.checkStatus ==0 }" >
                                            <input type=radio name=no disabled="disabled" checked="checked">否
										</c:when>
										<c:otherwise>
										    <input type="radio" name="prpLUserHolidayVo.checkStatus" value="1" datatype="*">是
									    <input type="radio" name="prpLUserHolidayVo.checkStatus" value="0" >否 
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
			<input class="btn btn-primary ml-5" id="submit" type="button"
				value="提交"> <input class="btn btn-primary ml-5"
				onclick="closeLayer()" type="button" value="返回">
		</div>
	</div>

	<script type="text/javascript">
		var flag = $("inpurt[name='flag']");

		//提交表单
		$("#submit").click(function() {
			var status = $("#check").val();
			if (status == "2") {
				$("#defossform").submit();
			} else {
				alert("该任务已审核！");
			}

		});

		$(function() {
			var check = $("#check").val();
			if(check != '2'){
				$("#submit").hide();
			}
			var ajaxEdit = new AjaxEdit($('#defossform'));
			ajaxEdit.targetUrl = "/claimcar/holidayManage/saveholidayManage.do";
			ajaxEdit.afterSuccess = function(result) {
				$("#submit").hide(); //保存成功防止重复提交
				layer.msg("提交成功");
				window.location.reload();
			};
			ajaxEdit.bindForm();
		});

		function closeLayer() {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
		
		function init(){
			var bl = $("#bl").val();
			if(bl){
				$("#submit").hide();
			}
		}
		
	</script>
</body>

</html>