<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>赔偿标准查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row cl accident-over-add">
						<label class="form_label col-1"></label>
						<label class="form_label col-1">地区(省市)</label>
					    <div class="form_input col-5 clearfix">
					    <input type="hidden" id="conFlag" value="${conFlag}"/>
						<div style="display:inline">
							<app:areaSelect targetElmId="damageAreaCode" areaCode="${prplFeeStandardVo.areaCode}" showLevel="2"/>
						</div>
						<input type="hidden" id="damageAreaCode" name="prplFeeStandardVo.areaCode" value="${prplFeeStandardVo.areaCode}"/>
						</div>
						   <label class="form-label col-1 text-c">年份</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<select id="myYear" name="prplFeeStandardVo.yearCode" style="width:80px;height:25px;">
								</select>&nbsp;月份
								<select id="mymonth" name="prplFeeStandardVo.monthCode" style="width:60px;height:25px;">
								</select>
								<font color="red">*</font>
						  </div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button">查询</button>
							   <c:if test="${conFlag eq 'add'}">
							   <button class="btn btn-primary btn-outline btn-noPolicy ml-20" onclick="add()" type="button">新增</button>
							   </c:if>
					           

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
									<th>地区</th>
									<th>时间</th>
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
	
	<script type="text/javascript">
	var index;
	var id;	
	var modifiId=0;
	var columns = [
		       		 {
		       			"data" : "areaName"
		       		}, {
		       			"data" : "dateCode"
		       		}, {
		       			"data" : null
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var conFlag=$("#conFlag").val();
		if(conFlag=='add'){
			$('td:eq(2)', row).html("<a class='btn btn-primary' onclick='show("+data.imageBussNo+");'><font size='2'>查看</font></a>&nbsp;&nbsp;<a class='btn btn-primary' onclick='deleteImage("+data.imageBussNo+");'><font size='2'>删除</font></a>");
		}else{
			$('td:eq(2)', row).html("<a class='btn btn-primary' onclick='show("+data.imageBussNo+");'><font size='2'>查看</font></a>");
		}
		
	}		

	function show(imageBussNo){
		
		/* index=layer.open({
		    type: 2,
		    title:false,
		    closeBtn: 2,
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1100px', '500px'],
		    content:"/claimcar/payfeeInfo/imageShowView.do?id="+id,
		    end:function(){
		    }
		    
		}); */
		var imageUrl = "/claimcar/payfeeInfo/imageShowView.do?imageBussNo="+imageBussNo;
		openTaskEditWin('人伤赔偿标准查看', imageUrl);
	}
	
    //新增
	function add(){
		var ce_index;
			var url = "/claimcar/payfeeInfo/addFeeStandardEdit.do";
			if (ce_index != null) {
				layer.close(ce_index);
			} else {
				ce_index = layer.open({
					type : 2,
					title : false,
					shade : false,
					area : [ '60%', '70%' ],
					content : url,
					scrollbar: true,
					end : function() {
						ce_index = null;
					}
				});
			}
		
	}
	
	$(function(){
		window.onload=function(){ 
			//设置年份的选择 
			var myDate= new Date(); 
			var startYear=myDate.getFullYear()-50;//起始年份 
			var endYear=myDate.getFullYear()+50;//结束年份 
			var obj=document.getElementById('myYear');
			var flag="0";
			for (var i=startYear;i<=endYear;i++) 
			{ 
			if(flag=="0"){
				obj.options.add(new Option('',''));
				flag="1";
			}else{
				obj.options.add(new Option(i,i));
			}
			
			}
			
			};
			
		$("#myYear").click(function(){
			var yearValue=$(this).val();
			if(yearValue != null && yearValue != "undefined" && yearValue != "" && yearValue != "null"){
				//设置月份的选择
				var startMonth=0;//起始月份
				var endMonth=12;//结束月份
				var monthObj=document.getElementById('mymonth');
				var monthFlag="0";
				$("#mymonth").empty();
				for(var i=startMonth;i<=endMonth;i++){
					if(monthFlag=="0"){
						monthObj.options.add(new Option('',''));
						monthFlag="1";
					}else{
						monthObj.options.add(new Option(i,i));
					}
					
				}
			}else{
				$("#mymonth").empty();
				
			}
		});
		$("#damageAreaCode_lv1").attr("name","feeStandrad");
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#search").click(
			function() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/payfeeInfo/feeStandardList.do";
				ajaxList.postData=$("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
		);	
	});
	
	
	
	
	function deleteImage(imageBussNo){
		layer.confirm('确定要删除吗?', {
			btn : [ '是', '否' ]
			}, function(index) {
				var url = "/claimcar/payfeeInfo/deleteFeeStandard.do";
				var params = {
					"imageBussNo" : imageBussNo
				};
				$.ajax({
					url : url,
					data : params,
					type : 'post',
					async : true,
					success : function(result) {
						if (eval(result).status == "200") {// 成功
							layer.msg("删除成功！", { shift: -1,time: 1500 }, function () {
								window.location.reload();
								});
						}else{
							layer.msg(eval(result).statusText,{time : 2000});
							
						}
					},
					error : function() {
						layer.msg("获取地址数据异常");
					}
				});
			}, function(index) {
				layer.close(index);
			});
		
	}
	
	
	
	</script>
</body>
</html>
