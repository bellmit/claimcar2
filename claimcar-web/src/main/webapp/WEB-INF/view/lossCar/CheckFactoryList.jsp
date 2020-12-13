<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_cont">
	<div class="formtable BankNum">
		<div class="table_cont">
			<form id="form1" class="form-horizontal" role="form" method="post">
				<div class="formtable f_gray4">
					<div class="row mb-3 cl">
						<label for="factoryName" class="form_label col-3">修理厂名称</label>
						<div class="form_input col-3">
							<input id="factoryName" type="text" class="input-text"
								name="prplRepairFactoryVo.factoryName" />
								
								<input type="hidden" class="input-text" name="prplRepairFactoryVo.comCode" value="${comCode }"/>
						</div>
						<label for="address" class="form_label col-1">地址</label>
						<div class="form_input col-5">
							<app:areaSelect targetElmId="intermBankProvCity" areaCode="${checkAddressCode}" showLevel="3" style="width:100px"/>
							<input type="hidden" id="intermBankProvCity" name="prplRepairFactoryVo.areaCode" value="${checkAddressCode }"/>
						</div>
					</div>
				</div>
				<div class="line"></div>
				<div class="row cl">
					<span class="col-12 text-c">
						<button class="btn btn-primary" id="search" type="button">查询</button>
						<button class="btn btn-primary" onclick="addRepairF()" type="button">增加</button>
						<button class="btn btn-primary" onclick="repairKeep()" type="button">维护</button>
						<button class="btn btn-primary ml-15" id="close" type="button">关闭</button>
						
					</span><br />
				</div>

			</form>
		</div>
	</div>
	<div class="table_title f14">查询结果</div>
	<!--标签页 开始-->
	<div class="tabbox">
		<div id="tab-system" class="HuiTab">
			<div class="tabCon clearfix">
				<table class="table table-border table-hover data-table"
					cellpadding="0" cellspacing="0" id="resultDataTable1">
					<thead>
						<tr class="text-c">
							<th>序号</th>
							<th>修理厂代码</th>
							<th>修理厂名称</th>
							<th>地址</th>
							<th>操作</th>
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
<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
<script type="text/javascript">
//修理厂编辑
function addRepairF() {
	var url = "/claimcar/manager/repairFactoryEdit.do";
	layer.open({
		type : 2,
		title : "新增修理厂编辑",
		area : [ '100%', '100%' ],
		content : [ url ],
		fix : true, // 不固定
		maxmin : false,
		end : function() {
			$("#search").click();
		}
	});
} 
//修理厂维护
function repairKeep(){
	var url="/claimcar/manager/repairFactoryList.do";
	var title="修理厂维护";
	openWinCom(title,url);
}
$("#intermBankProvCity_lv1").blur(function(){
	var index1=$("#intermBankProvCity_lv3").find("option:eq(0)").val();
	if(!isBlank(index1)){
      $("#intermBankProvCity_lv3").find("option:eq(0)").before("<option value=''></option>");
	}
   
});

$("#intermBankProvCity_lv2").blur(function(){
	var index2=$("#intermBankProvCity_lv3").find("option:eq(0)").val();
	if(!isBlank(index2)){
      $("#intermBankProvCity_lv3").find("option:eq(0)").before("<option value=''></option>");
	}
   
});

	$(function() {
		
		
	
		$("#intermBankProvCity_lv3").find("option:eq(0)").before("<option value=''></option>");
	
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current",
				"click", "0");
		/* $("#intermBankProvCity_lv1").css("width", "150px");
		$("#intermBankProvCity_lv2").css("width", "200px"); */

		var columns = [ {
			"data" : "id",
			"orderable" : true,
			"targets" : 0
		}, {
			"data" : "factoryCode"
		}, {
			"data" : "factoryName"
		}, {
			"data" : "address"
		}, {
			"data" : null
		} ];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			//去除字符串中的空格，不然匹配不到函数
			var factoryCode = $.trim(data.id);
			var factoryName = $.trim(data.factoryName);
			var factoryType = $.trim(data.factoryType);
			var mobile = $.trim(data.mobile);
			var address = $.trim(data.address);
		    var s = "Cancelled('" + factoryCode + "','" + factoryName
					+ "','" + factoryType + "','" + mobile + "','" + address + "')";
			$('td:eq(4)', row).html("<button class='btn btn-primary btn-outline btn-search' onclick="+s+">选择</button>");
		}

		$("#search").click(function() {
			var index="0";//判断地区下拉框第三级是否被选择地区
			var data1=$("#intermBankProvCity_lv3").val();
			var data2=$("#intermBankProvCity_lv2").val();
		
			if(isBlank(data1)){
				$("#intermBankProvCity").val(data2);
				index="1";
			}
			
			
							if ($("#intermBankProvCity_lv2").val() == ""
									&& $("#factoryName").val() == "") {
								layer.msg("至少录入一个查询条件！");
								return;
							}
                   
							var ajaxList = new AjaxList("#resultDataTable1");
							ajaxList.targetUrl = "/claimcar/defloss/checkFactoryList.do?index="+index;
							ajaxList.postData = $("#form1").serializeJson();
							ajaxList.columns = columns;
							ajaxList.rowCallback = rowCallback;
							ajaxList.query();
						});

		$("#close").click(function() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		});
	});
	function Cancelled(factoryCode,factoryName,factoryType,mobile,address) {
		parent.$('#factoryCode').val(factoryCode);
		parent.$('#factoryName').val(factoryName);
		parent.$('#repairFactoryType').val(factoryType);
		parent.$("input[name='lossCarMainVo.defSite']").val(address);
		if(isBlank(mobile)){
			mobile="";
		}
		parent.$('#factoryMobile').val(mobile);
		parent.$('#factoryType_hid').val(factoryType);

		//parent.$('#factoryCode').removeClass("Validform_error");
		//parent.$('#factoryCode').qtip('destroy', true);
		parent.$("input[name='lossCarMainVo.defSite']").blur();
		parent.$('#factoryCode').blur();

		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}
</script>