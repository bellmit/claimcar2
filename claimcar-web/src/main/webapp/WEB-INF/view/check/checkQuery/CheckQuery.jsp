<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘任务查询</title>
</head>
<body>
	<!--check-query start-->
	<div class="pd-20 workbench_div">
		<!--查询条件 开始-->
		<div class="bdmessage">
			<div class="mb-10 f-14">
				<strong>请输入查勘任务查询条件</strong>
			</div>
			<div class="table-cont pd-10">
				<div class="formtable f_gray4">
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c"><font class="c-red">*</font>报案号</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" />
						</div>
						<label class="form-label col-1 text-c"><font class="c-red">*</font>保单号</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" />
						</div>
						<label class="form-label col-1 text-c"><font class="c-red">*</font>车牌号码</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" />
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">出险时间</label>
						<div class="formControls col-3">
							<input type="text"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})"
								id="datemin" class="input-text Wdate" style="width: 47%">
							- <input type="text"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})"
								id="datemax" class="input-text Wdate" style="width: 47%;">
						</div>
						<label class="form-label col-1 text-c">案件紧急程度</label>
						<div class="formControls col-3">
							<span class="select-box"> <select class="select">
									<option>2-请选择</option>
									<option>0-紧急</option>
									<option>1-一般</option>
							</select>
							</span>
						</div>
						<label class="form-label col-1 text-c">归属机构</label>
						<div class="formControls col-3">
							<span class="select-box"> <select class="select">
									<option>2-请选择</option>
									<option>0-紧急</option>
									<option>1-一般</option>
							</select>
							</span>
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">流入时间</label>
						<div class="formControls col-3">
							<input type="text"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})"
								id="datemin" class="input-text Wdate" style="width: 47%">
							- <input type="text"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})"
								id="datemax" class="input-text Wdate" style="width: 47%;">
						</div>
						<label class="form-label col-1 text-c">任务类型</label>
						<div class="formControls col-3">
							<span class="select-box"> <select class="select">
									<option></option>
									<option>查勘</option>
									<option>复勘</option>
							</select>
							</span>
						</div>
						<label class="form-label col-1 text-c"></label>
						<!-- <div class="formControls col-3">
								<span class="select-box">
									<select class="select">
									 	<option>2-请选择</option>
										<option>0-紧急</option>
										<option>1-一般</option>
									</select>
								</span>
							</div> -->
					</div>
					<div class="row mb-6 cl">
						<label class="form-label col-1 text-c">任务状态</label>
						<div class="formControls col-6">
							<div class="radio-box">
								<input type="checkbox" name="zt1" id="zt1"> <label
									for="zt1">未接收</label>
							</div>
							<div class="radio-box">
								<input type="checkbox" name="zt1" id="zt1"> <label
									for="zt1">正在处理</label>
							</div>
							<div class="radio-box">
								<input type="checkbox" name="zt1" id="zt1"> <label
									for="zt1">已处理</label>
							</div>
						</div>
					</div>
					<div class="line"></div>
					<div class="row">
						<span class="col-offset-10 col-2"> 
							 <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
						</span>
					</div>
				</div>
			</div>
		</div>
		<!--查询条件 结束-->


		<!--标签页 开始-->
		<div class="bxmessage">
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabBar f_gray4 cl">
						<span><img src="images/circle1.png" />未接收(5)</span> <span><img
							src="images/circle3.png" />正在处理(5)</span> <span><img
							src="images/circle4.png" />已处理(5)</span>
					</div>
					<br />
					<div class="tabCon clearfix">
						<table class="table table-border table-hover" cellpadding="0"
							cellspacing="0">
							<thead>
								<tr>
									<th>案件紧急程度</th>
									<th>客户等级</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>车牌号码</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>出险时间</th>
									<th>流入时间</th>
									<th>提交人</th>
									<th>任务类型</th>
									<th>照片上传</th>
									<th>未处理原因</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('查勘处理','/claimcar/check/CheckMain.do','1')">T4000000201112...</a></td>
									<td>00002011</td>
									<td>京AD2121</td>
									<td>周周</td>
									<td>深圳分部</td>
									<td>2015-09-28</td>
									<td>2015-09-28</td>
									<td>测试人</td>
									<td>查勘</td>
									<td><a class="btn btn-primary fl">上传</a></td>
									<td><a class="btn btn-primary fl">录入</a></td>
								</tr>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('查勘处理','/claimcar/check/CheckMain.do','1')">T4000000201112...</a></td>
									<td>00002011</td>
									<td>京AD2121</td>
									<td>周周</td>
									<td>深圳分部</td>
									<td>2015-09-28</td>
									<td>2015-09-28</td>
									<td>测试人</td>
									<td>查勘</td>
									<td><a class="btn btn-primary fl">上传</a></td>
									<td><a class="btn btn-primary fl">录入</a></td>
								</tr>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('查勘处理','/claimcar/check/CheckMain.do','1')">T4000000201112...</a></td>
									<td>00002011</td>
									<td>京AD2121</td>
									<td>周周</td>
									<td>深圳分部</td>
									<td>2015-09-28</td>
									<td>2015-09-28</td>
									<td>测试人</td>
									<td>查勘</td>
									<td><a class="btn btn-primary fl">上传</a></td>
									<td><a class="btn btn-primary fl">录入</a></td>
								</tr>

							</tbody>
						</table>
						<!--table   结束-->
						<!--分页开始-->
						<div class="pagination f_gray4 clearfix">
							<div class="fl">
								<span>共3页，每页显示</span> <select>
									<option>10</option>
									<option>20</option>
									<option>30</option>
								</select> <span>条</span>
							</div>
							<div class="fr clearfix">
								<ul class="clearfix">
									<li class="nocurrent">&lt;</li>
									<li class="clis">1</li>
									<li>2</li>
									<li>&gt;</li>
								</ul>
							</div>
						</div>
						<!--分页结束-->
					</div>

					<!-- 正在处理任务开始 -->
					<div class="tabCon clearfix">
						<table class="table table-border table-hover" cellpadding="0"
							cellspacing="0">
							<thead>
								<tr>
									<th>案件紧急程度</th>
									<th>客户等级</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>车牌号码</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>出险时间</th>
									<th>流入时间</th>
									<th>提交人</th>
									<th>任务类型</th>
									<th>照片上传</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('查勘处理','/claimcar/check/CheckMain.do','1')">T4000000201112...</a></td>
									<td>00002011</td>
									<td>京AD2121</td>
									<td>周周</td>
									<td>深圳分部</td>
									<td>2015-09-28</td>
									<td>2015-09-28</td>
									<td>测试人</td>
									<td>查勘</td>
									<td><a class="btn btn-primary fl">上传</a></td>
								</tr>

								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('查勘处理','/claimcar/check/CheckMain.do','1')">T4000000201112...</a></td>
									<td>00002011</td>
									<td>京AD2121</td>
									<td>周周</td>
									<td>深圳分部</td>
									<td>2015-09-28</td>
									<td>2015-09-28</td>
									<td>测试人</td>
									<td>查勘</td>
									<td><a class="btn btn-primary fl">上传</a></td>
								</tr>

							</tbody>
						</table>
						<!--table   结束-->
						<!--分页开始-->
						<div class="pagination f_gray4 clearfix">
							<div class="fl">
								<span>共3页，每页显示</span> <select>
									<option>10</option>
									<option>20</option>
									<option>30</option>
								</select> <span>条</span>
							</div>
							<div class="fr clearfix">
								<ul class="clearfix">
									<li class="nocurrent">&lt;</li>
									<li class="clis">1</li>
									<li>2</li>
									<li>&gt;</li>
								</ul>
							</div>
						</div>
						<!--分页结束-->
					</div>
					<!-- 正在处理任务结束 -->
				</div>
			</div>
		</div>
		<!--标签页 结束-->
	</div>

	<script type="text/javascript">
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current",
				"click", "0");
		/*图片-编辑*/
		function caseDetails(title, url, id) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
	</script>
	<!--     结束-->
</body>
</html>
