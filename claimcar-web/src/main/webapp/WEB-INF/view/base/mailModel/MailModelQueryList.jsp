<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>邮件查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-2">
								<input type="text" class="input-text" datatype="n4-22"
									ignore="ignore" errormsg="请输入4到22位数"
									name="prpsmsEmailVo.businessNo" />
							</div>
								<label class="form-label col-2 text-c">
								归属机构
						</label>
						<div class="formControls col-2">	
							<span class="select-box">
								<c:choose>
									  <c:when test="${fn:startsWith(userComCode, '0000')}">
											<app:codeSelect
											codeType="ComCodeLv2" type="select" 
											lableType="code-name" clazz="must" 
											name="prpsmsEmailVo.comCode"  />
											<font class="must">*</font>
										</c:when>
										 <c:when test="${fn:startsWith(userComCode, '0001')}">
											<app:codeSelect
											codeType="ComCodeLv2" type="select" 
											lableType="code-name" clazz="must" 
											name="prpsmsEmailVo.comCode"  />
											<font class="must">*</font>
										</c:when>
										<c:otherwise>
											<input type="hidden" name="prpsmsEmailVo.comCode" value="${userComCode}" /> 
											<app:codeSelect
											codeType="ComCodeLv2" type="select" 
											lableType="code-name" clazz="must" 
											name="prpsmsEmailVo.comCode"
											value="${userComCode}"   disabled="true"  />
											<font class="must">*</font>
										</c:otherwise>
							  </c:choose>
							</span>
						</div>
						
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">
								邮箱地址
							</label>
							<div class="formControls col-2">
								<input type="text" class="input-text"  name="prpsmsEmailVo.email"  />
							</div>
								<label class="form-label col-2 text-c">发送时间</label>
							<div class="formControls col-4">
								<input name="prpsmsEmailVo.createTimeStart" id="startDate"
									value="<fmt:formatDate value='${startDate}'  pattern='yyyy-MM-dd HH:mm' />"
									datatype="*" type="text" class="Wdate wd96"
									onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})"/>
									<span class="datespt">-</span>
									<input name="prpsmsEmailVo.createTimeEnd" id="endDate"  value="<fmt:formatDate value='${endDate}'  pattern='yyyy-MM-dd HH:mm' />" 
								datatype="*" type="text" class="Wdate wd96" 
								onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})"/>
								<font color="red">*</font>
						    </div>   
						</div>
					<div class="line"></div>
					<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询</button> 
							</span>
					</div>
					</form>
				</div>
			</div>
		</div>
	</div>
			<br />
			<!-- 查询条件结束 -->

			<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>邮箱地址</th>
									<th>发送时间</th>
									<th width="40%" class="text-c">发送内容</th>
									<th>发送状态</th>
								</tr>
							</thead>
							<tbody class="text-c">
							
							</tbody>
						</table>
						<!--table   结束-->
						
						
					</div>
				</div>
			</div>
			<!--标签页 结束-->
	
   
  
<!-- 此处放页面数据 -->
<script type="text/javascript" src="/claimcar/plugins/ajaxfileupload/ajaxfileupload.js"></script>
<script type="text/javascript">
    $(function() {
    	$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");		
			bindValidForm($('#form'), search);
			});

			var columns = [
		       		 {
		       			"data" : "email",  

		       		}, {
		       			"data" : "createTime",
		       		}, {
		       			"data" :"sendText",
		       		}, {
		       			"data" : "status"
		       		}
		       	  ];

			function rowCallback(row, data, displayIndex, displayIndexFull) {
			
			$("#sendText").val(data.sendText);
			if(data.sendText.length>25){
			 sendContext=data.sendText.substring(0,26)+'......';
			}else{
			 sendContext=data.sendText; 
			}
			$('td:eq(2)',row).html("<input class='text-c' name='"+data.sendText+"' style='width:80%;height:30px;border:none' readonly='true' onmouseover=showSendText(this) value='"+sendContext+"'/>");
			
			}

		   function search() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = "/claimcar/mailModel/mailModelFind.do";
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}

	//yzy	
    function showSendText(obj){
      var sendTextcontext=$(obj).attr("name");
      if(sendTextcontext.length<=25){
    	  
         return false;
      }
       $(obj).qtip({
           content:sendTextcontext,
          });
    	obj.onmouseover = null;
    	$(obj).trigger('mouseover');//手动触发mouseover事件
    	
    }
	</script>

</body>
</html>
