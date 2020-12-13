<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri ="http://shiro.apache.org/tags" prefix="shiro"%>
<%@page import="ins.sino.claimcar.flow.vo.WfFlowShowVo"%>
<%@page import="ins.sino.claimcar.flow.vo.WfFlowNodeVo"%>
<%@page import="java.util.List"%>
<%@page import="ins.platform.utils.DateUtils"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>工作流流程图</title>
<style>
body {
     overflow-x : auto;
     overflow-y : auto;
     background-color:white;
}
ul{
list-style-type:circle;
}
li{
font-size:14px;
list-style-type:disc;
list-style-position:inside
}
.content{overflow:auto;}
p.nodeHead{text-align:center;width:110px;height:25px;height:21px\9;margin-top:0px;padding-top:5px\9;font-weight:bold;}
p.nodeContent{text-align:center;width:106px;width:105px\9;height:57px;height:50px\9;margin-top:0px;padding-top:5px\9;background-color:white;margin-left:2px;}
</style>
<script type="text/javascript">
    var timer = null;
	function intoNode(e) {
		var url = $(e).attr("url");
		if(url != "cancel"){
			if(url == "noPermission"){
				layer.alert("您没有处理该任务的权限！");
			}else{
				openTaskEditWin($(e).attr("name")+"处理",$(e).attr("url"));
			}
		}
	}
	function showTips(e) {
		clearTimeout(timer);
		timer = setTimeout(function(){
			layer.tips($(e).attr("tips"), e);
		},600);
		
	}
	$(function() {
		var chart = new Highcharts.Chart(
				{
					chart : {
						renderTo : 'container',
						events : {

							load : function() {
								// Draw the flow chart
								var ren = this.renderer;
								<% 
								WfFlowShowVo wfFlowShowVo = (WfFlowShowVo)request.getAttribute("wfFlowShowVo"); 
								List<WfFlowNodeVo>  wfFlowNodeList =  wfFlowShowVo.getWfFlowNodeList();
							    String itemNameCode = "DLoss,VLoss,VPrice";
								int maxRow =  wfFlowShowVo.getMaxRow();
						        int maxColum = wfFlowShowVo.getMaxColum();
						        int interspaceX = 24;//x轴间隙
						        int interspaceY = 24;//y轴间隙
						        int nodeWidth = 110;//节点宽度
						        int nodeHeight = 85;//节点高度
						        int maxX = (interspaceX+nodeWidth)*maxColum;
						        int maxY = (interspaceX+nodeHeight)*maxRow;
						        int width = 975;
						        int height = 500;
						        if(maxX > width){
						        	width = maxX + 100;
						        }
						        if(maxY > height){
						        	height = maxY;
						        }
						        %>
						        $("#mainContainer").attr("style","height:<%=height+100%>px;width:<%=width+200%>px;");
						        <%
						       for(WfFlowNodeVo wfFlowNodeVo:wfFlowNodeList){
						    	   String nodeName =  wfFlowNodeVo.getTaskName();
						    	   String title = "流入时间:"+DateUtils.dateToStr(wfFlowNodeVo.getTaskInTime(), DateUtils.YToSec)+"<br>"+"处理时间:"+DateUtils.dateToStr(wfFlowNodeVo.getHandlerTime(), DateUtils.YToSec)+"<br>"+"流出时间:"+DateUtils.dateToStr(wfFlowNodeVo.getTaskOutTime(), DateUtils.YToSec);
						    	   String url = wfFlowNodeVo.getUrl();
						    	   String nodeStatus = wfFlowNodeVo.getNodeStatus();//节点状态翻译
						   	       String nodeColor = wfFlowNodeVo.getNodeColor();//节点颜色 不同状态颜色不同
						   	       String userName = wfFlowNodeVo.getUserName();
						   	       String taskName = wfFlowNodeVo.getTaskName();
						   	       if("RecLoss".equals(wfFlowNodeVo.getNodeCode())){
						   	    	  taskName = "损余回收";
						   	       }
						   	       String itemName = wfFlowNodeVo.getItemName();
						    	   int row = wfFlowNodeVo.getRow();//节点行数
						    	   int colum = wfFlowNodeVo.getColum();//节点列数
						    	   WfFlowNodeVo parentNode = wfFlowNodeVo.getParentNode();
						    	   String div = "<div style='cursor:pointer;' url="+url+" name="+taskName+" tips='"+title.toString()+"' onmouseover='showTips(this)' onmouseout ='clearTimeout(timer);' onclick='intoNode(this);'><p class='nodeHead'>"+nodeName+"</p><p class='nodeContent'>"+userName+"<br/>"+nodeStatus+"</p></div>";
						    	   if(itemNameCode.indexOf(wfFlowNodeVo.getNodeCode()) > -1){
						    		   div = "<div style='cursor:pointer;' url="+url+" name="+taskName+" tips='"+title.toString()+"' onmouseover='showTips(this)' onmouseout ='clearTimeout(timer);' onclick='intoNode(this);'><p class='nodeHead'>"+nodeName+"</p><p class='nodeContent'>"+itemName+"<br/>"+userName+"<br/>"+nodeStatus+"</p></div>";
						    	   }
						    	   int x = nodeWidth*(colum - 1) + interspaceX * colum;//节点计算x像素
						    	   int y = nodeHeight*(row - 1)+ interspaceY * row;//节点计算y像素
								%>
								ren.label("<%=div%>",<%=x%>, <%=y%>, null, null, null,true, true)
							    .attr({
										fill : '<%=nodeColor%>',
										'stroke-width' : 0,
										padding : 0,
										r : 5,
										width : <%=nodeWidth%>,
										height : <%=nodeHeight%>
									})
								.css({
										color : 'black'
									}).add();
                                <% 
                                if(parentNode != null){
                                   int parentRow = parentNode.getRow();//父节点行数
  						    	   int parentColum = parentNode.getColum();//父节点列数
                                   int pathY = 15;//画线时候的高度差
                                   int parentX = nodeWidth*(parentColum - 1) + interspaceX * parentColum;//计算父节点x像素
 						    	   int parentY = nodeHeight*(parentRow - 1)+ interspaceY * parentRow;//计算父节点y像素
 						    	   int lineX = x + nodeWidth/2;//线的终点x坐标
 						    	   int lineY = y - pathY;//先的终点Y坐标
 						    	   int parentLineX = parentX + nodeWidth/2;//线的起始x坐标
 						    	   int parentLineY = parentY + nodeHeight - pathY;//线的起始y坐标
 						    	   if(lineX == parentLineX){
 						    	   %>
 						    		  ren.path([ 'M', <%=parentLineX%>, <%=parentLineY%>, 'L', <%=lineX%>, <%=lineY%>,'L', <%=lineX-5%>, <%=lineY-5%>, 'M', <%=lineX%>,<%=lineY%>, 'L', <%=lineX+5%>, <%=lineY-5%>])
 									   .attr({
 												'stroke-width' : 2,
 												stroke : '<%=nodeColor%>'
 											}).add();
 						           <%
 						    	   }else{
                                      %>
                                      ren.path([ 'M', <%=parentLineX%>, <%=parentLineY%>, 'L', <%=parentLineX%>, <%=parentLineY+12%>,'L', <%=parentLineX%>, <%=parentLineY+12%>, 'M', <%=parentLineX%>,<%=parentLineY+12%>, 'L', <%=parentLineX%>, <%=parentLineY+12%>])
									   .attr({
												'stroke-width' : 2,
												stroke : '<%=nodeColor%>'
											}).add();
						    		ren.path([ 'M', <%=parentLineX%>, <%=parentLineY+12%>, 'L', <%=lineX%>, <%=parentLineY+12%>, 'C', <%=lineX%>, <%=parentLineY+12%>, <%=lineX%>, <%=parentLineY+12%>, <%=lineX%>, <%=parentLineY+12%>, 'L', <%=lineX%>,<%=lineY%>, 'L', <%=lineX-5%>, <%=lineY-5%>, 'M', <%=lineX%>,<%=lineY%>, 'L', <%=lineX+5%>, <%=lineY-5%> ])
									.attr({
										'stroke-width' : 2,
										stroke : '<%=nodeColor%>'
									}).add();
 						    		  <%
 						    	   }
                                }
						       }%>
							}
						},
				    width:<%=width%>,
				    height:<%=height%>
					},
					title : {
						text : ''
					},
					credits: {
				          enabled:false
				}

				});
	});
</script>
<script src="${ctx }/js/flow/highcharts.js"></script>
</head>
<body>
<input type="hidden" id="registNo" value="<%=wfFlowNodeList.get(0).getRegistNo()%>"/>
<input type="hidden" id="queryCode_URLS" value="${queryCode_URLS}">
<input type="hidden" id="addCode_URLS" value="${addCode_URLS}">
    <div style="margin-left:10px;padding-top:10px;float:top;width:100%;display:block;height:50px;">
	    <label class="col-1 text-c" style="font-size:14px">报案号:</label>
		<div class="col-3">
		<%=wfFlowNodeList.get(0).getRegistNo() %>
		</div>
		<div class="col-8">
		<%=wfFlowShowVo.getBussTag() %>
		</div>
	</div>
	<div id="mainContainer">
    <div style="margin-left:10px;float:left;width:120px;height:300px;display:inline;position:relative;">
    <ul>
    <li><a>流程图</a></li>
    <li><a  onClick="openTaskEditWin('节点图','/claimcar/flowQuery/showFlow.do?flowId=<%=wfFlowNodeList.get(0).getFlowId()%>&showType=node')" target="_blank">节点图</a></li>
    <li><a onClick="openTaskEditWin('案件列表','/claimcar/flowQuery/showCaseList.do?flowId=<%=wfFlowNodeList.get(0).getFlowId()%>')" target="_blank">案件列表</a></li>
    <li><a onclick="createRegistMessage('<%=wfFlowNodeList.get(0).getRegistNo() %>','');">案件备注</a></li>
    <c:if test="${hasRegistTask  && !empty registNo}">
		<%-- <li><a onclick="window.open('${queryCode_URL}${registNo}')">关联工单</a></li> --%>
		<li><a onclick="send('1')">关联工单</a></li>
		<%-- <li><a onclick="window.open('${addCode_URL}${registNo}')">新建工单</a></li> --%>
		<li><a onclick="send('2')">新建工单</a></li>
	</c:if>
		<shiro:hasRole name="claim.recording">
			<%--<li><a onclick="window.open('${recording_URL}${registNo}')">报案录音</a></li>--%>
			<li><a onclick="layer.open({
        		type : 2,
        		area : [ '900px', '345px' ],
        		fix : false, // 不固定
        		maxmin : false,
        		shade : [0.3],
        		scrollbar : true,
        		title : '报案录音',
				content : '${recording_URL}${registNo}'
				});">报案录音</a></li>
		</shiro:hasRole>
    </ul>
    </div>
	<div id="container" style="display:inline;float:left;position:relative;"></div>
	</div>
	<script type="text/javascript">
	
	function messageSearch(registNo) {
		var registNo=$("#registNo").val();
		if (isBlank(registNo)) {
			layer.alert("报案号不能为空");
			return;
		}
		var isRepair="1";
		var goUrl = "/claimcar/msgModel/msgModelQueryList.do?registNo=" + registNo + "&isRepair="+isRepair;
		openTaskEditWin('短信查询', goUrl);
	}
	
	function send(flag){
		var registNo = $("#registNo").val();
		var addCode_URL = $("#addCode_URLS").val();
		var queryCode_URL = $("#queryCode_URLS").val();
		
		if(flag == 1){
			//关联工单
			window.parent.postMessage({"reportno":registNo,"active":"queryorder"},queryCode_URL);
		}else{
			//新建工单
			window.parent.postMessage({"reportno":registNo,"active":"addorder"},addCode_URL);
		}
	}
	</script>
</body>

</html>
