<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<script language="javascript">
//关闭子页面
//alert(window.opener.location);
//window.opener.location.reload();
window.opener.refreshFee();
window.close();                                             

</script>

