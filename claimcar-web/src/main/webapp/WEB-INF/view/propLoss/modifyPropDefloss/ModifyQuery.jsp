<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>定损修改查询</title>

</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for="registNo" class="form_label col-1">报案号</label>
							<div class="form_input col-3">
								<input id="registNo" type="text" class="input-text" name="propQueryVo.registNo" clazz="must" />
							</div>
							<label for="licenseNo" class="form_label col-1">保单号</label>
							<div class="form_input col-3">
								<input id="licenseNo" type="text" class="input-text" name="propQueryVo.licenseNo" />
							</div>
							<label for="insuredName" class="form_label col-1">车牌号码</label>
							<div class="form_input col-3">
								<input id="insuredName" type="text" class="input-text" name="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label for="engineNo" class="form_label col-1">车架号</label>
							<div class="form_input col-3">
								<input id="engineNo" type="text" class="input-text" name="propQueryVo.frameNo" />
							</div>
							<label for="frameNo" class="form_label col-1">被保险人</label>
							<div class="form_input col-3">
								<input id="frameNo" type="text" class="input-text" name="propQueryVo.insuredName" />
							</div>
						<label for="licenseColor" class="form_label col-1">案件紧急程度</label>
							<div class="form_input col-3">
								<span class="select-box"> <select id="licenseColor" class="select f_gray4" name="propQueryVo.mercyFlag">
										<option value="0">紧急</option>
										<option value="1">一般</option>
								</select>
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-5 col-2">
								<button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->

		<!--标签页 开始-->
			

					<table class="table table-striped table-bordered table-hover jqueryDataTable" id="resultDataTable">
						<thead>
							<tr>
								<th>案件紧急程度</th>
								<th>客户等级</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>损失方</th>
								<th>车牌号码</th>
								<th>损失项目类型</th>
								<th>被保险人</th>
								<th>客户类型</th>
								<th>承保机构</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>一般</td>
								<td>SVIP</td>
								<td><a href="/claimcar/propQueryOrLaunch/propModifyLaunchEdit?businessId=367">601011414401000002220</a></td>
								<td>210010020151101000011</td>
								<td>标的车</td>
								<td>粤B8888</td>
								<td>标的车财产损失</td>
								<td>刘矗</td>
								<td>普通</td>
								<td>广州本部</td>
							</tr>
						</tbody>
					</table>
					<!--table   结束-->

	
		
		<!--标签页 结束-->
	</div>
	<script type="text/javascript">
	</script>
</body>
</html>
