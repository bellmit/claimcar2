<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>报案查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >
						<div class="row mb-9 cl">
								<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" clazz="must"
										name="comCode" lableType="code-name"/>
										<font color="red">*</font>
								</span>
								
							</div>
							<label class="form-label col-1 text-c">年度
							</label>
							<div class="formControls col-3">
								<input name='avgYear' class="input-text" value = '2016' datatype="*"  errormsg="请输入年度信息" ><font color="red">*</font>
							</div>
							<label class="form-label col-1 text-c">状态</label>
								<div class="formControls col-3">
								<span class="select-box"> 
								 <select  name="validFlag" class=" select ">
								 <option>全部</option>
								 <option value="有效">有效</option>
								<option value="无效">无效</option>
								</select>
								<font color="red">*</font>
								</span>
								
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<!-- <button class="btn btn-primary btn-outline btn-search"
									id="search" type="submit" disabled>查询</button> -->
									<button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
						
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span><button onclick="imports()" class="btn btn-primary btn-outline btn-search">导入</button></span> 
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>序号</th>
								<th>机构</th>
								<th>年度</th>
								<th>险种代码</th>
								<th>险别代码</th>
								<th>案均类型</th>
								<th>案均金额</th>
								<th>导入时间</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理-->
			</div>
			<!--标签页 结束-->
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
	<script type="text/javascript">
		//----------正在处理  --- start
		var columns = [ 
			{"data" : "id"}, //序号
			{"data" : "comCode"}, //机构
	        {"data" : "avgYear"}, //年度
	        {"data" : "riskCode"}, //险种代码
	        {"data" : "kindCode"}, //险别代码
			{"data" : "avgType"}, //案均类型
	        {"data" : "avgAmount"}, //案均金额
	        {"data" : "createTime"}, //导入时间
			{"data" : "validFlag"}//状态
			];

	 	function rowCallback(row, data, displayIndex, displayIndexFull) {
		}  
		
	 	function search(){
			var ajaxList = new AjaxList("#DataTable_0");
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback; 
			ajaxList.query();
		}
		
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			
		}); 
		
		
					
		function imports() {
		 	index = layer.open({
				type : 2,
				title : "导入",
				shadeClose : true,
				scrollbar : true,
				skin : 'yourclass',
				area : [ '1150px', '550px' ],
				content : [ "/claimcar/claimAvgList/importInit.do", "no" ]
			}); 
		}
	</script>
</body>
</html>
