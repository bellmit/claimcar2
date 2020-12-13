<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>导入Excel</title>
</head>
<body>
     
     <form name="fileForm" action="importExcel.do" id="form" class="form-horizontal"
		enctype="multipart/form-data" method="post">
         <div class="table_cont">
			<div class="table_wrap">
			 <div class="formtable">
			      <div class="row cl">
			          <label class="form_label col-5">选择要导入的excel文件：</label>
			              <div class="form_input col-3">
								<input type="file" class="input-text"
									name="importFile"  datatype="*" nullmsg="请选择导入文件名！"/>
									
							</div>
							
							<div class="row mt-15">
							    <span class="col-offset-5 col-5">
								<button class="btn btn-primary btn-outline btn-search mt-20 mb-20"
									name="submitBtn" value="导入" onclick="submitImport()" type="button">导入</button>
							    </span>
							</div>
							
				  </div>
			 </div>
			</div>
		</div>
	</form>
	
	<script type="text/javascript">
	    function submitImport(){
	    	var importFile = document.getElementsByName("importFile");
	    	for ( var i = 0; i < importFile.length; i++) {
				var files = importFile[i];
				if (files.length != 0&&files.value == '') {
					alert("请选择导入文件名!");
					return false;
				}
			}
	    	
	    	$("#form").submit();
	    }
</script>
</body>
</html>