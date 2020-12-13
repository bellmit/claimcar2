<%@page import="java.util.Enumeration"%>
<%@page import="java.lang.reflect.InvocationTargetException"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="ins.framework.exception.BusinessException"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%
String title = ""; //信息
String content = ""; //详细信息
StringWriter stringWriter = new StringWriter();

if(exception==null){
	exception = (Throwable)request.getAttribute("javax.servlet.error.exception");
}

if(exception!=null){
	Throwable throwable = null;
	if(exception instanceof ServletException){
		throwable = ((ServletException)exception).getRootCause();
	}else {
		throwable = exception;
	}
	if(throwable instanceof BusinessException){
		throwable = (BusinessException)throwable;
	}
//	else if(throwable instanceof DelegationException){ 
//		throwable = throwable.getCause();
//	} 
	title = throwable.getMessage();
/* 	if(throwable instanceof PermissionException) {
	    throwable=(PermissionException)throwable;
	    title="您没有此功能的操作权限，请与管理员联系！";
	} */
	throwable.printStackTrace(new PrintWriter(stringWriter));
}
%>
<html>
<head>
<title>Error Page</title>

<style>
	td{font-size:9pt;}
.button_ty,.button_ty_over{color:#000;border:1px solid #94D8E4;padding:1px 5px 1px 5px;height:20px;}
.button_ty{background: #fff url(${ctx}/pages/image/btbg_blue.gif) repeat-x left left -2px;}
.button_ty_over{background: #fff url(${ctx}/pages/image/btbg_orange.gif) repeat-x left left -2px;}
</style>
<script language=javascript>
function shContent()
{
  if(content.style.display=='')
    content.style.display = 'none';
  else
    content.style.display = '';
}

function closeIFrame() {
	if(document.parentWindow.name=="msgIFrame"){
	  var ifr = document.parentWindow.parent.document.getElementById("msgIFrame"); 
		document.parentWindow.parent.document.body.removeChild(ifr);
	}
}

function loadBody(){
  if(document.parentWindow.name=="msgIFrame"){
    trCloseButton.style.display = "";
  }
}
function closeMethod(){
		parent.location.reload();
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
		
		
/*        //当父窗口需要关闭的时候，增加该参数即可
       if(window!=null&&window.parent!=null&&window.parent.document!=null
                            &&window.parent.document.getElementById('closeFromChild')!=null){
		    if(window.parent.document.getElementById('closeFromChild').value=='1'){
		       parent.window.close();
		       return;	
		    }
	    }
  if(parent!=null && parent.window!=null){
    if(parent.submitDlg!=null){
      if(parent.submitDlg.hide!=null){
           parent.submitDlg.hide();
        }else{
           parent.submitDlg.visible=false;
        }
     
       // PNC-8997 互碰自赔案件,如果查勘提交不符合条件，关闭提示信息后，最下方一排按钮全部置灰，建议关闭提示信息后刷新页面。
       if(parent.reloadPage!=undefined && parent.reloadPage!=null){
       	if(window.parent.document.getElementById('autoCompensateFlag') != undefined 
	       	&& window.parent.document.getElementById('autoCompensateFlag').value != null 
	       	&& window.parent.document.getElementById('autoCompensateFlag').value =='1'){
	       		 parent.window.close();
       	}else{
	 	  	parent.reloadPage();
       	}
 	  }
    }
 	else{
  //modify by sunjianliang begin 20090316：此处用close不合理
      if(window.opener != null){
 	    window.close();
      }else{
        window.location="about:blank";
      }
  //modify by sunjianliang end 20090316

 	}
  }else{
   	window.location="about:blank";
  } */
}
function refreshMethod(){
        //当父窗口需要关闭的时候，增加该参数即可
        if(window!=null&&window.parent!=null&&window.parent.document!=null
                            &&window.parent.document.getElementById('closeFromChild')!=null){
			    if(window.parent.document.getElementById('closeFromChild').value=='1'){
			       parent.window.close();
			       return;
			    }
	    }
	if(parent!=null && parent.window!=null){
		if(parent.submitDlg!=null){
		 if(parent.submitDlg.hide!=null){
               parent.submitDlg.hide();
           }else{
              parent.submitDlg.visible=false;
        }
			// PNC-8997 互碰自赔案件,如果查勘提交不符合条件，关闭提示信息后，最下方一排按钮全部置灰，建议关闭提示信息后刷新页面。
			if(parent.reloadPage!=undefined && parent.reloadPage!=null){
				if(window.parent.document.getElementById('autoCompensateFlag') != undefined 
	       			&& window.parent.document.getElementById('autoCompensateFlag').value != null 
	       			&& window.parent.document.getElementById('autoCompensateFlag').value =='1'){
		       			 parent.window.close();
		       	}else{
			 	  	parent.reloadPage();
		       	}
		 	}
		}else{
  //modify by sunjianliang begin 20090316：此处用close不合理
      if(window.opener != null){
        parent.window.close();
      }else{
        history.go(-1);
      }
  //modify by sunjianliang end 20090316
		}
	}
}

</script>
</head>
<body onload="loadBody()">

  <table class=common align=center>
    <tr>
      <td class=formtitle colspan="2">系统提示</td>
    </tr>
    <tr>
      <td align=center>
        <img src="/claimcar/images/failure.gif"
          style='cursor:hand' alt='详细信息' onclick="shContent()">
      </td>
      <td class="common">
        <%=title%>
      </td>
    </tr>
    <tr id="trCloseButton" >
      <td colspan="2" align="center">
		<input type="button" class="btn btn-primary"  value=" 关 闭 " onclick="closeMethod();" class="button_ty">
		<!-- <input type="button" value=" 返 回 " onclick="refreshMethod();" class="button_ty"> -->
      </td>
    </tr>

  </table>

<div id="content" style="display:none">
	<pre><%=stringWriter%></pre>
	<table border="1">
		<tr>
			<th>request.getAttributeName</th>
			<th>request.getAttribute</th>
		</tr>
<%
		Enumeration enums =  request.getAttributeNames();
		while(enums.hasMoreElements()){
			String key = (String)enums.nextElement();
			out.println( "<tr><td>");
			out.println( key );
			out.println( " </td><td>");
			out.println( request.getAttribute(key) );
			out.println( " </td></tr>");
		}
%>
	</table>

</div>
</body>
</html>