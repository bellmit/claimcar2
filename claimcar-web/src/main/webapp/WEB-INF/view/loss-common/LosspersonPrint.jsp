<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人伤单证打印</title>
</head>
<body>
<center>
<form name="myform">
<c:forEach var="vo" items="${PrpLDlossPersTraceList}">
<input type="radio" name="prpLDlossPersTraceVo.PersonName" value="${vo.id}"/> <span class="STYLE4">${vo.personName}</span> 
 </c:forEach>
 <div class="btn-footer clearfix text-c">
<input class="btn btn-primary" type="submit" name="submit" value="打印" />
</div>
</form>
</center>
</body>
</html>