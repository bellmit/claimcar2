<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>


<c:forEach var="holidayRiskVo" items="${prpLUserHolidayGrades}" varStatus="status">
	<tr class="text-c">
	<td>
		    ${status.index + size + 1}
		</td>
		<td>
			<input type="text" class="input-text" name="prpLUserHolidayVo.prpLUserHolidayGrades[${status.index + size  }].gradeName" 
			    value="${holidayRiskVo.gradeName }" readonly="readonly"/></td>
	    <td>
			<input type="text" class="input-text" name="prpLUserHolidayVo.prpLUserHolidayGrades[${status.index + size  }].transferCode" 
			    value="${holidayRiskVo.transferCode }" readonly="readonly"/></td>
			
	</tr>
</c:forEach>