<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>修理厂查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
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
						</div>
						<div class="row mb-3 cl">
							<label for="factoryName" class="form_label col-1">修理厂名称</label>
							<div class="form_input col-3">
								<input id="factoryName" type="text" class="input-text" name="repairFactoryVo.factoryName"/>
							</div>
							<!-- 该字段待确定 -->
							<label for="frameNo" class="form_label col-1">修理厂简称</label>
							<div class="form_input col-3">
								<input id="frameNo" type="text" class="input-text"  value="待确定字段" disabled="true"/>
							</div>
							<label for="address" class="form_label col-1">地址</label>
							<div class="form_input col-3">
								<input id="address" type="text" class="input-text" name="repairFactoryVo.address"/>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-4 col-5">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							   <button class="btn btn-primary" onclick="addRepairF()" type="button">增加</button>
							   
							   
								<!-- <input type="file" name="excel" id="fileExecl" class="" onchange="uploadFile(this)"/>
								<input type="button" class="btn btn-primary" value="导入Excel" onclick="importExcel()"> -->


								<button class="btn btn-primary btn-outline" onclick="expExcel()" type="button">导出数据</button>
							   
							   <button class="btn btn-primary btn-outline" onclick="importExcel()" type="button" Style="display: none">导入Excel</button>
							   <button class="btn btn-primary btn-outline" id="noPolicy" type="button" Style="display: none">下载Excel模板</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->

		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>序号</th>
									<th>修理厂类型</th>
									<th>修理厂代码</th>
									<th>修理厂名称</th>
									<th>修理厂省市区</th>
									<th>地址</th>
									<th>有效状态</th>
									<th>编辑</th>
<%--									<th>操作</th>--%>
								</tr>
							</thead>
							<tbody class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
				</div>
			</div>
		</div>
		<!--标签页 结束-->
		
		
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
	var index;
	var id;
	var columns = [
		       		{
		       			"data" :"id",
		       			"orderable" : true,
		       			"targets" : 0},
		       		{"data" : "factoryType"},
		       		{"data" : "factoryCode"},
		       		{"data" : "factoryName"},
		       		{"data" : "areaCodeName"},
		       		{"data" : "address"},
					{"data" : "validStatusName"},
					{"data" : null}
		       	  ];
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		
		//$('td:eq(1)', row).html("<app:codetrans codeType="+'RepairFactoryType'+" codeCode="+002+"/>");
		if(data.factoryType=="001"){
			$('td:eq(1)', row).html("特约维修站");
		}else if(data.factoryType=="002"){
			$('td:eq(1)', row).html("一类修理厂");
		}else{
			$('td:eq(1)', row).html("二类修理厂");
		}                                            
		$('td:eq(2)', row).html("<a onclick='decrease("+data.id+",0"+");'>"+data.factoryCode+"</a>");

		if (data.validStatusName === "无效") {
			$('td:eq(6)', row).html("<span class='label label-default radius''>"+data.validStatusName+"</span>");
		} else if (data.validStatusName === "有效") {
			$('td:eq(6)', row).html("<span class='label label-secondary radius''>"+data.validStatusName+"</span>");
		}

		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='decrease("+data.id+",1"+");'>修改</button>");

	}	
	
	function expExcel(){
		
		
		var type = $("select[name='repairFactoryVo.factoryType']").val();
		var code = $("input[name='repairFactoryVo.factoryCode']").val();
		var status = $("select[name='repairFactoryVo.validStatus']").val();
		var name = $("input[name='repairFactoryVo.factoryName']").val();
		var address = $("input[name='repairFactoryVo.address']").val();
		var relatedUrl = "/claimcar/manager/importExcel.do?type="+type+
				"&code="+code+"&status="+status+"&name="+name+"&address="+address;
		window.open(relatedUrl);
		/* layer.open({
			type : 2,
			title : "数据导出",
			shadeClose: true,
		    shade: 0.8,
			area: ['95%', '70%'],
			fix : true, // 不固定
			maxmin : false,
			content : relatedUrl,
		}); */
	}
	
	function importExcel(){
		layer.open({
			type : 2,
			title : "数据导出",
			shadeClose: true,
		    shade: 0.8,
			area: ['90%', '50%'],
			fix : true, // 不固定
			maxmin : false,
			content : "/claimcar/manager/excelImport.do",
		});
	}
	
	function Cancelled(btn){
		layer.confirm('确定要注销或激活？', {
		    btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				url:"/claimcar/manager/repairFactoryCancelled.do",
				type:"post",
				data:{"Id":btn},
				dataType:"json",
				success:function(result){
					var $result=result.data;
					if($result=="1"){
						layer.msg('已更改！', {icon: 1});
						$("#search").click();
						return;
					}else{
						layer.msg("操作失败！");
						return;
					}
				}
			});
		});	
	}
	
	
		function decrease(key,index) {
			var biaoti="";
			var url="";
			if(index=="0"){
				biaoti="修理厂查看";
				 url = "/claimcar/manager/factoryId.do?Id=" + key+"&index="+index;
			}else{
				biaoti="修理厂编辑";
			   url = "/claimcar/manager/factoryId.do?Id=" + key+"&index="+index;
			}
			
			layer.open({
				type : 2,
				title : biaoti,
				area : [ '85%', '80%' ],
				fix : true, // 不固定
				maxmin : false,
				content : url,
				end : function() {
					$("#search").click();
				}
			});
		}

		function addRepairF() {
			var url = "/claimcar/manager/repairFactoryEdit.do";
			layer.open({
				type : 2,
				title : "新增修理厂编辑",
				area : [ '95%', '80%' ],
				content : [ url ],
				fix : true, // 不固定
				maxmin : false,
				end : function() {
					$("#search").click();
				}
			});
		}

		function no() {
			return index;
		}

		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
					"current", "click", "0");
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = "/claimcar/manager/repairFactoryFind.do";
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			$("#search").click(function() {
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.query();
			});
		});
		
	</script>
</body>
</html>
