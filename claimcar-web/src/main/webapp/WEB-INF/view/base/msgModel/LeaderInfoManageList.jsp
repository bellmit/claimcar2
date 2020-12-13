<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>领导信息查询</title>
</head>
<body>
<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					    <!-- 隐藏域 -->
		                <!-- 隐藏域 -->
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">姓名</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpdAddresseeVo.name" />
							</div>
							<label class="form-label col-1 text-c">手机号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpdAddresseeVo.mobileNo" />
							</div>
							
							<label class="form-label col-1 text-c">归属机构</label>
							
							<div class="formControls col-3">
							    <span class="select-box"> 
									<app:codeSelect codeType="ComCodeLv2" name="prpdAddresseeVo.comCode" id="comCode"
											 type="select" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c" >
							
								<button class="btn btn-primary btn-outline btn-search "
									id="search" type="button" disabled>查询</button>
								<button class="btn btn-primary btn-outline btn-search "
										onclick="disposeadd()" type="button">增加</button>
							
						</div>
					</form>
				</div>
			</div>
		<!--标签页 开始-->
	    <div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>姓名</th>
									<th>手机号</th>
									<th>归属机构</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
				</div>
			</div>
		</div>
	<!--标签页 结束-->
    </div>
</div>
<script type="text/javascript">
var columns = [
	       		{
	       			"data" : "name"
	       		}, {
	       			"data" : "mobileNo"
	       		}, {
	       			"data" : "comCodeName"
	       		},{
	       			"data" : null
	       		}
	       	  ];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {		
	$('td:eq(3)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose("+data.id+");'>修改</button>");
}

$(function(){
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
	var ajaxList = new AjaxList("#resultDataTable");
	ajaxList.targetUrl = "/claimcar/msgModel/searchLeaderInfo.do";
	ajaxList.columns = columns;
	ajaxList.rowCallback = rowCallback;
	$("#search").click(function(){
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.query();
	});
});

function dispose(id){
	index=layer.open({
		type: 2,
		title: "领导信息编辑",
		shadeClose: true,
		scrollbar: false,
		skin: 'yourclass',
		area: ['1000px', '550px'],
		content:"/claimcar/msgModel/initLeaderInfoEdit.do?id="+id,
		end:function(){
			$("#search").click();
		}
	});
}

function disposeadd(){
	index=layer.open({
		type: 2,
		title: "领导信息增加",
		shadeClose: true,
		scrollbar: false,
		skin: 'yourclass',
		area: ['1000px', '550px'],
		content:"/claimcar/msgModel/openAddLeaderInfoPage.do?",
		end:function(){
			$("#search").click();
		}
	});
}
</script>
</body>
</html>