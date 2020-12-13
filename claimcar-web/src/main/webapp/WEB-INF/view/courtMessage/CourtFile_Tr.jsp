<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
	<td>${prpLCourtFile.caseno}</td>
	<td>${prpLCourtFile.wjmc}</td>
	<td><fmt:formatDate  value="${prpLCourtFile.scsj}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${prpLCourtFile.wjurl}</td>
	<td>${prpLCourtFile.wjtype}</td>
	<td>${prpLCourtFile.dsrno}</td>
</tr>