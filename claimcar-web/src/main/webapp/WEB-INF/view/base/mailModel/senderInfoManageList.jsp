<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>邮件发送人查询</title>
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
								<input type="text" class="input-text"  name="prpdEmailVo.name" />
							</div>
							<label class="form-label col-1 text-c">邮箱地址</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpdEmailVo.email" />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
									<%-- <app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo.comCode" id="comCode"
												 type="select" />  --%>
									 <c:choose>
					                <c:when test="${fn:startsWith(userComCode, '0000')}">
										<app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo.comCode" id="comCode"
												 type="select" />
					                </c:when>
					                <c:when test="${fn:startsWith(userComCode, '0001')}">
										<app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo.comCode" id="comCode"
												 type="select" />
					                </c:when>
					                <c:otherwise>
					                	<input type ="hidden" name="prpdEmailVo.comCode" value="${userComCode}"/>
					                	<app:codeSelect codeType="ComCodeLv2"  id="comCode" value="${userComCode}"
												 type="select"  disabled ="true"/>
					                </c:otherwise>
					            </c:choose>  
							</div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c" >
								<button class="btn btn-primary btn-outline btn-search "
									id="search" type="button" disabled>查询</button>
								<button class="btn btn-primary btn-outline btn-search "
									onclick="dispose()" type="button">增加</button>
							
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
									<th>邮箱地址</th>	
									<th>类型</th>
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
	       			"data" : "email"
	       		},{
	       			"data" : "caseTypeName"
	       		}, {
	       			"data" : "comCodeName"
	       		},{
	       			"data" : null
	       		}
	       	  ];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {		
	$('td:eq(4)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose();'>修改</button>");
}

$(function(){
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
	var ajaxList = new AjaxList("#resultDataTable");
	ajaxList.targetUrl = "/claimcar/mailModel/searchSendInfo.do";
	ajaxList.columns = columns;
	ajaxList.rowCallback = rowCallback;
	$("#search").click(function(){
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.query();
	});
});

function dispose(){
	index=layer.open({
	    type: 2,
	    title: "邮件发送人编辑",
	    shadeClose: true,
	    scrollbar: false,
	    skin: 'yourclass',
	    area: ['1000px', '550px'],
	    content:"/claimcar/mailModel/initSenderInfoEdit.do",
	    end:function(){
	    	$("#search").click();
	    }
	});
}
</script>
</body>
</html>