<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>重开赔案查询</title>
</head>
<body>
    <div class="page_wrap">
        <div class="table_wrap">
            <div class="table_cont pd-10">
                <div class="formtable f_gray4">
                    <form id="form" name="form" class="form-horizontal" role="form" method="post">
                        <div class="line"></div>
                        <div class="row mb-3 cl">
                            <label class="form-label col-3 text-c">立案号</label>
                            <div class="formControls col-3">
								<input type="text" class="input-text"  name="claimNo" 
								datatype="n22" ignore="ignore" errormsg="请输入22位的立案号"/>
							</div>
						</div>
						<div class="line"></div>
						<br>
						<div class="row cl text-c" >
								<button class="btn btn-primary btn-outline btn-search "
									onclick="search()" type="button" disabled>查询</button>
							
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
    function search(){
    	var claimNo=$("input[name='claimNo']").val();
    	index=layer.open({
		    type: 2,
		    title: "赔案信息表的查看页面",
		    shadeClose: true,
		    scrollbar: false,
		    skin: 'yourclass',
		    area: ['1000px', '550px'],
		    content:"/claimcar/reOpen/reOpenCaseView.do?claimNo="+claimNo+"",
		});
    }
    $(function() {
    	
	});
</script>
</body>
</html>