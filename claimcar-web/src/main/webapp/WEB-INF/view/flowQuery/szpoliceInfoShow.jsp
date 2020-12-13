<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>警报事故信息查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">	
							<label class="form-label col-1">报案号</label>
							<div class="form_input col-3"> 
								<input id="accidentResInfo.AccidentNo" type="text" class="input-text" name="AccidentResInfo.RegistNo" />
							</div>
							<label class="form-label col-1">事故编号</label>
							<div class="form_input col-3">
							    <input id="accidentResInfo.AccidentNo" type="text" class="input-text" name="AccidentResInfo.AccidentNo"/>
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
									<th>事故编号</th>
									<th>报案号</th>
									<th>车牌号</th>
									<th>平台公估师姓名</th>
									<th>平台公估师电话</th>
									<th>报案人姓名</th>
									<th>报案人电话</th>
									<th>出险时间</th>
									<th>出险地点</th>
									<th>交警系统/行业平台</th>
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

<script type="text/javascript">
        
//显示详情
function decrease(id){
	index=layer.open({
	    type: 2,
	    title: '查看机构信息',
	    closeBtn: 1,
	    shadeClose: true,
	    scrollbar: true,
	    skin: 'yourclass',
	    area: ['900px', '550px'],
	    content:"/claimcar/manager/intermediaryView.do?Id="+id+""
	});
}

var index;
var id;	
var modifiId=0;
var columns = [
			    {
					"data" :"accidentNo",
					"orderable" : true,
					"targets" : 0
				}, {
					"data" : "registNo"
				}, {
					"data" : "plateNos"
				}, {
					"data" : "surveyorName"
				}, {
					"data" : "surveyorPhone"
				}, {
					"data" : "reporterName"
				}, {
					"data" : "reporterPhoneNo"
				}, {
					"data" : "accidentDate"
				},{
					"data" : "accidentAddress"
				},{
					"data" : "reportMode"
				}
			 ];
    //查詢回調
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		null;//$('td:eq(0)', row).html("<a  onclick='decrease("+data.accidentNo+");'>"+data.accidentNo+"</a>");
    }		
	$(function(){
		//$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
//查詢方法
		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/szpolice/szPoliceCaseInfoList.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		);	
	});

</script>

</body>
</html>