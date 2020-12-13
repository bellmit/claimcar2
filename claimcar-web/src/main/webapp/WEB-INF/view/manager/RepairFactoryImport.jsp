<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>导出数据</title>
	</head>
	<body>
	
	
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
						   <label for="address" class="form_label col-1"> 归属机构</label>
							<div class="form_input col-3">
								<input id="comcode" type="text" class="input-text" name="repairFactoryVo.comcode"/>
							</div>
							<label for="factoryType" class="form_label col-1">修理厂类型	</label>
					 		<div class="form_input col-3">
					 			<span class="select-box">
					 				<app:codeSelect codeType="RepairFactoryType" id="factoryType" name="repairFactoryVo.factoryType"  type="select"/>
								</span>
					 		</div>
							<label for="factoryCode" class="form_label col-1">修理厂代码</label>
							<div class="form_input col-3">
								<input id="factoryCode" type="text" class="input-text"  name="repairFactoryVo.factoryCode" />
							</div>
						
						</div>
						<div class="row mb-3 cl">
							<label for="factoryName" class="form_label col-1">修理厂名称</label>
							<div class="form_input col-3">
								<input id="factoryName" type="text" class="input-text" name="repairFactoryVo.factoryName"/>
							</div>
							
								<label for="validStatus" class="form_label col-1">有效标志</label>
					 		<div class="form_input col-3">
					 			<span class="select-box">
									<select id="validStatus" class="select" name="repairFactoryVo.validStatus">
										<option value="">所有</option>
										<option value="1">有效</option>
										<option value="0">无效</option>
									</select>
								</span>
					 		</div>
							<!-- 该字段待确定 -->
							<label for="frameNo" class="form_label col-2"></label>
							<div class="form_input col-2">
								<button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							</div>
							
						</div>
<!-- 						<div class="line"></div> -->
				<!-- 		<div class="row">
							<span class="col-offset-4 col-5">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button">查询</button>
							   <button class="btn btn-primary" onclick="addRepairF()" type="button">增加</button>
							   
							   
								<input type="file" name="excel" id="fileExecl" class="" onchange="uploadFile(this)"/>
								<input type="button" class="btn btn-primary" value="导入Excel" onclick="importExcel()">


								<button class="btn btn-primary btn-outline" onclick="impExcel()" type="button">导出数据</button>
							   
							   <button class="btn btn-primary btn-outline" onclick="importExcel()" type="button" Style="display: none">导入Excel</button>
							   <button class="btn btn-primary btn-outline" id="noPolicy" type="button" Style="display: none">下载Excel模板</button>
							</span>
						</div> -->
					</div>
				</form>
			</div>
		</div>
	
	
	
		<div>
		<div class="table_wrap">
			<div class="table_title f14">提示：</div>
			<div class="table_cont table_List">
				<table class="table table-border">
					<thead class="text-c">
						<tr>
							<th>选择</th>
							<th>序号</th>
							<th>修理厂类型</th>
							<th>修理厂代码</th>
							<th>修理厂名称</th>
							<th>地址</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<input type="hidden" name="repairFactoryVoList" value="${repairFactoryVoList}"/>
						<c:forEach var="repairVo" items="${repairFactoryVoList}" varStatus="status">
							<tr>
								<td><input name="checkCode" id="" group="ids"
									type="checkbox" onclick="" value="" /></td>
								<td>${repairVo.id}<input type="hidden"
									class="input-text ready-only" name value="${repairVo.id}" />
								</td>
								<td><app:codetrans codeType="RepairFactoryType"
										codeCode="${repairVo.factoryType}" /></td>
								<td>${repairVo.factoryCode}</td>
								<td>${repairVo.factoryName}</td>
								<td>${repairVo.address}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<br /> <br />
			<!-- 底部按钮 -->
			<div class="btn-footer clearfix text-c">
				<input class="btn btn-primary ml-15" type="button" onclick="quding()" value="确定" />
				<input class="btn btn-disabled ml-15" type="button" onclick="" value="返回" />
			</div><br />
		</div>
	</div>
		<script type="text/javascript">
			
			function quding(){
				var ss = $("input[name='repairFactoryVoList']").val();
				var url = "";
				$.ajax({
					url : url,
					type : 2,
					
				});
			}
			/* function close() {
				var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
				parent.layer.close(index);// 执行关闭	
			} */
			
		</script>
	</body>
</html>