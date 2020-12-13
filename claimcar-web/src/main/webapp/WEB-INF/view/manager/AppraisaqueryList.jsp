<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>伤残鉴定机构信息查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-1">鉴定机构名称</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="prpdAppraisaVo.appraisaName"/>
							</div>
							
							<label for=" " class="form_label col-1">组织机构代码</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="prpdAppraisaVo.appraisaCode"/>
							</div>
							
							<label for=" " class="form_label col-1">有效标志</label>
							<div class="form_input col-3">
					 			<span class="select-box">
									<select id="validStatus" class="select" name="prpdAppraisaVo.validStatus">
									    <option value="">所有</option>
										<option value="1">有效</option>
										<option value="0">无效</option>
									</select>
								</span>
					 		</div>
						</div>
						
				<div class="row cl accident-over-add">
			 	 <label class="form_label col-1">鉴定机构地址</label>
					<div class="form_input col-10 clearfix">
						<div style="display:inline">
							<app:areaSelect targetElmId="damageAreaCode" areaCode="${prpdAppraisaVo.areaCode}" showLevel="2" />
						</div>
						<input type="hidden" id="damageAreaCode" name="prpdAppraisaVo.areaCode" value="${prpdAppraisaVo.areaCode}"/>
						 </div>								
			      </div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>查询</button>
							   <button class="btn btn-primary btn-outline btn-noPolicy ml-20" onclick="add()" type="button">新增</button>
					
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
									<th>鉴定机构名称</th>
									<th>组织机构代码</th>
									<th>创建人</th>
									<th>创建时间</th>
									<th>有效标识</th>
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
		       			"data" : "appraisaName"
		       		}, {
		       			"data" : "appraisaCode"
		       		}, {
		       			"data" : "creatUser"
		       		}, {
		       			"data" : "creatTime"
		       		},{
		       		    "data" :"validStatus"
		       		},{
		       		    "data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		
		$('td:eq(5)', row).html("<a  onclick='modifiyHospitalInfor("+data.id+");'><font size='3'>修改</font></a>");
		
		
	}		
	
	function modifiyHospitalInfor(param){//更新伤残机构信息
		
		index=layer.open({
		    type: 2,
		    title: false,
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['90%', '80%'],
		    content:["/claimcar/manager/AppraisaEdit.do?id="+param+"","yes"],
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	
	
	function add(){//添加伤残鉴定机构信息
		index=layer.open({
		    type: 2,
		    closeBtn: 1,
		    title: false,
	        shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['90%', '80%'],
		    content:["/claimcar/manager/addAppraisa.do","yes"],
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	
	
	
	
	
	
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/manager/appraisaFind.do";
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
