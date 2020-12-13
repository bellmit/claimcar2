<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>单证收集</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
			<div class="top_btn">
				<a class="btn  btn-primary">案件备注</a> <a class="btn  btn-primary">打印索赔须知</a>
				<a class="btn  btn-primary">索赔清单</a> <a class="btn  btn-primary">承保影像资料</a>
				<a class="btn  btn-primary">收款人账户信息维护</a> 
				<a class="btn  btn-primary">单证上传</a> <a class="btn  btn-primary">影像查看</a>
				<a class="btn  btn-primary">诉讼</a> <a class="btn  btn-primary">风险提示</a>
				<a class="btn  btn-primary">车辆追加定损</a> <a class="btn  btn-primary">财产追加定损</a>
				<a class="btn  btn-primary">新增定损</a>
			</div>
		<p>
		<form action="#" id="editform">
			<div class="fixedmargin page_wrap">
				<!--标签页   开始-->
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<span>单证收集信息</span> 
						<span>损失照片</span>
						<span>收款人账户信息</span>
					</div>
					<!--单证收集信息页面 开始-->
					<div class="tabCon">
						<div class="table_wrap">
						<div class="table_title f14">单证收集标志</div>
							<div class="table_cont mt-30 md-30">
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-4">保单号码：</label>
										<div class="form_input col-4">
											<span>1345111645211114</span>
											<span class="ml-15">3453245268865643</span>
										</div>
										<div class="form_input col-4">
											<input class="btn" onclick="" type="button" value="关联保单承保影像资料">
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">收集标志 ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">基本单证 ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">事故证明 ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">基本财产单证 ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">涉案车辆（粤A78928） ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-4">涉案车辆（粤A68728） ：</label>
										<div class="form_input col-4">
											<app:codeSelect codeType="YNCom" type="radio" name="" value=""/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="table_title f14">代位求偿类型</div>
								<div class="formtable ">
									<div class="row cl">
										<div class="form_input col-6">
											<label class="radio-box" style="">
												<input type="radio" name="daiwei" value="1" class="" originvalue="">
												非代位求偿案件
											</label>
											<label class="radio-box" style="">
												<input type="radio" name="daiwei" value="0" class="" originvalue="">
												代位求偿
											</label>
										</div>
									</div>
								</div>
								
								<div class="table_title f14">确认信息</div>
								<div class="table_cont mt-30 md-30">
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-2">客户索赔时间：</label>
										<label class="form_label col-2">2016-05-28 09:18:12</label>
											<div class="form_input col-2">
												<input type="text" class="Wdate" id="dgDateMin" name=" "
													onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})"
													style="width: 97%" /> 
											</div>
										<label class="form_label col-2">补充材料一次性告知时间：</label>
										<label class="form_label col-2">2016-05-28 09:18:12</label>
											<div class="form_input col-2">
												<input type="text" class="Wdate" id="dgDateMin" name=" "
													onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})"
													style="width: 97%" /> 
											</div>
											
									</div>
								</div>
								
								<div class="formtable ">
									<div class="row cl">
										<label class="form_label col-2">索赔材料收集齐全时间：</label>
										<label class="form_label col-2">2016-05-28 09:18:12</label>
										<div class="form_input col-2">
											<input type="text" class="Wdate" id="dgDateMin" name=" " style="width: 97%" 
													onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})"/> 
										</div>
									</div>
								
									<div class="row cl">
									<label class="form_label col-2">是否诉讼：</label>
										<div class="form_input col-2">
											<app:codeSelect codeType="YN10" type="radio" name="susong" value=""/>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!--损失照片页面 开始-->
					<div class="tabCon">
						<div class="table_wrap">
						<div class="table_title f14">损失照片</div>
						<div class="row cl">
						<div class="table_cont col-4">
							<table class="table table-bordered table-bg" style="width：50%">
									<thead class="text-c">
										<tr>
											<th>照片类型</th>
											<th>已上传数量</th>
										</tr>
									</thead>
									<tbody class="text-c">
										<tr>
											<td>事故现场</td>
											<td>2</td>
										</tr>
										<tr>
											<td>标的车：粤B90908</td>
											<td>2</td>
										</tr>
										<tr>
											<td>三者车：粤A8080A</td>
											<td>2</td>
										</tr>
										<tr>
											<td>标的车上财产损失：粤B90908</td>
											<td>2</td>
										</tr>
										<tr>
											<td>三者车上财产损失：粤A8080A</td>
											<td>2</td>
										</tr>
										<tr>
											<td>财产损失（地面）</td>
											<td>2</td>
										</tr>
										<tr>
											<td>涉案人员：王五</td>
											<td>2</td>
										</tr>
										<tr>
											<td>复勘</td>
											<td>2</td>
										</tr>
										<tr>
											<td>复检</td>
											<td>2</td>
										</tr>
										
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<!--收款人信息页面 开始-->
					<div class="tabCon">
						<div class="table_wrap">
							<div class="table_title f14">收款人账户信息</div>
							<div class="table_cont">
								<div class="formtable">
									<div class="row cl">
										<label class="form_label col-1">收款人类型</label>
										<div class="form_input col-3">被保险人</div>
									</div>
									<div class="row cl">
										<label class="form_label col-1">收款人</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
										<label class="form_label col-1">身份证号</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
										<label class="form_label col-1">联系电话</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-1">开户银行</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
										<label class="form_label col-1">账户名</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
										<label class="form_label col-1">银行账号</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="" value="" />
										</div>
									</div>
									<div class="row cl">
										<label class="form_label col-1">银行信息</label>
										<div class="form_input col-3">
											<a class="btn  btn-primary">银行信息</a>
										</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
					
				</div>
			</div>
			
		</form>
	
	<div class="text-c mt-10">
		<input class="btn btn-primary ml-5" id="save" type="submit" value="提交"> 
		<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="暂存"> 
		<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="取消">
		<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="短信发送">
	</div>
	</div>

	<script type="text/javascript">
	$(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
				"current", "click", "0");

	});
	</script>
</body>

</html>