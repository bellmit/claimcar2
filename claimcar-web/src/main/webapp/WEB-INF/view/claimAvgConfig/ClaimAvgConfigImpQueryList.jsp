<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘任务处理查询</title>

</head>
<body>
	<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form name="fileform" action="importExcel.do" id="form" class="form-horizontal"
		enctype="multipart/form-data" method="post">
		<input type="hidden" value="${user.userName}" name="CreateUser">
					<div class="table_title f14">配置立案赋值案均</div>
					<div class="table_cont">
		<table width="100%" id="AddFile" border="0" align="center"
			cellpadding="5" cellspacing="1" class="common">
			<thead>
			</thead>
			<tbody>
			<div class="row mb-4 cl">
				<tr>
					<td>导入文件：</td>
					<td><input type='file' name='uploadFilezz' class="input-text"
						style="width: 100%">
					</td>
					
					<td class='input'><div id='AddFile_Td'>
				<!-- 	案均类型：
					<select name="riskClassFlag" class="select-box" style='width: 20%'>
						<option value="car" selected="selected">车险</option>
						<option value="ncar">非车险</option>
					</select> -->
					&nbsp;&nbsp;&nbsp;&nbsp;归属机构:
					<!--   <select name='comCode' class="select-box" style='width: 20%' >
						<option value =00>深圳</option><option value =10>广东</option><option value =11>广西</option><option value =12>云南</option><option value =13>贵州</option><option value =20>海南</option><option value =21>四川</option><option value =22>上海</option><option value =30>湖北</option><option value =50>河南</option><option value =60>江西</option>
					</select>    -->
				 	<span class="select-box" style='width: 20%'> <app:codeSelect 
										codeType="ComCodeSelect" type="select"  
										name="comCode" lableType="code-name" />
					</span>  
					&nbsp;&nbsp;&nbsp;&nbsp;年度：<input class="input-text" type="text" name='avgYear' onblur="effecttime();" style='width: 20%' maxlength="4" 
						 >
						<!-- onkeyup="value=value.replace(/[^\d]/g,'')" -->
					
				 	&nbsp;&nbsp;&nbsp;&nbsp;生效时间：
					<input name='avgTime' class="input-text" type="text" style='width: 20%' maxlength="4">
					</td> 
					</div>
					<td  class='input' >
						<div style="display: none"><input type="button" value=" 删  除 " class="btn btn-primary btn-outline btn-search" id = 'DelBtn' onclick='deleteitem(this,"AddFile");'></div>
						&nbsp;
					</td>
				</tr>
				</div>
			</tbody>
		
		</table>
		</div>
		
						<div class="row mt-15">
							<span class="col-offset-9 col-5">
								<button class="btn btn-primary btn-outline btn-search"
									name="submitBtn" value="导入案均" onclick="submitForm()" type="button">导入案均</button>
									<button class="btn btn-primary btn-outline btn-search"
									name='insertBtn' type="button" onclick="insertItem('AddFile')">新增文件</button>
									<button class="btn btn-primary btn-outline btn-search"
									name='dloadBtn' onclick="download()" type="button">下载配置模版</button>
							</span><br />
						</div>
		<table width="100%" border="0" align="center" cellpadding="5"
			cellspacing="1" class="common">
			<!-- <tr>
				<td class='input' align="right"><input type="button"
					class='button' name="submitBtn" value="导入案均" onclick="submitForm()">
				</td>
				<td class='input'><input type="button" class='button'
					name='insertBtn' value="新增文件" onclick="insertItem('AddFile')">
				</td>
			</tr>
			<tr>
				<td class='input' align="right"><input type="button"
					class='button' name='dloadBtn' value="下载配置模版" onclick="download()">
				</td>
				<td class='input'><input type="button" class='button'
					name='selectBtn' value="查询案均历史" onclick="selectHis()">
				</td>
			</tr> -->
			<tr>
				<td class='input' colspan="2"><b>规则说明：</b> <br> 1、请先下载模版；
					<br> 2、仅支持Excel文件导入，且Excel文件与模版格式一致； <br> 3、操作模版前需阅读填表说明。
				</td>
			</tr>
		</table>
	</form>
	</div>
	</div>
	</div>
	</div>
	<script type="text/javascript" src="/claimcar-main/src/main/webapp/js/common/DateUtils.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){ 
		var YS=new Date();
		document.getElementsByName("avgYear")[0].value=YS.getFullYear();
		effecttime();
	});
	var num = 1;
	/* var Date = new Date();
	var Year = Date.getFullYear(); */
	var Year = null;
	function submitForm() {
		 
		var avgYear = document.getElementsByName("avgYear");
		var comCode = document.getElementsByName("comCode");
		var avgTime = document.getElementsByName("avgTime");
		
		var uploadFile = document.getElementsByName("uploadFilezz");
		var riskClassFlag = document.getElementsByName("riskClassFlag");
		for ( var i = 0; i < uploadFile.length; i++) {
			var files = uploadFile[i];
			if (files.length != 0) {
				if (files.value == '') {
					alert("请选择导入文件!");
					return false;
				}
			}
		}
		for ( var k = 0; k < avgYear.length; k++) {
			var year = avgYear[k].value;
			var nowDate = new Date();
			if(year<nowDate.getFullYear()){
				alert("年度不能小于当前年度!");
				return false;
			}
			if (avgYear[k].value == '') {
				alert("年度不能为空!");
				return false;
			}
			if (comCode[k].value == '') {
				alert("机构不能为空!");
				return false;
			}
			if(year.length <4){
				alert("年度必须为四位整数!");
				return false;
			}
			for ( var j = 0; j < k; j++) {
				if (comCode[j].value == comCode[k].value
						&& avgYear[j].value == avgYear[k].value) {
					alert("同一机构同一年份的同一种案均类型请不要重复录入!");
					return false;
				}
			}
		}
		//showIframe("正在导入");
	/* 	var a = $("#form").serialize();
			 $.ajax({
	             type: "POST",
	             url: "importExcel.do",
	             data:a,
	             success: function(data){
	            	 		alert("导入成功！");
	                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	            			parent.layer.close(index); //再执行关闭 
	            			
	             }

	         });   */
			
		 fileform.submit();
	        
	         /* var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	 			parent.layer.close(index); //再执行关闭  */
	}

	function download() {
		/* var oldaction = fileform.action;
		fileform.action = '/claim/claimavgModelDL.do';
		fileform.submit();
		fileform.action = oldaction; */
		//fileform.action = 'exportComp.do';
		//alert("213");location.href 
		location.href="/claimcar/claimAvgList/exportComp.do";
	}

	function selectHis() {
		/* window.open("/claim/claimavgQuery.do?actionType=init",
					"NewWindow",
					"width=1024,height=768,top=0,left=0,toolbar=no,location=no,directories=no,menubar=no,scrollbars=yes,resizable=yes,status=no"); */
	}
	//添加导入行
	function insertItem(id) {
		var row, cell,cell2,cell3, str;
		var newTd =document.getElementById("AddFile_Td").cloneNode(true); 
		var delbtn = document.getElementById("DelBtn").cloneNode(true);

		row = eval("document.all[" + '"' + id + '"' + "]").insertRow();
		if (row != null) {
			cell = row.insertCell();
			cell.className = 'title';
			str = "导入文件：";
			cell.innerHTML = str;
			cell = row.insertCell();
			cell.className = 'input';
			str = "<input type=" + '"' + "file" + '"' + " class=" + '"'
					+ "input-text" + '"' + " style=" + '"' + "width:100%" + '"'
					+ " name="+ '"' + "uploadFilezz" + '"'+">";
			cell.innerHTML = str;
			cell2 = row.insertCell();
			cell2.className = 'input';
			cell2.appendChild(newTd);  
			cell3 = row.insertCell();
			cell3.className = 'input';
			cell3.appendChild(delbtn);  
		}
		num++;
	}
	function deleteitem(obj, id) {
		var rowNum, curRow;
		curRow = obj.parentNode.parentNode;
		rowNum = eval("document.all." + id).rows.length - 1;
		eval("document.all[" + '"' + id + '"' + "]").deleteRow(curRow.rowIndex);
		var inputList = document.getElementsByTagName("input");
	}
	
		function effecttime(){
			var s = document.getElementsByName("avgYear");
			 var d = new Date().addDays(0);
			 d = d.format('yyyy:MM:dd 00:00:01'); 
		var YS=new Date();
		
		/* var s = document.getElementsByName("avgYear")[0].value+"-01-01 00:00"; */
		for(var i=0;i<s.length;i++){
			if(document.getElementsByName("avgYear")[i].value==YS.getFullYear()){
				document.getElementsByName("avgTime")[i].value=d;
			}else{
			document.getElementsByName("avgTime")[i].value=document.getElementsByName("avgYear")[i].value+":01:01 00:00:01";			
		
			}
		}
		}
	
</script>
</body>

</html>
