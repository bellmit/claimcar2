<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>导出Excel</title>
</head>
<body>
    <form name="fileForm" action="exportExcel.do" id="form" class="form-horizontal"
		enctype="multipart/form-data" method="post">
         <div class="table_cont">
			<div class="table_wrap">
			 <div class="formtable">
			      <div class="row cl">
			          <label class="form_label col-5">选择文件：</label>
			              <div class="form_input col-3">
								<input type="file" class="input-text"
									name="exportFile"  datatype="*" nullmsg="请选择导出文件位置！"/>
									
							</div>
							
							<div class="row mt-15">
							    <span class="col-offset-5 col-5">
								<button class="btn btn-primary btn-outline btn-search mt-20 mb-20"
									name="submitBtn" value="导出" onclick="submitExport()" type="button">导出</button>
							    </span>
							</div>
							
				  </div>
			 </div>
			</div>
		</div>
	</form>
	
	<script type="text/javascript">
	    function submitExport(){
	    	var exportFile = document.getElementsByName("exportFile");
	    	for ( var i = 0; i < exportFile.length; i++) {
				var files = exportFile[i];
				if (files.length != 0&&files.value == '') {
					alert("请选择导出文件位置!");
					return false;
				}
			}
	    	
	    	fileForm.submit();
	    }
</script>
</body>
</html>