<%@page import="java.lang.reflect.InvocationTargetException"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.net.SocketException"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>:::系统信息:::</title>
 
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);

	String expType = "sys";//系统提示user 系统异常sys
	String expMsg = "未知异常";
	String expTrace = null;
	String expImage="Error_sys.gif";
	
	Object expObj = request.getAttribute("ex");
	if(expObj==null)expObj = request.getAttribute("javax.servlet.error.exception");
	//System.out.println("==exception=="+exception.getCause());
 	if(expObj==null&&exception!=null){
 		expObj=exception.getCause();
 		if(expObj!=null){
	 	 	if(expObj instanceof ServletException){
	 			ServletException svlexp=(ServletException) expObj;
	 			expObj=svlexp.getRootCause();
	 		} 
 		}else{
 			expObj=exception;
 		}
 	}
 	
 	if(expObj instanceof InvocationTargetException){
 		expObj=((InvocationTargetException)expObj).getCause();
 	}
 	//System.out.println("==expObj=="+expObj.toString());

 		StringWriter sout = new StringWriter();
 		PrintWriter pout=new PrintWriter(sout);
		if(expObj!=null){
			Throwable sysexp = (Throwable) expObj;
			expMsg = sysexp.getMessage();
			if(expMsg==null)expMsg=expObj.toString();
			if(exception!=null)expMsg="【JSP】" +expMsg;
			sysexp.printStackTrace(pout);
		}else{
			expMsg="【系统异常】无相关异常信息";
			expTrace="无相关堆栈信息";
		}
	if(expTrace==null)expTrace = sout.toString();
	if(expTrace==null)expTrace="无相关堆栈信息";
	if(expMsg==null)expMsg="【系统异常】"+expObj.toString();
	if(expMsg.length()>256)expMsg=expMsg.substring(0,256)+"......";
	session.setAttribute("exp_msg",expMsg);
	
	if(expMsg.indexOf("<")>-1){
		expMsg=expMsg.replaceAll("<","&lt;");
		expMsg=expMsg.replaceAll(">","&gt;");
	}
	if(expTrace.indexOf("<")>-1){
		expTrace=expTrace.replaceAll("<","&lt;");
		expTrace=expTrace.replaceAll(">","&gt;");
	}
	session.setAttribute("exp_Trace",expTrace);
	
	//#5cb096
%>
 
<style type="text/css">
BODY{
 	FONT-SIZE: 9pt;
}
TABLE{ FONT-SIZE: 9pt}
.dialogBorder{border-style: outset;border-width: 1px;border-color: #CCCCCC;background-color:gray ;width:650px;}
.dialogTitleBar{color: #FFFFFF;height: 20px;background-color: gra;cursor: move;padding-left: 7px;padding-right: 7px;
	padding-top: 5px;padding-bottom: 1px;position: relative;font-weight:bold;font-size:12px;text-align:left;}
.dialogBackground{background-color: #ECE9D8;	}
.dBackground{margin-left: 10px;margin-right: 10px;padding-top: 4px;}
.dialog_body{border-width:1px;border-style:inset;width:100%;height:100%;font-family:arial;font-size:8pt;}
.exception_dialog{border-style:solid;border-color:#cccccc;background-color:#ffffef;border-width:1px;} 
.exception_dialog_message{	padding: 2px;font-size: 10pt;font-weight:bold;}
.exception_dialog_label{cursor: pointer;font-size: 8pt;color: gray;font-weight: bold;text-decoration: underline;}
.exception_dialog_container{border-left-style:solid;border-left-width:1px;border-left-color:#cccccc;} 
.exception_dialog_detail{padding:2px;overflow:auto; height:170px;width:570px;border-top-color:#cccccc;
	border-top-style:solid;border-top-width:1px;font-size:8pt;font-family:inherit;direction: ltr;text-align: left;}
.dialogBtnBar{height: 30px;text-align: right;}
.dialogBtnBarButton{float: right;cursor: pointer;height: 24px;width: 80px;overflow: visible;color: black; 
	padding-left: 7px;padding-right: 7px;}
.dTitleTextContainer{float: left; position: relative;height: 100%;	}
.dCloseBtnContainer{height: 100%;float: right;position: relative;}
</style>
<script type="text/javascript">
function showTraceDetail(obj){
	var disp=exceptionTraceContainer.style.display;
	if(disp==""){
		exceptionTraceContainer.style.display="none";
		obj.innerHTML="显示堆栈跟踪信息 ";
	}else{
		exceptionTraceContainer.style.display=""
		obj.innerHTML="隐藏堆栈跟踪信息 ";
	}
	
}
function moveDialog(obj){
	var dialogTop=obj.style.pixelTop;
	var dialogLeft=obj.style.pixelLeft;
	obj.style.pixelLeft=dialogTop+event.clientY;       
	obj.style.pixelTop=dialogLeft+event.clientX;       
}

function MoveStart(moveId,actionID)
{
	var oObj = document.getElementById(moveId);
	var actionObj = document.getElementById(actionID);
	var oEvent,dragData,backData;
	//赋予对象拖动操作
	actionObj.onmousedown=mousedown;
	function mousedown(event)
	{
		actionObj.style.cursor="move";
	oEvent = window.event ? window.event : event;
	//保留鼠标位置
	dragData = {x : oEvent.clientX, y : oEvent.clientY};
	//保留对象位置
	backData = {x : parseInt(oObj.style.top), y : parseInt(oObj.style.left)};
	//捕获鼠标移动
	actionObj.setCapture ? actionObj.setCapture() : function(){};
	actionObj.onmousemove=mousemove;
	actionObj.onmouseup = mouseup;
	}

	//鼠标移动
	function mousemove(event)//获取窗口事件
	{
	oEvent = window.event ? window.event : event;

	//计算提示框位置
	var iLeft = oEvent.clientX - dragData["x"] + parseInt(oObj.style.left);
	var iTop = oEvent.clientY - dragData["y"] + parseInt(oObj.style.top);

	//设置提示框位置
	//设置IE屏蔽层位置
	oObj.style.left=iLeft+"px";//
	oObj.style.top=iTop+"px";//
	//刷新保留鼠标位置
	dragData ={x: oEvent.clientX, y: oEvent.clientY};
	}

	//鼠标键抬起
	function mouseup(event)
	{
	//获取窗口事件
	oEvent = window.event? window.event : event;
	//清除对象拖动操作
	actionObj.onmousemove = null;
	actionObj.onmouseup = null;

	//如果鼠标是否超出浏览器范围
	if(oEvent.clientX < 1 || oEvent.clientY < 1 || oEvent.clientX > document.body.clientWidth || oEvent.clientY > document.body.clientHeight)
	{
	//还原提示框位置
	//还原IE屏蔽层位置
	oObj.style.left=backData.y+"px";//
	oObj.style.top=backData.x+"px";//
	}
	//关闭捕获鼠标移动 
	actionObj.releaseCapture ? actionObj.releaseCapture() : function(){};
	actionObj.style.cursor="";
	}
}
function CloseExpDialog(){
	exceptionDialog.style.display="none";
}
</script>
</head>
<body >
<div id="TB_window"> 
  <form name="Errorfm" action="#" method="post"  >
<input type="hidden" name="requestURL" value="">
<input type="hidden" name="priority" value="1">
 <div id="exceptionDialog" class="dialogBorder" style="position:absolute;top:100;left:200; z-index:220;display: "   >
 	<iframe id="exceptionDialogiframe"  name="exceptionDialogiframe" style="z-index:-1; display: none; left:0px; top:0px;
					 background-color: #ff0000; opacity: .0; filter: alpha(opacity = 0); position: absolute;" frameBorder="0" scrolling="no" src="">
	</iframe>
 	<div  id="dialogTitle" class="dialogTitleBar">
 	<div class="dTitleTextContainer"><%=(expType.equals("user")?"系统提示":"系统信息") %></div>
 	<div class="dCloseBtnContainer"> 
 	<img alt="关闭" src="/claimcar/images/Error_close.gif" style="cursor: pointer;" onclick="CloseExpDialog()">  </div>
 	</div>
 	<div class="dialogBackground" style="overflow: auto;"> 
	<div class="dBackground"> 
	<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="dialog_body">
		<TR>
		<TD CLASS="exception_dialog">
		
		<TABLE CELLSPACING="2" CELLPADDING="2">
				<TR>
					<TD VALIGN="top"><IMG SRC="/claimcar/images/<%=expImage %>" /></TD>
					
					<TD>
					
						<TABLE CELLSPACING="2" CELLPADDING="4" CLASS="exception_dialog_container" >
							<TR>
								<TD>
								<DIV ID="faultStringContainer" CLASS="exception_dialog_message">
								<%=expMsg %>
								</DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<DIV ID="showTraceLabel" CLASS="exception_dialog_label" onclick="showTraceDetail(this)">
										显示堆栈跟踪信息
									</DIV>																				
															
								</TD>
							</TR>
							<TR>
								<TD>
									<DIV ID="exceptionTraceContainer" STYLE="display:none">
										<TABLE WIDTH="100%">
											<TR>
												<TD>
													堆栈跟踪:<BR>
												</TD>
											</TR>
											<TR>
												<TD> 
													<DIV CLASS="exception_dialog_detail">
														<pre><%=expTrace %></pre>
													</DIV>
												</TD>
											</TR>											
										</TABLE>
									</DIV>
								</TD>
							</TR>
						</TABLE>
					
					</TD>
					
				</TR>
			</TABLE>
					
		</TD>
		</TR>
	</TABLE>
<hr />
<div class="dialogBtnBar">  
<input type="button" class="dialogBtnBarButton" value="返 回" onclick="javascript:history.back()" />
</div>

	</div>
 	</div>

 </div>
 </form>
 </div>
</body>
<script type="text/javascript">
function initPosition(){
	var CONTAINER_HEIGHT=(document.body.clientHeight);//container高度
	var CONTAINER_WIDTH=(document.body.clientWidth);//container宽度

	var dialogTop=CONTAINER_HEIGHT/2-200;
	var dialogLeft=CONTAINER_WIDTH/2-360;
	if(dialogTop<10)dialogTop=10;
	if(dialogLeft<10)dialogLeft=10;
	exceptionDialog.style.top=dialogTop;
	exceptionDialog.style.left=dialogLeft;
	
}
initPosition();


var wartTime=1;
function showErrorWindow(){
	 if(document.readyState=="complete"){
		var objdiv = document.createElement("DIV");
		objdiv.innerHTML=TB_window.innerHTML;
		TB_window.innerHTML="";
		document.body.appendChild(objdiv);
		MoveStart('exceptionDialog','dialogTitle');
		//===如果窗口宽度小于20就alert出异常
		try {
			var bodyWidth=document.body.clientWidth;
			var bodyHeigth=document.body.clientHeight;
			<% 
			String scriptStr=expMsg;
			scriptStr=scriptStr.replaceAll("\"","'");
			scriptStr=scriptStr.replaceAll("\n","");
			%>
			if(bodyWidth<100||bodyHeigth<100){
				alert("<%=scriptStr%>");
			}
		} catch (e) {
		}
		//===========
	 }else if(wartTime<20){
		setTimeout("showErrorWindow()", 1000);
		wartTime++;
	 }
	}
showErrorWindow();
</script>
</html>