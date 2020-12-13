<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>医院信息查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-1">医院代码代码</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="prpDHospitalVo.hospitalCode"/>
							</div>
							
							<label for=" " class="form_label col-1">医院名称</label>
							<div class="form_input col-3">
								<input id=" " type="text" class="input-text" name="prpDHospitalVo.hospitalCName"/>
							</div>
							
							<label for=" " class="form_label col-1">有效标志</label>
							<div class="form_input col-3">
					 			<span class="select-box">
									<select id="validStatus" class="select" name="prpDHospitalVo.validFlag">
										<option value="">所有</option>
										<option value="1">有效</option>
										<option value="0">无效</option>
									</select>
								</span>
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
									<th>医院名称</th>
									<th>医院代码</th>
									<th>医院等级</th>
									<th>联系人</th>
									<th>联系人电话</th>
									<th>联系人手机</th>
									<th>编辑</th>
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
	<!-- 	<div class="table_wrap text-c">			
			<button class="btn btn-primary btn-outline btn-search mt-20" onclick="modifiInterm()" type="button">修改</button>
		</div> -->
		
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
		       			"data" : "hospitalCName"
		       		}, {
		       			"data" : "hospitalCode"
		       		}, {
		       			"data" : null
		       		}, {
		       			"data" : "contractName"
		       		},{
		       			"data" : "contractTel"
		       		},
		       		{
		       			"data" : "contractMobile"
		       		},
		       		{
		       			"data" : null
		       		},
		       		{
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {	
		$('td:eq(1)', row).html("<a  onclick='show("+data.id+");'>"+data.hospitalCode+"</a>");
		$('td:eq(6)', row).html("<a  onclick='modifiyHospitalInfor("+data.id+");'>修改</a>");
		$('td:eq(2)', row).html(data.hospitalLevel+data.hospitalClass);
		if(data.validFlag === '1'){
			$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'   onclick='Cancelled("+data.id+",0"+");'>注销</button><button class='btn btn-primary btn-outline btn-search'");
		}else{
			$('td:eq(7)', row).html("<button class='btn btn-primary btn-outline btn-search'   onclick='Cancelled("+data.id+",1"+");'>激活</button><button class='btn btn-primary btn-outline btn-search'");
		}
		
	}		
	function modifiCheck(event){
		modifiId = $(event).val();
		alert(modifiId);
	}

	function modifiyHospitalInfor(param){//跟新医院信息
		
		index=layer.open({
		    type: 2,
		    title: false,
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1100px', '550px'],
		    content:["/claimcar/manager/saveOrUpdateHospital.do?id="+param+"","yes"],
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function show(param){
		
		index=layer.open({
		    type: 2,
		    title:false,
		    closeBtn: 2,
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1100px', '550px'],
		    content:"/claimcar/manager/show.do?id="+param+"",
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	
	function add(){//添加医院信息
		index=layer.open({
		    type: 2,
		    closeBtn: 1,
		    title: false,
	        shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1100px', '550px'],
		    content:["/claimcar/manager/save.do","no"],
		    end:function(){
		    	$("#search").click();
		    }
		});
	}
	/**
	*点击激活或者注销
	*/
	function Cancelled(id,states){
		var str;
		if(states == '0'){
			str = "确定要注销？";
		}else{
			str = "确定要激活？";
		}
		layer.confirm(str, {
		    btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				url:"/claimcar/manager/hospitalCancelActive.do",
				type:"post",
				data:{"id":id,"states":states},
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
	
	function decrease(id){
		index=layer.open({
		    type: 2,
		    title: '查看机构信息',
		    closeBtn: 1,
		    shadeClose: true,
		    scrollbar: true,
		    skin: 'yourclass',
		    area: ['900px', '550px'],
		    content:"/claimcar/manager/intermediaryView.do?Id="+id+"",
		    
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
				ajaxList.targetUrl = "/claimcar/manager/hospitalFind.do";
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
