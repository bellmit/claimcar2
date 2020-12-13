<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>临时保单录入</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<!--基本信息    开始-->
			<div class="table_wrap">
            	<div class="table_title f14">基本信息</div>
				<div class="table_cont ">
					<div class="formtable ">
						<div class="row  cl">
							<label class="form_label col-2">临时保单号：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" value="000000000000"/>
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2">被保险人：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
						<div>
							<label class="form_label col-2">承保机构：</label>*
							<div class="form_input col-3">
								<span class="select-box">
									<select class="select">
										<option>深圳分公司</option>
										<option>广州分公司</option>
									</select>
								</span>*
								<span class="red">*</span>
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2">车牌号码：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
						</div>
						<div class="row  cl">
							<label class="form_label col-2">发动机号：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2">车架号：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
						</div>
						<div class="row  cl">
							<label class="form_label col-2">厂牌型号：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2">号牌底色：</label>
							<div class="form_input col-3">
								<span class="select-box">
									<select class="select">
										<option>蓝色</option>
										<option>黑色</option>
									</select>
								</span>
							</div>
							<div class="form_input col-1"></div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">起保日期：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2">终保日期：</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"/>
							</div>
							<div class="form_input col-1"></div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">险种名称：</label>
							<div class="form_input col-3">
								<input type="checkbox" />交强险
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="checkbox" />商业险
							</div>
							<div class="form_input col-1"></div>
							<label class="form_label col-2"></label>
							<div class="form_input col-3">
							</div>
							<div class="form_input col-1"></div>
						</div>
					</div>
				</div>
			</div>
			<!--基本信息    结束-->
			
			<!--标的   开始-->
			<div class="table_wrap">
            	<div class="table_title f14">标的</div>
				<div class="table_cont ">
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>险别名称</th>
					 				<th>保额</th>
					 				<th>删除</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<tr>
					 				<td>
					 					<span class="select-box">
											<select class="select">
												<option>1</option>
												<option>2</option>
											</select>
										</span>
									</td>
					 				<td>
					 					<input type="text" class="input-text" />
					 				</td>
					 				<td>
					 					<a class="btn btn-zd mr-10 fl">删除</a>
					 				</td>
					 			</tr>
					 		</tbody>
					 </table>
					 <a class="btn btn-zd mr-10 fl">增加</a>
				</div>
			</div>
			<!--标的  结束-->
			
			<!--底部信息 开始-->
			<div class="row cl">
				<!--备注  开始-->
				<div  class="form_input col-2"></div>
				<div  class="form_input col-8">
					<div class="notice_title clearfix">
						<div class="fl f14"><strong>备注</strong></div>
					</div>
					<textarea class="textarea w90" name="" placeholder="请输入..."></textarea>
				</div>
				<!--备注   结束-->
			</div>
			
			<div class="row">
			<div class="btn-toolbar" style="text-align: center;">
				<a href="/claimcar/regist/edit.do" class="btn btn-primary">登记案件</a></li>
			</div>
		</div>
	</body>
</html>