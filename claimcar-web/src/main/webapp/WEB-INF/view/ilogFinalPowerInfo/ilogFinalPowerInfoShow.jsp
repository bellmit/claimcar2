<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>ILOG兜底权限查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">	
							<label class="form-label col-1">用户工号</label>
							<div class="form_input col-3"> 
								<input id="ILOGFinalPowerInfo.userCode" type="text" class="input-text" name="ILOGFinalPowerInfo.userCode" />
							</div>
							<label class="form-label col-1">用户名称</label>
							<div class="form_input col-3">
							    <input id="ILOGFinalPowerInfo.userName" type="text" class="input-text" name="ILOGFinalPowerInfo.userName"/>
							</div>                                                                                               
						</div>
					
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
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
								    <th>id</th>
									<th>用户代码</th>
									<th>用户名称</th>
									<th>岗位权限</th>
									<th>权限等级</th>
									<th>岗位金额</th>
									<th>二级机构</th>
									<th>三级机构</th>
									<th>首次录入时间</th>
									<th>更新时间</th>
<!-- 									<th>备注</th>	  -->
									<th>更新</th>	
									<th>删除</th>
								</tr> 
							</thead>
							<tbody class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
						<div class="row text-c">
							<br/>
						</div>
						<div class="row text-c">
							<button class="btn btn-primary btn-outline btn-report" id="add" type="button">增加</button>
							<input type="hidden" id="noCheckExistRegis" value="0"  />
						</div>
				</div>
			</div>
		</div>
		<!--标签页 结束-->
</div>
<%-- <script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script> 
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script> --%>
	<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>
<!-- 
	<script type="text/javascript" src="/claimcar/lib/layer/1.9.3/layer.js"></script> -->
	<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/common.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script> 
	<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
    <script type="text/javascript" src="/claimcar/js/flow/flowCommon.js"></script>
    
<script type="text/javascript">
        
var index;
var id;	
var modifiId=0;
var columns = [
			    {
					"data" :"id",
					"orderable" : true,
					"targets" : 0
				}, {
					"data" : "userCode"
				}, {
					"data" : "userName"
				}, {
					"data" : null,
					"orderable" : false,
	       			"targets" : 0
				}, {
					"data" : null,
					"orderable" : false,
	       			"targets" : 0
				}, {
					"data" : "gradeAmount"
				}, {
					"data" : "branchComcode"
				}, {
					"data" : "subSidiaryComcode"
				}, {
					"data" : "inputTime"
				}, {
					"data" : "updateTime"
				}, {
					"data" : null
				}, {
	       			"data" : null,
	       			"orderable" : false,
	       			"targets" : 0
	       		}				
			 ];
    //查詢回調
	function rowCallback(row, data, displayIndex, displayIndexFull) {
    	
		var goUrl = "/claimcar/ilogfinalpower/ilogFinalPowerInfoupdate.do?id=" + data.id;
		var goUrl2 = "/claimcar/ilogfinalpower/ilogFinalPowerInfodel.do?id=" + data.id;
<!--		 $('td:eq(10)',row).html("<a target='_blank'  onClick=openTaskEditWin('更新','"+goUrl+"')>更新</a>"); -->  
             
             //$('td:eq(0)', row).html("<a href="+goUrl+"  target='_blank'>"+data.id+"</a>");
             $('td:eq(10)',row).html("<button target='_blank' value='更新' style='height:25px;width:40px' onClick=ilogUpdate('更新','"+goUrl+"') >更新</button>");
             $('td:eq(11)',row).html("<button target='_blank' value='删除' style='background:red;height:25px;width:40px' onClick=ilogUpdate('删除','"+goUrl2+"') >删除</button>");
             
             //转换第三列
             if(data.gradePower == 'CarLoss'){
                 $('td:eq(3)',row).html("车辆定损");	 
             }else if(data.gradePower == 'PropLoss'){
            	 $('td:eq(3)',row).html("财产定损");
             }else if(data.gradePower == 'PersonLoss'){
            	 $('td:eq(3)',row).html("人伤定损");
             }else if(data.gradePower == 'PeopChk'){
            	 $('td:eq(3)',row).html("查勘");
             }else if(data.gradePower == 'CompenSate'){
            	 $('td:eq(3)',row).html("理算");
             };
             //转换第四列
             if(data.powerLevel == 'carloss1'){
                 $('td:eq(4)',row).html("定损岗一级");	 
             }else if(data.powerLevel == 'proploss1'){
            	 $('td:eq(4)',row).html("定损岗一级");
             }else if(data.powerLevel == 'peoploss1'){
            	 $('td:eq(4)',row).html("人伤岗一级");
             }else if(data.powerLevel == 'peoploss2'){
            	 $('td:eq(4)',row).html("人伤岗二级");
             }else if(data.powerLevel == 'peopchk1'){
            	 $('td:eq(4)',row).html("查勘岗一级");
             }else if(data.powerLevel == 'peopchk2'){
            	 $('td:eq(4)',row).html("查勘岗二级");
             }else if(data.powerLevel == 'comp1'){
            	 $('td:eq(4)',row).html("理算岗一级");
             };
    }		
	$(function(){
		//$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
        //查詢方法
		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/ilogfinalpower/ilogFinalPowerInfoList.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			});

		$("button.btn-report").click(
				
				function() {	
					layIndex = layer.open({
						title : '添加新的信息',
						type : 2,
						area : [ '90%', '90%' ],
						fix : false, // 固定
						maxmin : true,
						content : "/claimcar/ilogfinalpower/ilogFinalPowerInfoBeforeAdd.do"
					});		       
			});
		
		
	});
	
	function ilogUpdate (title, url){
		layIndex = layer.open({
			title : title,
			type : 2,
			area : [ '90%', '90%' ],
			fix : false, // 固定
			maxmin : true,
			content : url
		});	
	}
	

</script>

</body>
</html>