<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/layout/common/taglib.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>自助理赔短信发送人维护</title>
</head>
<body>
<div class="fixedmargin page_wrap">
    <form id="form" role="form" method="post" name="fm">
        <div class="table_cont">
            <div class="formtable">
                <table class="table table-bordered table-bg">
                    <thead class="text-c">
                    <tr>
                        <th>姓名</th>
                        <th>手机号</th>
                        <th>归属机构</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody class="text-c" id="infoTbody">
                    <input type="hidden" id="TBodySize" value="${fn:length(prpdAddresseeVoList)}">
                    <c:forEach var="prpdAddresseeVo" items="${prpdAddresseeVoList}" varStatus="status">
                        <tr class="text-c">
                            <td>
                                <input type="hidden" name="prpdAddresseeVo.id" value="${prpdAddresseeVo.id }"/>
                                <input type="hidden" name="prpdAddresseeVo.dbname" value="${prpdAddresseeVo.name }"/>
                                <input type="text" class="input-text" name="prpdAddresseeVo.name" value="${prpdAddresseeVo.name }" disabled/>
                            </td>
                            <td>
                                <input type="text" class="input-text" name="prpdAddresseeVo.mobileNo" value="${prpdAddresseeVo.mobileNo }" disabled/>
                                <input type="hidden" name="prpdAddresseeVo.dbmobileNo" value="${prpdAddresseeVo.mobileNo }"/>
                            </td>
                            <td>
                                <span class="select-box">
                                    <app:codeSelect codeType="ComCodeLv2" name="prpdAddresseeVo.comCode"
                                                    value="${prpdAddresseeVo.comCode }" clazz="must" id="comCode" type="select" disabled="true"/>
                                </span>
                            </td>
                            <td>
                                <button type="button" name="modifyBtn" class="btn  btn-primary mt-5" onclick="modifyInfo(this)">修改</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
    /**
     * 点击修改按钮，1. 隐藏修改按钮（避免可以多次点击）
     * 2. 允许当前行名字和电话可编辑
     * 3. 添加完成和取消按钮
     * @param element
     */
    function modifyInfo(element) {
        allowCurlineEdit(element);
        addButtons(element);
    }

    function allowCurlineEdit(element) {
        $(element).hide();
        var name = $(element).parent().parent().find("input[name='prpdAddresseeVo.name']");
        var mobile = $(element).parent().parent().find("input[name='prpdAddresseeVo.mobileNo']");
        name.attr("disabled", false);
        mobile.attr("disabled", false);
    }

    function addButtons(element) {
        addFinishBtn(element);
        addCancelBtn(element);
    }

    /** 添加完成按钮 */
    function addFinishBtn(element) {
        $(element).parent().append("<button type='button' name='finishBtn' class='btn  btn-primary mt-5' onclick='finishConfirm(this);'>完成</button>");
    }

    /** 添加取消按钮 */
    function addCancelBtn(element) {
        $(element).parent().append(" <button type='button' name='cancelBtn' class='btn btn-primary mt-5'  onclick='giveup(this);'>取消</button>");
    }

    /**
     * 点击完成按钮
     * 1. 检查数据是否有变化
     * （1）无变化时，显示修改按钮，删除完成与取消按钮
     * （2）有变化时，使用ajax请求，后台更新数据
     * （3）后台数据更新成功之后，需要更新页面隐藏域中的名字和电话
     * 2. 禁用当前行的可编辑状态
     * 3. 显示修改按钮，移除完成与取消按钮
     */
    function finishConfirm(element) {
        if(isChanged(element)) {
            var curtr = $(element).parent().parent();
            var name = curtr.find("input[name='prpdAddresseeVo.name']").val();
            if (name == undefined || name == "") {
                layer.alert("姓名不能为空！");
                return ;
            }
            var addressid = curtr.find("input[name='prpdAddresseeVo.id']").val();
            var mobile = curtr.find("input[name='prpdAddresseeVo.mobileNo']").val();
            var params = {
                "addressid": addressid,
                "name": name,
                "mobile": mobile
            };
            $.ajax({
                url: "/claimcar/msgModel/updateMsgSenderInfo.do?",
                type: "post",
                data: params,
                dataType: "json",
                success: function(result) {
                    if (result.status == "200") {
                        updatePageHiddenData(element);
                        disabledCurline(element);
                        removeBtns(element);
                    }
                    layer.alert(result.data);
                }
            });
        } else {
            disabledCurline(element);
            removeBtns(element);
        }
    }

    /**
     * 名字或电话是否有变动
     */
    function isChanged(element) {
        var curtr = $(element).parent().parent();
        // 获取页面初始值
        var dbname = curtr.find("input[name='prpdAddresseeVo.dbname']").val();
        var dbmobile = curtr.find("input[name='prpdAddresseeVo.dbmobileNo']").val();
        // 获取当前行名字与电话对象
        var curname = curtr.find("input[name='prpdAddresseeVo.name']").val();
        var curmobile = curtr.find("input[name='prpdAddresseeVo.mobileNo']").val();

        if (dbname != curname || dbmobile != curmobile) {
            return true;
        } else {
            return false
        }
    }

    /**
     * 更新页面隐藏域的值
     */
    function updatePageHiddenData(element) {
        var curtr = $(element).parent().parent();
        // 获取页面初始对象
        var nameObj = curtr.find("input[name='prpdAddresseeVo.dbname']");
        var mobileObj = curtr.find("input[name='prpdAddresseeVo.dbmobileNo']");
        // 获取当前行名字与电话
        var curname = curtr.find("input[name='prpdAddresseeVo.name']").val();
        var curmobile = curtr.find("input[name='prpdAddresseeVo.mobileNo']").val();

        nameObj.val(curname);
        mobileObj.val(curmobile);

    }

    /**
     *  点击取消按钮
     * 1. 恢复修改之前的值
     * 2. 禁用当前行的可编辑状态
     * 3. 显示修改按钮，移除完成和取消按钮
     */
    function giveup(element) {
        recoverData(element);
        disabledCurline(element);
        removeBtns(element);
    }

    /**
     * 恢复修改过的数据
     * 1. 获取修改前的数据
     * 2. 获取当前行的数据
     * 3. 判断数据是否一致
     * 4. 不一致则恢复，一致则无动作
     *
     */
    function recoverData(element) {
        var curtr = $(element).parent().parent();
        // 获取页面初始值
        var name = curtr.find("input[name='prpdAddresseeVo.dbname']").val();
        var mobile = curtr.find("input[name='prpdAddresseeVo.dbmobileNo']").val();
        // 获取当前行名字与电话对象
        var curnameObj = curtr.find("input[name='prpdAddresseeVo.name']");
        var curmobilObj = curtr.find("input[name='prpdAddresseeVo.mobileNo']");

        if (name != curnameObj.val()) {
            curnameObj.val(name);
        }
        if (mobile != curmobilObj.val()) {
            curmobilObj.val(mobile);
        }
    }

    /**
     * 禁用当前行的可编辑状态
     * 1. 显示修改按钮
     * 2. 禁用名字和电话的修改状态
     */
    function disabledCurline(element) {
        var name = $(element).parent().parent().find("input[name='prpdAddresseeVo.name']");
        var mobile = $(element).parent().parent().find("input[name='prpdAddresseeVo.mobileNo']");
        name.attr("disabled", true);
        mobile.attr("disabled", true);
    }

    /**
     * 1. 显示修改按钮
     * 2. 移除当前行的完成与取消按钮
     * @param element
     */
    function removeBtns(element) {
        var curtd = $(element).parent();
        curtd.find("button[name='modifyBtn']").show();
        curtd.find("button[name='finishBtn']").remove();
        curtd.find("button[name='cancelBtn']").remove();
    }
</script>
</body>
</html>