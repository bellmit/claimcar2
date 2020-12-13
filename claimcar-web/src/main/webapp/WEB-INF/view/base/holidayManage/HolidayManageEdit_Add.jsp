<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<c:forEach var="map" items="${userMap}" varStatus="status">
	<tr class="text-c">
	    <td>
		    ${status.index + size + 1}
		</td>
		<td>
			<input  type="text" class="input-text" name="prpLUserHolidayVo.prpLUserHolidayGrades[${status.index + size  }].gradeName" 
			    value="${map.key }" readonly="readonly"/></td>
	    <td>
			<app:codeSelect   name="prpLUserHolidayVo.prpLUserHolidayGrades[${status.index + size  }].transferCode" 
			clazz="must"  type="select" codeType="UserCode"  dataSource="${map.value }"/>
			</td>
			
	</tr>
</c:forEach>