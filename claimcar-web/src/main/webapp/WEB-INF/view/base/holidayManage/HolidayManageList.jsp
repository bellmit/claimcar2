<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>休假申请</title>
</head>
<body>
    <div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					    <!-- 隐藏域 -->
					        <input type="hidden" name="flag_add" value="0"/>
		                    <input type="hidden" name="flag_dispose" value="1"/>
		                <!-- 隐藏域 -->
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">员工工号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLUserHolidayVo.userCode" 
								value="${userCode }" readonly="readonly"/>
							</div>
							<label class="form-label col-1 text-c">员工姓名</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="prpLUserHolidayVo.userName" 
								value="${userName }" readonly="readonly"/>
							</div>
							
							<label class="form-label col-1 text-c">归属机构</label>
							
							<div class="formControls col-3">
							    <span class="select-box"> 
									<app:codeSelect codeType="ComCode" name="prpLUserHolidayVo.comCode" id="comCode"
											 type="select" value="${comCode}" disabled="disabled" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<div class="row cl text-c" >
							
								<button class="btn btn-primary btn-outline btn-search "
									id="search" type="button" disabled>查询</button>
								<button class="btn btn-primary btn-outline btn-search "
									onclick="add()" type="button">增加</button>
							
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
									<th>员工工号</th>
									<th>员工姓名</th>
									<th>归属机构</th>
									<th>岗位</th>
									<th>手机号码</th>
									<th>审核状态</th>
									<th>撤销时间</th>
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
    var today=new Date();
    var columns = [
		       		{
		       			"data" : "userCode",
		       				"orderable" : true,
			       			"targets" : 0
		       		}, {
		       			"data" : "userName"
		       		}, {
		       			"data" : "comCodeName"
		       		}, {
		       			"data" : "prpLUserHolidayGrade.gradeName"
		       		},{
		       			"data" : "mobileNo"
		       		},{
		       			"data" : "checkStatusName"
		       		},{
		       			"data" : "cancelTime"
		       		}
		       		,{
		       			"data" : null
		       		}
		       	  ];
    
    function rowCallback(row, data, displayIndex, displayIndexFull) {		
    	if(data.status=='1'){
    		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose("+data.id+");'>查看</button> <button class='btn btn-primary btn-outline btn-search'  onclick='cancel("+data.id+");'>撤销</button>");
    	}else{
    		$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'  onclick='dispose("+data.id+");'>查看</button>");
    	}
}	
    
    function dispose(hid){
    	index=layer.open({
		    type: 2,
		    title: "休假申请查看",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/holidayManage/holidayManageEdit.do?hid="+hid+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
    }
    
    function cancel(hid){
    	index=layer.open({
		    type: 2,
		    title: "撤销休假",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['850px', '300px'],
		    content:"/claimcar/holidayManage/cancelInit.do?hid="+hid+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
    }
    
    function add(){
    	var flag="";
    	$.ajax({
    		url:"/claimcar/holidayManage/judge.do",
    		type:'post',
    		dataType:'json',
    		async:false,
    		success:function(result){
    			var res=result.data;
    			flag=res;
    		}
    	});
    	if(flag=="f"){
    		layer.msg("您已有申请未审核！");
    	}else{
    		index=layer.open({
    		    type: 2,
    		    title: "休假申请新增",
    		    shadeClose: true,
    		    scrollbar: false,
    		    skin: 'yourclass',
    		    area: ['1000px', '550px'],
    		    content:"/claimcar/holidayManage/holidayManageAddEdit.do",
    		    end:function(){
    		    	$("#search").click();
    		    }
    		});
    	}
    }
    
    $(function(){
    	$("#comCode").prop("readonly",true);
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/holidayManage/holidayManageFind.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		$("#search").click(function(){
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.query();
		});
	});
	</script>
			
</body>
</html>