<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>休假申请处理</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn  btn-primary">历史休假信息</a>
			</div>
			<br />
		</div>
		<p>
		<form id="defossform" role="form" method="post" name="fm">
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_wrap">
				<div class="table_title f14">员工基本信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-2">员工代码</label>
								<div class="form_input col-2">50107</div>
								<label class="form_label col-2">员工姓名</label>
								<div class="form_input col-2">王二</div>
								<label class="form_label col-2">归属机构</label>
								<div class="form_input col-2">广州本部</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">手机号码1</label>
								<div class="form_input col-2">13324242323</div>
								<label class="form_label col-2">手机号码2</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" name="" value=""/>
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
									<th>序号</th>
									<th>岗位</th>
									<th>任务转交人员工号</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<tr>
									<td>1</td>
									<td>单证收集岗</td>
									<td> </td>
								</tr>
								<tr>
									<td>2</td>
									<td>车辆核价初一级</td>
									<td>
										<select class="select">
											<option>90017</option>
											<option>90018</option>
											<option>90019</option>
										</select> 
									</td>
								</tr>
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
									<th>休假起期<font class="must">*</font></th>
									<th>休假止期<font class="must">*</font></th>
									<th>销假日期<font class="must">*</font></th>
									<th>休假原因<font class="must">*</font></th>
									<th>天数</th>
									<th>是否同意</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<tr>
									<td>1</td>
									<td>
										<input type="text" class="Wdate"  id="dgDateMin" name=" " style="width:97%" 
											onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" />
									</td>
									<td>
										<input type="text" class="Wdate"  id="dgDateMin" name=" " style="width:97%" 
											onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" />
									</td>
									<td>2015-09-02</td>
									<td>事假</td>
									<td>5天</td>
									<td>
										<app:codeSelect codeType="YN01" type="radio" name="" value="" clazz="must"/>
									</td>
								</tr>
			
								</tbody>
							</table>
					</div>
				</div>	
				
			</div>
		</form>

		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="save" onclick=" " type="submit" value="提交"> 
			<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="返回">
		</div>
	</div>

	<script type="text/javascript">
		$(function() {

		});
	</script>
</body>

</html>