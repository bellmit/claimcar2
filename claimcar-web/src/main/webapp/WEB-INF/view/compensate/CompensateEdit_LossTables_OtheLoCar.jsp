<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

			<select class="select2" name="otherLoss[${size }].itemName">
				<c:forEach var="prpLcCarDeviceVo" items="${prpLcCarDeviceVoList }"  varStatus="sta">
					<option value="${prpLcCarDeviceVo.deviceName}">${prpLcCarDeviceVo.deviceName }</option>
				</c:forEach>
			</select>

		