<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘机构查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-2">查勘机构代码</label>
							<div class="form_input col-2">
								<input id=" " type="text" class="input-text" name="prpdCheckBankMainVo.checkCode"/>
							</div>
							
							<label for=" " class="form_label col-2">查勘机构名称</label>
							<div class="form_input col-2">
								<input id=" " type="text" class="input-text" name="prpdCheckBankMainVo.checkName"/>
							</div>
							
							<label for=" " class="form_label col-2">归属机构</label>
							<div class="form_input col-2">
							<%-- 	<app:codeSelect codeType="ComCodeLv2" name="prpdIntermMainVo.ComCode"  id="comCode"
									type="select"  lableType="code-name"/> --%>
									 <c:choose>
						                <c:when test="${fn:startsWith(userComCode, '0000')}">
											<app:codeSelect codeType="ComCodeLv2" name="prpdCheckBankMainVo.comCode" id="comCode"
													 type="select" />
						                </c:when>
						                <c:when test="${fn:startsWith(userComCode, '0001')}">
											<app:codeSelect codeType="ComCodeLv2" name="prpdCheckBankMainVo.comCode" id="comCode"
													 type="select" />
						                </c:when>
						                <c:otherwise>
						                	<input type ="hidden" name="prpdCheckBankMainVo.comCode" value="${userComCode}"/>
						                	<app:codeSelect codeType="ComCodeLv2"  id="comCode" value="${userComCode}"
													 type="select"  disabled ="true"/>
						                </c:otherwise>
						            </c:choose>  
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
					
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							   <button class="btn btn-primary btn-outline btn-noPolicy ml-20" onclick="add()" type="button">增加</button>
					
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
									<th>机构代码</th>
									<th>机构名称</th>
									<th>上级机构名称</th>
									<th>开户银行</th>
									<th>账号</th>
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
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
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
		       			"data" : "checkCode"
		       		}, {
		       			"data" : "checkName"
		       		}, {
		       			"data" : "upperCode"
		       		}, {
		       			"data" : "prpdcheckBank.bankNameName"
		       		},{
		       			"data" : "prpdcheckBank.accountNo"
		       		},{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
			$('td:eq(0)', row).html("<a  onclick='decrease("+data.id+");'>"+data.id+"</a>");
			$('td:eq(6)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='modifiInterm("+data.id+");'>修改</button>");
	}		

	function modifiInterm(modifiId){		
		var flag = "mod";
		index=layer.open({
		    type: 2,
		    title: '修改机构信息',
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['1100px', '550px'],
		    content:"/claimcar/checkagency/checkAgencyEdit?Id="+modifiId+"&flag="+flag,
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function decrease(id){
		index=layer.open({
		    type: 2,
		    title: '查看机构信息',
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['900px', '550px'],
		    content:"/claimcar/checkagency/checkAgencyView?Id="+id+"",
		    
		});
	}
	
	function add(){
		index=layer.open({
		    type: 2,
		    title: '增加机构信息',
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['1100px', '550px'],
		    content:"/claimcar/checkagency/checkAgencyEdit",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	
	function no(){
		return index;
	}
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/checkagency/checkAgencyFind";
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
