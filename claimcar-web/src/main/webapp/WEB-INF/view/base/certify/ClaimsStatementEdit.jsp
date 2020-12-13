<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>索赔清单</title>
</head>
<body>
<div class="fixedmargin page_wrap">
<div class="table_cont">
<div class="table_wrap">
	<div class="table_title f14">基本单证</div>
	<div class="row cl">
		<div class="table_cont col-6">
			<table class="table table-bordered table-bg" style="">
				<thead class="text-c">
					<tr>
						<th>是否勾选</th>
						<th>单证</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>《机动车保险索赔申请书》</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>代位求偿案件索赔申请书</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>机动车辆保险单正本</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">事故证明</div>
	<div class="row cl">
		<div class="table_cont col-6">
			<table class="table table-bordered table-bg" style="">
				<thead class="text-c">
					<tr>
						<th>是否勾选</th>
						<th>单证</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>交通事故责任认定书</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>调解书</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>简易事故处理书</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">基本财产单证</div>
	<div class="row cl">
		<div class="table_cont col-6">
			<table class="table table-bordered table-bg" style="">
				<thead class="text-c">
					<tr>
						<th>是否勾选</th>
						<th>单证</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>《机动车辆保险财产损失确认书》</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>设备总体造价及损失程度证明</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="checkbox" name="" value=""/>
						</td>
						<td>设备恢复的工程预算</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">车辆盗抢资料</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">涉案车辆（标的车）</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">涉案车辆（三者车）</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">涉案人员（王五）</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">其他</div>
	<div class="row cl">
		<div class="table_cont col-6">
			<table class="table table-bordered table-bg" style="">
				<thead class="text-c">
					<tr>
						<th>是否勾选</th>
						<th> </th>
						<th>单证</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td> </td>
						<td> 1 </td>
						<td><input type="text" class="input-text" name="" value="" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<div class="table_wrap text-c mt-10">
	<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="保存"> 
	<input class="btn btn-primary ml-5" id="" onclick="" type="button" value="关闭"> 
</div>
</div>
</div>
</body>

</html>