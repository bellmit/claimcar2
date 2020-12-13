<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
	<head>
		<title></title>
		<base target="_self">
		</head>
<script language="javascript">
window.opener.refreshLossInfo();
window.opener.isWaterFlooded();
window.opener.isMajorCase();
window.opener.refreshFee();
//关闭子页面
//window.opener.location.reload();
window.close();
//window.parent.close();

</script>
</html>
