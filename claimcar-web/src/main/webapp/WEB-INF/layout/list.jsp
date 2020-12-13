<%@page import="ins.framework.web.filter.WebAppFilter"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	long pageStartTime = System.currentTimeMillis();
%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%-- 被装饰页面的Title --%>
<title><decorator:title default="鼎和新理赔" /></title>

	<!--[if lt IE 9]>
		<script type="text/javascript" src="lib/html5.js"></script>
		<script type="text/javascript" src="lib/respond.min.js"></script>
		<script type="text/javascript" src="lib/PIE-2.0beta1/PIE_IE678.js"></script>
		<![endif]-->
		<link href="/claimcar/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/h-ui/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
		<link href="/claimcar/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
		
		<link  href="/claimcar/css/font.css" rel="stylesheet" />
		<link href="/claimcar/css/style.css" rel="stylesheet"  />
		<link  href="/claimcar/css/fee.css"  rel="stylesheet" type="text/css" />
		<link href="/claimcar/plugins/qtip/jquery.qtip.min.css" rel="stylesheet" type="text/css" />
		<link href="/claimcar/plugins/select2-3.4.4/select2.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/claimcar/lib/jquery/1.9.1/jquery.min.js"></script>
	
		<!--[if IE 6]>
		<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
		<script>DD_belatedPNG.fix('*');</script>
		<![endif]-->
		
<%-- 被装饰页面的head --%>
<decorator:head />
<style type="text/css">
</style>
</head>
<body>

			<!-- Main content -->
			<section class="content">
				<%-- 被装饰页面的body --%>
				<decorator:body />
			</section>
			<!-- /.content -->

		<footer class="main-footer">
			<div class="f-r hidden-xs">
			当前用户${user.userCode}
				<%
					Date requestStartTime = (Date) request.getAttribute(WebAppFilter.REQUEST_START_TIME);
					if (requestStartTime != null) {
						Date requestEndTime = new Date();
						out.println("整个请求耗时 " + (requestEndTime.getTime() - requestStartTime.getTime()) + " 毫秒");
						out.println("，其中业务逻辑耗时 " + (pageStartTime - requestStartTime.getTime()) + "毫秒");
						out.println("，页面渲染耗时 " + (requestEndTime.getTime() - pageStartTime) + "毫秒");
					}
				%>
				<c:if test="${!fn:startsWith(pageContext.request.serverName,'10')}">
					<a class="Hui-logo" title="中国南方电网标识" href="https://biaozhi.csg.cn/verify?sn=CA202001078093186168" target="_BLANK">
						<img src="/claimcar/images/csgbiaoshi.png">
					</a>
				</c:if>
			</div>
		</footer>
		
		<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
		<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>
		<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
		<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
		<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
		<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/claimcar/js/common/common.js"></script> 
		<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
		<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
		<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
		
		<script type="text/javascript">
		
 		</script>
</body>
</html>
