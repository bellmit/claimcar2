<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
						<label class="form-label col-1 text-c"><input type="radio"
							name="ss" class="mr-1" />&nbsp;&nbsp;&nbsp;&nbsp; 报案号</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" id="registNo"
								name="registNo" value="" />
						</div>
						<label class="form-label col-1 text-c"><input type="radio"
							name="ss" class="mr-1" />&nbsp;&nbsp;&nbsp;&nbsp;保单号</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" id="policyNo"
								name="policyNo" value="" />
						</div>
						<label class="form-label col-1 text-c"><input type="radio"
							name="ss" class="mr-1" />&nbsp;&nbsp;&nbsp;&nbsp;车牌号</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" id="licenNo" name="licenNo"
								value="" />
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">案件紧急程度</label>
						<div class="formControls col-3">
							<span class="select-box"> </select> <app:codeSelect
									codeType="MercyFlag" type="select" id="mercyFlag" clazz="must"
									name="prpLcheck.mercyFlag" lableType="code-name" value="" />
							</span>
						</div>
						<label class="form-label col-1 text-c">提交人</label>
						<div class="formControls col-3">
							<span class="select-box"> </select> <app:codeSelect
									codeType="MercyFlag" type="select" id="mercyFlag" clazz="must"
									name="prpLcheck.mercyFlag" lableType="code-name" value="" />
							</span>
						</div>
						<label class="form-label col-1 text-c"><input type="radio"
							name="ss" class="mr-1" />&nbsp;被保险人</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" id="llName" name="llName"
								value="" />
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">报案时间</label>
						<div class="formControls col-3">
							<input type="text" class="input-text Wdate Wdate45"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemin\')||\'%y-%M-%d\'}'})"
								id="datemin" style="width: 47%" value=""> - <input
								type="text" class="input-text Wdate Wdate45"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemax\')}',maxDate:'%y-%M-%d'})"
								id="datemax" value="" style="width: 47%;">
						</div>
						<label class="form-label col-1 text-c">归属机构</label>
						<div class="formControls col-3">
							<span class="select-box"> </select> <app:codeSelect
									codeType="MercyFlag" type="select" id="mercyFlag" clazz="must"
									name="prpLcheck.mercyFlag" lableType="code-name" value="" />
							</span>
						</div>
						<label class="form-label col-1 text-c">客户等级</label>
						<div class="formControls col-3">
							<span class="select-box"> </select> <app:codeSelect
									codeType="MercyFlag" type="select" id="mercyFlag" clazz="must"
									name="prpLcheck.mercyFlag" lableType="code-name" value="" />
							</span>
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">流入时间</label>
						<div class="formControls col-3">
							<input type="text" class="input-text Wdate Wdate45"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemin\')||\'%y-%M-%d\'}'})"
								id="datemin" style="width: 47%" value=""> - <input
								type="text" class="input-text Wdate Wdate45"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemax\')}',maxDate:'%y-%M-%d'})"
								id="datemax" value="" style="width: 47%;">
						</div>
						<label class="form-label col-1 text-c">查询范围</label>
						<div class="formControls col-3">
							<span class="select-box"> </select> <app:codeSelect
									codeType="MercyFlag" type="select" id="mercyFlag" clazz="must"
									name="prpLcheck.mercyFlag" lableType="code-name" value="" />
							</span>
						</div>
						<label class="form-label col-1 text-c">联系电话</label>
						<div class="formControls col-3">
							<input type="text" class="input-text" />
						</div>
					</div>
					<br />
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
					<br />
					<div class="line"></div>
					<br />
					<div class="row">
						<span class="col-offset-8 col-2"> <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
						</span><br />
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
									<th>紧急程度</th><th>客户等级</th><th>报案号</th><th>案件属地</th>
									<th>是否现场</th><th>报案时间</th><th>流入时间</th><th>提交人</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('调度处理','/claimcar/schedule/scheduleEdit/000000000000000001/','1')">T4000000201112...</a></td>
									<td>贵州省六盘水市</td>
									<td>是</td>
									<td>2015年2月11日-22:34:23</td>
									<td>2015年2月11日-22:34:23</td>
									<td>测试理赔员2</td>
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
									<th>紧急程度</th><th>客户等级</th><th>报案号</th><th>案件属地</th>
									<th>是否现场</th><th>报案时间</th><th>流入时间</th><th>提交人</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>一般</td>
									<td>普通</td>
									<td><a href="javascript:;"
										onClick="caseDetails('调度处理','/claimcar/schedule/scheduleEdit/000000000000000001/','1')">T4000000201112...</a></td>
									<td>贵州省六盘水市</td>
									<td>是</td>
									<td>2015年2月11日-22:34:23</td>
									<td>2015年2月11日-22:34:23</td>
									<td>测试理赔员2</td>
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
		
		function caseDetails(title, url, id) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
	</script>
</body>
</html>
