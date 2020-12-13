<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>休假新增</title>
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
								     <input type="text" class="input-text"
									name="prpLUserHolidayVo.mobileNo"  value="${prpLUserHolidayVo.mobileNo }" 
									readonly="readonly" />
								</div>
								<label class="form_label col-2">手机号码2</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLUserHolidayVo.phone" 
									value="${prpLUserHolidayVo.phone }" datatype="m|/^0\d{2,3}-\d{7,8}$/"  ignore="ignore"/>
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
								<input type="hidden" id="gradeTSize" value="${fn:length(userMap)}"> 
									<%@include file="HolidayManageEdit_Add.jsp" %>
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
									    <fmt:formatDate value="${prpLUserHolidayVo.startDate }" pattern="yyyy-MM-dd HH:mm:ss" />
									</c:set>
										<input type="text" class="Wdate"  id="startDate" name="prpLUserHolidayVo.startDate" 
										value="${startDate }" style="width:99%"  pattern="yyyy-MM-dd HH:mm:ss" datatype="*"
											onFocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate: '%y-%M-%d %h:%m' })" />
									</td>
									<td>
									<c:set var="endDate">
									    <fmt:formatDate value="${prpLUserHolidayVo.endDate }" pattern="yyyy-MM-dd HH:mm:ss" />
									</c:set>
										<input type="text" class="Wdate"  id="endDate" name="prpLUserHolidayVo.endDate" 
										style="width:99%" value="${endDate }" pattern="yyyy-MM-dd HH:mm:ss" onchange="countDay()"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate: '#F{$dp.$D(\'startDate\')}' })" datatype="*"/>
									</td>
									<td>
									<c:set var="leaveDate">
									    <fmt:formatDate value="${prpLUserHolidayVo.leaveDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:set>
									     <input type="text" class="Wdate" value="${leaveDate}" id="leaveDate"  style="width:99%" 
									     name="prpLUserHolidayVo.leaveDate"  pattern="yyyy-MM-dd HH:mm:ss" readonly="readonly" />
									</td>
									<td> <input type="text"   name="prpLUserHolidayVo.cause" datatype="*"
									    value="${prpLUserHolidayVo.cause }" style="width:99%"/>
									</td>
									<td>
									    <input type="text"   name="prpLUserHolidayVo.daysNum" id="daysNum" 
									    value="${prpLUserHolidayVo.daysNum }" style="width:99%" readonly="readonly"/>
									</td>
									<td>
									    <input type="radio" name="yes" disabled="disabled">是
                                        <input type="radio" name="no" disabled="disabled">否
									</td>
								</tr>
			
								</tbody>
							</table>
					</div>
				</div>	
				
			</div>
		</form>

		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="submit"  type="submit" value="提交"> 
			<a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
		</div>
	</div>

	<script type="text/javascript">
	var flag=$("inpurt[name='flag']");
	
		
		//提交表单
		$("#submit").click(function(){
			$("#defossform").submit();
			});
		
		
		
		$(function (){	
			var ajaxEdit = new AjaxEdit($('#defossform'));	
			ajaxEdit.targetUrl = "/claimcar/holidayManage/saveholidayManage.do";
			ajaxEdit.beforeSubmit=function(){
				layer.load(0, {shade : [0.8, '#393D49']});
			};
			ajaxEdit.afterSuccess=function(result){
					$("#submit").hide();	//保存成功防止重复提交
					$("input").attr("disabled",true);
					$("select").prop("disabled",true);
					layer.msg("提交成功");
					layer.close(layerIndex);
					layer.closeAll('loading');
			}; 
			//绑定表单
			ajaxEdit.bindForm();
						
		});
		
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
		//设置销假日期和休假天数
		function countDay(){
			var startDate=$("#startDate").val();
			var endDate=$("#endDate").val();
			var date1 = DateUtils.strToDate(startDate);
			var date2 = DateUtils.strToDate(endDate);
			var diffDays=parseInt((date2 - date1) / 1000 / 60 / 60 /24);
//			var diffHours=parseInt((date2 - date1) / 1000 / 60 / 60 );
			$("#daysNum").val(diffDays+1);
//			date2.setDate(date2.getDate()+1);
//			var date3=new Date(date2).toDateStr(); 
			$("#leaveDate").val(endDate);
		}
		
	</script>
</body>

</html>