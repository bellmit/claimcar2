<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/layout/common/taglib.jspf" %>
<div class="table_cont pd-10">
<div class="formtable f_gray4">
    <form id="form" name="form" class="form-horizontal size-M" role="form">
        <div class="row  cl">
            <label class="form_label col-2">姓名</label>
            <div class="formControls col-2">
                <label>${indexInfo.userName }</label>
            </div>
            <label class="form_label col-2">所属机构</label>
            <div class="formControls col-2">
                <label><app:codetrans codeType="FlowNode" codeCode="${indexInfo.comCode }"/></label>
            </div>
            <label class="form_label col-2">职级</label>
            <div class="formControls col-2">
                <label><app:codetrans codeType="UserCode" codeCode="${indexInfo.rankLevel }"/></label>
            </div>
        </div>
        <div class="row cl">
            <label class="form_label col-2">年度工作量</label>
            <div class="formControls col-2">
                <label>${indexInfo.monthAverageWorkLoad }件</label>
            </div>
            <label class="form_label col-2">及时查勘率</label>
            <div class="formControls col-2">
                    <label>${indexInfo.checkRateInTime }%</label>
            </div>
            <label class="form_label col-2">及时定损率</label>
            <div class="formControls col-2">
                    <label>${indexInfo.dLossRateInTime }%</label>
            </div>
        </div>
        <div class="row cl">
            <label class="form_label col-2">万元以下案均报案周期</label>
            <div class="formControls col-2">
                <label>${indexInfo.caseAveragePaymentCycle }天</label>
            </div>
            <label class="form_label col-2">一次性通过率</label>
            <div class="formControls col-2">
                    <label>${indexInfo.oneTimePassRate }%</label>
            </div>
            <label class="form_label col-2">定损偏差率</label>
            <div class="formControls col-2">
                    <label>${indexInfo.dLossDeviationRate }%</label>
            </div>
        </div>

        <div class="row cl">
            <label class="form_label col-2">配件系统点选率</label>
            <div class="formControls col-2">
                <label>${indexInfo.fittingSelectRate }%</label>
            </div>
            <label class="form_label col-2">万元以下车均换件数量</label>
            <div class="formControls col-2">
                    <label>${indexInfo.componentNumAverage }件</label>
            </div>
            <label class="form_label col-2">备选指标</label>
            <div class="formControls col-2">
                    <label>${indexInfo.optionalQuota }</label>
            </div>
        </div>
    </form>
</div>
</div>