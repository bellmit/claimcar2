<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
	<!-- 理算提示信息 -->
		<div class="f-12 table_cont" name="CompensateTipsInfo">
			<div class="CiIndemInfo  mb-5">
				<input type="hidden" name="TipsInfoMap" value="" />
					<span>三者车</span> <span>粤A84759</span> <span>交强责任类型为：</span> <span class="c-red">有责</span><br>
					<span>标的车</span> <span>粤A84750</span> <span>交强责任类型为：</span> <span class="c-red">有责</span><br>
					<c:if test="">
					 	<span>立案环节没有发起追偿，理算可以选择发起追偿任务</span>
					</c:if>
								
			</div>
			<hr style="color:#333">
	
			<div class="IndemnityInfo mb-5 ">
				<span>保单3107297475859573632572出险次数</span> <span>91</span><br>
				<span>承保时间与出险时间相差：</span> <span>5天</span> 
				
				<span>产品标识:</span>
				<c:choose>
					<c:when test="">
					 	<span>有</span><br>
					</c:when>
					<c:otherwise>
						<span>无</span><br>
					</c:otherwise>
				</c:choose>	
				<c:if test="">
					<span>标的车作为三者车有索赔记录</span>
				</c:if>
				<c:if test="">
					 <span>该保单48小时内已报过案</span> <span class="c-red">疑似重复报案</span><br>
				</c:if>			
			</div>
		</div>


	<!-- 弹出出险信息结束 -->


