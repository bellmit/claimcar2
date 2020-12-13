<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="ins.sino.claimcar.flow.vo.WfFlowNodeShowVo"%>
<%@page import="ins.sino.claimcar.flow.vo.WfFlowNodeVo"%>
<%@page import="ins.sino.claimcar.flow.vo.WfFlowShowVo"%>
<%@page import="ins.platform.utils.DateUtils"%>
<%@page import="java.util.List"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>工作流节点图</title>
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
								List<WfFlowNodeShowVo> wfFlowNodeShowList = wfFlowShowVo.getWfFlowNodeShowList(); 
								String itemNameCode = "DLoss,VLoss,VPrice";
								int interspaceX = 24;//x轴间隙
						        int interspaceY = 24;//y轴间隙
						        int nodeWidth = 110;//节点宽度
						        int nodeHeight = 85;//节点高度
						        int widthPre = 150;//预留宽度
						        
						        String registNo = "";
						        String flowId = "";
								int maxX = widthPre + (interspaceX+nodeWidth)*wfFlowShowVo.getMaxColum();
						        int maxY = (interspaceX+nodeHeight)*wfFlowShowVo.getMaxRow();
						        int width = 975;
						        int height = 500;
						        if(maxX > width){
						        	width = maxX + 200;
						        }
						        if(maxY > height){
						        	height = maxY;
						        }
						        %>
						        $("#mainContainer").attr("style","height:<%=height+100%>px;width:<%=width+200%>px;");
						        <%
								int preCenterX = 0;
								int preCenterY = 0;
								for(int i = 0;i < wfFlowNodeShowList.size();i ++){
									WfFlowNodeShowVo wfFlowNodeShowVo = wfFlowNodeShowList.get(i);
									for(WfFlowNodeVo wfFlowNodeVo:wfFlowNodeShowVo.getChildList()){
										   if("".equals(registNo)){
											   registNo = wfFlowNodeVo.getRegistNo();
										   }
										   if("".equals(flowId)){
											   flowId = wfFlowNodeVo.getFlowId();
										   }
										   String nodeName =  wfFlowNodeVo.getTaskName();
								    	   String title = "流入时间:" + DateUtils.dateToStr(wfFlowNodeVo.getTaskInTime(), DateUtils.YToSec) + "<br>"
								    	   + "处理时间:" + DateUtils.dateToStr(wfFlowNodeVo.getHandlerTime(), DateUtils.YToSec) + "<br>"
								    	   + "流出时间:" + DateUtils.dateToStr(wfFlowNodeVo.getTaskOutTime(), DateUtils.YToSec);
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
								    	   int x = widthPre + nodeWidth*(colum - 1) + interspaceX * colum;//节点计算x像素
								    	   int y = nodeHeight*(row - 1)+ interspaceY * row;//节点计算y像素
								    	   String div = "<div style='cursor:pointer;' url="+url+" name="+taskName+" tips='"+title.toString()+"' onmouseover='showTips(this)' onmouseout ='clearTimeout(timer);' onclick='intoNode(this);'><p class='nodeHead'>"+nodeName+"</p><p class='nodeContent'>"+userName+"<br/>"+nodeStatus+"</p></div>";
								    	   if(wfFlowNodeVo.getNodeCode().indexOf(itemNameCode) > -1){
								    		   div = "<div style='cursor:pointer;' url="+url+" name="+taskName+" tips='"+title.toString()+"' onmouseover='showTips(this)' onmouseout ='clearTimeout(timer);' onclick='intoNode(this);'><p class='nodeHead'>"+nodeName+"</p><p class='nodeContent'>"+itemName+"<br/>"+userName+"<br/>"+nodeStatus+"</p></div>";
								    	   }
								    	   %>
											ren.label("<%=div%>",
										             <%=x%>, <%=y%>, null, null, null,true, true)
										    .attr({
													fill : '<%=nodeColor%>',
													'stroke-width' : 0,
													padding : 0,
													r : 5,
													width : <%=nodeWidth%>,
													height : <%=nodeHeight%>
												})
											.css({
													color : 'black',
												}).add();
			                                <% 
			                                if(colum > 1){//节点到节点的线
			                                	  int lineBeginX = x - 24;
			                                	  int lineBeginY = y + nodeWidth/2 -23;
			                                	  int lineEndX = x;
			                                	  int lineEndY = y + nodeWidth/2 - 23;
			                                      %>
			                                      ren.path([ 'M', <%=lineBeginX%>, <%=lineBeginY%>, 'L', <%=lineEndX%>, <%=lineEndY%>,'L', <%=lineEndX-5%>, <%=lineEndY-5%>, 'M', <%=lineEndX%>,<%=lineEndY%>, 'L', <%=lineEndX-5%>, <%=lineEndY+5%>])
												   .attr({
															'stroke-width' : 2,
															stroke : '<%=nodeColor%>'
														}).add();
			 						    		  <%
			                                }
								     }
									int rootNodeNum = wfFlowNodeShowVo.getRootNodeNum();
									int rowNum = wfFlowNodeShowVo.getRowNum();
									String nodeName = wfFlowNodeShowVo.getNodeName();
									int centerX = 80;//圆节点中心x
									int centerY = interspaceY*(rowNum) + nodeHeight*(rowNum-1) + nodeHeight/2 - 10;//圆节点中心y
									if(rootNodeNum > 1){
										centerY = centerY - (nodeHeight/2+interspaceY/2)*(rootNodeNum-1);
									}
								    int radius = 36;//半径
								    int rootLength = 55;//圆节点到第一个节点得距离
								    if(preCenterX != 0 && preCenterY != 0){//
								    	%>
								        //圆节点到圆节点的线
								    	ren.path([ 'M', <%=preCenterX%>, <%=preCenterY+radius%>, 'L', <%=centerX%>, <%=centerY - radius%>,'L', <%=centerX-5%>, <%=centerY - radius -5%>, 'M', <%=centerX%>,<%=centerY - radius%>, 'L', <%=centerX+5%>, <%=centerY - radius - 5%>])
										   .attr({
													'stroke-width' : 2,
													stroke : '#6093C2'
												}).add();
								    	<%
									 }
								    preCenterX = centerX;
								    preCenterY = centerY;
									%>
									//画圆节点
									ren.circle(<%=centerX%>,<%=centerY%>,<%=radius%>).attr({
			                            fill: 'white',
			                           'stroke-width': 2,
			                           stroke: '#6093C2'
			                        }).add();
									//圆节点名称
									 ren.label('<%=nodeName%>', <%=centerX-14%>, <%=centerY-16%>)
				                        .css({
				                        	color:'#6093C2',
				                            fontWeight: 'bold'
				                        })
				                        .add();
									 <%
									 //圆节点到节点得线
									 if(rootNodeNum == 1){
									 %>
									 ren.path([ 'M', <%=centerX+radius%>, <%=centerY%>, 'L', <%=centerX+radius+rootLength%>, <%=centerY%>,'L', <%=centerX+radius+rootLength-5%>, <%=centerY-5%>, 'M', <%=centerX+radius+rootLength%>,<%=centerY%>, 'L', <%=centerX+radius+rootLength-5%>, <%=centerY+5%>])
									   .attr({
												'stroke-width' : 2,
												stroke : '#6093C2'
											}).add();
									<%
									 }else if(rootNodeNum > 1){
										 %>
											ren.path([ 'M', <%=centerX+radius%>, <%=centerY%>, 'L', <%=centerX+radius+rootLength/2%>, <%=centerY%>,'L', <%=centerX+radius+rootLength/2%>, <%=centerY%>, 'M', <%=centerX+radius+rootLength/2%>,<%=centerY%>, 'L', <%=centerX+radius+rootLength/2%>, <%=centerY%>])
											   .attr({
														'stroke-width' : 2,
														stroke : '#6093C2'
													}).add();
											<%
										 int nodeX = widthPre + interspaceX;//节点计算x像素
										 for(int k = rootNodeNum-1;k >= 0;k --){
											 int nodeY = nodeHeight*(rowNum - k - 1)+ interspaceY * (rowNum - k) + nodeHeight/2 - 10;//节点计算y像素
											 %>
											 ren.path([ 'M', <%=centerX+radius+rootLength/2%>, <%=centerY%>, 'L', <%=centerX+radius+rootLength/2%>, <%=nodeY%>, 'C', <%=centerX+radius+rootLength/2%>, <%=nodeY%>, <%=centerX+radius+rootLength/2%>, <%=centerY%>, <%=centerX+radius+rootLength/2%>, <%=nodeY%>, 'L', <%=nodeX%>,<%=nodeY%>, 'L', <%=nodeX-5%>, <%=nodeY-5%>, 'M', <%=nodeX%>,<%=nodeY%>, 'L', <%=nodeX-5%>, <%=nodeY+5%> ])
												.attr({
													'stroke-width' : 2,
													stroke : '#6093C2'
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
</head>
<body>
    <script src="${ctx }/js/flow/highcharts.js"></script>
    <div style="margin-left:10px;padding-top:10px;float:top;width:100%;display:block;height:50px;">
	    <label class="col-1 text-c" style="font-size:14px">报案号:</label>
		<div class="col-3">
		<%=registNo %>
		</div>
		<div class="col-8">
		<%=wfFlowShowVo.getBussTag() %>
		</div>
	</div>
	<input type="hidden" id="queryCode_URLS" value="${queryCode_URLS}">
	<input type="hidden" id="addCode_URLS" value="${addCode_URLS}">
	<input type="hidden" id="registNo" value="${registNo}">
	<div id="mainContainer">
        <div style="margin-left:10px;float:left;width:120px;height:300px;display:inline;position:relative;">
			<ul>
				<li><a
					onClick="openTaskEditWin('流程图','/claimcar/flowQuery/showFlow.do?flowId=<%=flowId%>')"
					target="_blank">流程图</a></li>
				<li><a>节点图</a></li>
				<li><a
					onClick="openTaskEditWin('案件列表','/claimcar/flowQuery/showCaseList.do?flowId=<%=flowId%>')"
					target="_blank">案件列表</a></li>
				<li><a onclick="createRegistMessage('<%=registNo%>','');">案件备注</a></li>
				<c:if test="${hasRegistTask && !empty registNo }">
					<%-- <li><a onclick="window.open('${queryCode_URL}${registNo}')">关联工单</a></li>
					<li><a onclick="window.open('${addCode_URL}${registNo}')">新建工单</a></li> --%>
					<li><a onclick="send('1')">关联工单</a></li>
					<li><a onclick="send('2')">新建工单</a></li>
				</c:if>
			</ul>
		</div>
	    <div id="container" style="display:inline;float:left;position:relative;"></div>
	</div>
	
		
	<script type="text/javascript">
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
