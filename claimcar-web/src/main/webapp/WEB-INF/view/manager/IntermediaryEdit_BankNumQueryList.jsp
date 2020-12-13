<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>行号查询</title>
</head>
<body>
<!-- 行号查询信息页面  开始 -->
<div class="table_cont ">
<div class="formtable BankNum mt-10">
	<div class="table_title f14">行号查询</div>
	<form id="form" class="form-horizontal" role="form" method="post">
	<div class="table_cont pd-5">
		<div class="row cl">
			<label class="form_label col-2 ">地址： </label> 
			<label class="form_label col-3"><span id="NumProv"></span></label>
			<label class="form_label col-3"><span id="NumCity"></span></label>
			<input type="hidden" name="AccBankNameVo.provincial" value="${AccBankNameVo.provincial}" />
			<input type="hidden" name="AccBankNameVo.city" value="${AccBankNameVo.city}" />
			<input type="hidden" name="AccBankNameVo.provinceCode" value="${AccBankNameVo.provinceCode}" />
			<input type="hidden" name="AccBankNameVo.cityCode" value="${AccBankNameVo.cityCode}" />
			<input type="hidden" name="AccBankNameVo.bankCode" value="${AccBankNameVo.bankCode}" />
		</div>
		<div class="line"></div>
		<div class="row cl">
			<label class="form_label col-2">开户行：</label> 
			<label class="form_label col-3"><span id="NumBankName"></span></label> 
			<span class="c-red col-1"></span>
			<div class="form_input col-2 ">
				<input type="text" class="input-text" name="AccBankNameVo.bankName" value="${AccBankNameVo.bankName}" />			
			</div>
			<div class="form_input col-2 ml-15">
				<a class="btn btn-zd mr-10 fl" id="search" onclick="searchBankNum()">检索</a>
				<a class="btn btn-zd mr-10 fl" onclick="closeLayer()">关闭</a>
			</div>
			<input type="hidden" name="AccBankNameVo.belongBank" value="${AccBankNameVo.belongBank}" />
			
		</div>
	</div>
	</form>
	<div class="table_title f14 ">查询结果</div>
	<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>行号</th>
									<th>站点名称</th>
									<th>选择</th>
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
</div>
<!-- 行号查询信息页面  结束 -->

<script type="text/javascript">
	var index;
	var id;	
	var modifiId=0;
	var columns = [
		       		{
		       			"data" :"bankCode",
		       			"orderable" : true,
		       			"targets" : 0
		       		}, {
		       			"data" : "bankName"
		       		}, {
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
			$('td:eq(2)', row).html("<button class=\"btn btn-primary btn-outline btn-search\" onclick=\"getBankNumTab('"+data.bankCode+"','"+data.bankName+"')\">选择</button>");
			$('td:eq(0)', row).html("<span name='accBankCode["+displayIndex+"]'>"+data.bankCode+"</span>");
			$('td:eq(1)', row).html("<span name='accBankName["+displayIndex+"]'>"+data.bankName+"</span>");
			
	}		
	
	function initQueryView(){
		provN = parent.$("#intermBankProvCity_lv1  option:selected").text();
		cityN = parent.$("#intermBankProvCity_lv2  option:selected").text();
		bname = parent.$("#BankName  option:selected").text();
		bQuery = parent.$("#BankQueryText").val();
		provCode = parent.$("#intermBankProvCity_lv1  option:selected").val();
		cityCode = parent.$("#intermBankProvCity_lv2  option:selected").val();
		bankKindCode = parent.$("#BankName option:selected").val();
		
		$("#NumProv ").text(provN);
		$("#NumCity ").text(cityN);
		$("#NumBankName ").text(bname);
		
		$("[name='AccBankNameVo.provincial']").val(provN);
		$("[name='AccBankNameVo.city']").val(cityN);
		$("[name='AccBankNameVo.belongBank']").val(bname);
		$("[name='AccBankNameVo.bankName']").val(bQuery);
		$("[name='AccBankNameVo.provinceCode']").val(provCode);
		$("[name='AccBankNameVo.cityCode']").val(cityCode);
		$("[name='AccBankNameVo.bankCode']").val(bankKindCode);
	}



	function getBankNumTab(bNumber,bOutlets){//点击选中行号信息行赋值
		parent.$("#BankQueryText").val(bOutlets);	
		parent.$("[name='prpdIntermBankVo.BankOutlets']").val(bOutlets);	
		parent.$("[name='prpdIntermBankVo.BankNumber']").val(bNumber);

		closeLayer();
	 };
	function closeLayer(){
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
		parent.layer.close(index);
	}
	 
	$(function(){
		initQueryView();
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
	});
	
	function searchBankNum(){
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/manager/bankNumberFind.do";
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
	}
	
	 
	</script>
</body>
	
