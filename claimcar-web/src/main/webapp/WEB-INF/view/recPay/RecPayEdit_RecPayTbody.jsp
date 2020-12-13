<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:if test="${riskCode =='1101' }">
<c:forEach var="replevyRiskVo" items="${prplReplevyDetailVos }" varStatus="status">
	<tr class="text-c">
	    <input type="hidden" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].replevyId" 
	        value="${prplReplevyMainVo.replevyId }"/>
		<td>
			<%-- <input type="button" title="删除" onclick="delSubRisk(this)" name="subRiskVo_${status.index + size }" class="btn btn-zd fl" value="-" /> --%>
		<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn" onclick="delSubRisk(this)" name="prplReplevyDetailVos_${status.index + size }"></button>
		<input type="hidden" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].id" 
	        value="${replevyRiskVo.id }"/>
		</td>
		<td>机动车交通事故责任强制险
		    <input type="hidden" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].kindCode"
		        value="BZ" />
		</td>
		<td>
		    <app:codeSelect type="select" codeType="LossCategory" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].lossCategory" 
		         value="${replevyRiskVo.lossCategory }"  clazz="must" />
		</td>
	    <td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].recoveryRes" 
			    value="${replevyRiskVo.recoveryRes }" datatype="*1-20" errormsg="请勿输入超过20个字！"/></td>
		<td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].planReplevy" 
			    value="${replevyRiskVo.planReplevy }" datatype="amount"  value nullmsg="请输入金额！" title="金额格式不正确！"
			     onchange="calSumPlanReplevy(this,true)" /></td>
		<%-- <td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].replevyFee" 
			    value="${replevyRiskVo.replevyFee }" datatype="amount" value nullmsg="请输入金额！" title="金额格式不正确！"
			     onchange="calSumReplevyFee(this,true)" /></td> --%>
	    <td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].realReplevy" 
			    value="${replevyRiskVo.realReplevy }" datatype="amount" value nullmsg="请输入金额！" title="金额格式不正确！"
			     onchange="calSumRealReplevy(this,true)" /></td>
			
	</tr>
</c:forEach>
</c:if>
<c:if test="${riskCode != '1101' }">
<c:forEach var="replevyRiskVo" items="${prplReplevyDetailVos }" varStatus="status">
	<tr class="text-c">
	    <input type="hidden" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].replevyId" 
	        value="${prplReplevyMainVo.replevyId }"/>
		<td>
			<%-- <input type="button" title="删除" onclick="delSubRisk(this)" name="subRiskVo_${status.index + size }" class="btn btn-zd fl" value="-" /> --%>
		<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delwusunrowBtn" onclick="delSubRisk(this)" name="prplReplevyDetailVos_${status.index + size }"></button>
		<input type="hidden" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].id" 
	        value="${replevyRiskVo.id }"/>
		</td>
		<td>
		    <app:codeSelect type="select" codeType="BiCode" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].kindCode" 
		         value="${replevyRiskVo.kindCode }" clazz="must" />
		    </td>
		<td>
		    <app:codeSelect type="select" codeType="LossCategory" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].lossCategory" 
		         value="${replevyRiskVo.lossCategory }" clazz="must" />
		</td>
	    <td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].recoveryRes" 
			    value="${replevyRiskVo.recoveryRes }" datatype="*1-20" errormsg="请勿输入超过20个字！"/></td>
		<td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].planReplevy" 
			    value="${replevyRiskVo.planReplevy }" datatype="amount" value nullmsg="请输入金额！" title="金额格式不正确！"
			     onchange="calSumPlanReplevy(this,true)" /></td>
	    <td>
			<input type="text" class="input-text" name="prplReplevyMainVo.prplReplevyDetails[${status.index + size  }].realReplevy" 
			    value="${replevyRiskVo.realReplevy }" datatype="amount" value nullmsg="请输入金额！" title="金额格式不正确！"
			     onchange="calSumRealReplevy(this,true)" /></td>
			
	</tr>
</c:forEach>
</c:if>
