<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%--tbody内容,人伤损失项 --%>


<c:forEach var="checkPersonVo" items="${checkPersonVos}" varStatus="status">
	<c:set var="inputIdx" value="${status.index + personSize}" />
	<tr class="text-c" id="personTr" name="personTr">
		<input type="hidden" id="personId${inputIdx}" name="checkPersonVos[${inputIdx}].id" value="${checkPersonVo.id}">
		<input type="hidden" id="personRegistNo" name="checkPersonVos[${inputIdx}].registNo" value="${checkPersonVo.registNo}">
		<td><app:codeSelect codeType="" id="personCarId" type="select"
				dataSource="${loss}" lableType="name" clazz="must"
				onchange="setSelectName(this,'lossPartyName_${inputIdx}')"
				name="checkPersonVos[${inputIdx}].lossPartyId"
				value="${checkPersonVo.lossPartyId}" nullToVal="0" upperCode="${inputIdx}"/>
			<input type="hidden" name="checkPersonVos[${inputIdx}].lossPartyName"
			id="lossPartyName_${inputIdx}" value="${checkPersonVo.lossPartyName}">
		</td>
		<td>
			<input type="text" class="input-text" name="checkPersonVos[${inputIdx}].personName"
				   value="${checkPersonVo.personName}" datatype="*">
		</td>
		<td><app:codeSelect codeType="LossItemType" type="select"
				name="checkPersonVos[${inputIdx}].personProp"
				value="${checkPersonVo.personProp}" /></td>
		<td>
			<app:codeSelect codeType="SexCode" type="select" id="personSex" 
				 			name="checkPersonVos[${inputIdx}].personSex" 
							value="${checkPersonVo.personSex}" />
		</td>
		<td><app:codeSelect codeType="IdentifyType" type="select"
				lableType="name" onchange="per_change(this,'${inputIdx}')"
				name="checkPersonVos[${inputIdx}].identifyType" style="width: 95%"
				value="${checkPersonVo.identifyType}" />
		</td>
		<td>
		 	<input type="text" class="input-text" datatype="*"
		 		name="checkPersonVos[${inputIdx}].idNo" maxlength="30" onchange="toUpperCase(this)"
				value="${checkPersonVo.idNo}" onblur="idNoOnblur(this,'${inputIdx}')">
		</td>
		<td>
		 	<input type="text" class="input-text" id="personAge"
		 		datatype="*0-0|age" errormsg="请输入1-120岁！"
		 		name="checkPersonVos[${inputIdx}].personAge"
				value="${checkPersonVo.personAge}">
		</td>
		<td>
			<app:codeSelect codeType="TicCode" type="select" lableType="name"
				id="ticCode" name="checkPersonVos[${inputIdx}].ticCode"
				value="${checkPersonVo.ticCode}" />
		</td>
		<td>
			<app:codeSelect codeType="ProcessMode" type="select"
					value="${checkPersonVo.checkDispose}" 
					name="checkPersonVos[${inputIdx}].checkDispose" />
		</td>
		<td>
			<app:codeSelect codeType="PersonPayType" type="select" 
					id="personPayType"
					name="checkPersonVos[${inputIdx}].personPayType"
					value="${checkPersonVo.personPayType}"/>
		</td>
		<%-- <td>
			<span class="select-box">
			<app:codeSelect codeType="IsValid" type="select" id="treatType"
							name="checkPersonVos[${inputIdx}].checkPersonVo"
							value="${checkPersonVo.treatType}" clazz="must"/></span>
		</td> --%>
		<td>
			<input type="text" class="input-text" datatype="*0-200"
					name="checkPersonVos[${inputIdx}].hospital"
					value="${checkPersonVo.hospital}">
		</td>
		<td>
			<input type="text" class="input-text" datatype="amount"
					name="checkPersonVos[${inputIdx}].lossFee" 
					value="${checkPersonVo.lossFee}" errormsg="请输入正确的金额，只能精确到两位小数！" >
		</td>
		<td><input type="text" class="input-text"
			name="checkPersonVos[${inputIdx}].woundDetail"
			value="${checkPersonVo.woundDetail}"></td>
		<td>
			<app:codeSelect codeType="InjuredPart" type="select"
				name="checkPersonVos[${inputIdx}].injuredPart"
				value="${checkPersonVo.injuredPart}"/>
		</td>
		<td><c:if test="${nodeCode eq 'Chk'}">
				<button type="button"
					class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
					onclick="delPersonItems(this)" name="delPrersonItem_${inputIdx}"></button>
			</c:if></td>
	</tr>
</c:forEach>

<script type="text/javascript">
	//人伤
	function idNoOnblur(obj, idx) {
		var value = $(obj).val();
		var idTypeName = "select[name='" + "checkPersonVos[" + idx + "].identifyType" + "']";
		var ageName = "input[name='" + "checkPersonVos[" + idx + "].personAge" + "']";
		var sexName = "select[name='" + "checkPersonVos[" + idx + "].personSex" + "']";
		var idType = $(idTypeName).val();
		//	layer.alert(idType);
		if (idType == 1 || idType == "1") {//省份证
			var strBirthday = getBirthdayByIdCard(value);
			$(ageName).val(jsGetAge(strBirthday));
			$(sexName).val(jsGetSex(value));
		}
	}
	//计算出生日期
	function getBirthdayByIdCard(val) {
		if (isBlank(val)) {
			return "";
		}
		var birthdayValue = null;
		if (15 == val.length) { // 15位身份证号码
			birthdayValue = val.charAt(6) + val.charAt(7);
			if (parseInt(birthdayValue) < 10) {
				birthdayValue = '20' + birthdayValue;
			} else {
				birthdayValue = '19' + birthdayValue;
			}
			birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9)
					+ '-' + val.charAt(10) + val.charAt(11);
		}
		if (18 == val.length) { // 18位身份证号码
			birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8)
					+ val.charAt(9) + '-' + val.charAt(10) + val.charAt(11)
					+ '-' + val.charAt(12) + val.charAt(13);
		}
		/*
		 * if(isValidDateTime(birthdayValue) === false){ birthdayValue = ""; }
		 */
		return birthdayValue;
	}

	/**
	 * 计算周岁。
	 * @param strBirthday
	 * @returns {*}
	 */
	function jsGetAge(strBirthday) {
		if (isBlank(strBirthday)) {
			return "";
		}
		var returnAge;
		var strBirthdayArr = strBirthday.split("-");
		var birthYear = strBirthdayArr[0];
		var birthMonth = strBirthdayArr[1];
		var birthDay = strBirthdayArr[2];

		d = new Date();
		var nowYear = d.getFullYear();
		var nowMonth = d.getMonth() + 1;
		var nowDay = d.getDate();

		if (nowYear == birthYear) {
			returnAge = 0;//同年 则为0岁
		} else {
			var ageDiff = nowYear - birthYear; //年之差
			if (ageDiff > 0) {
				if (nowMonth == birthMonth) {
					var dayDiff = nowDay - birthDay;//日之差
					if (dayDiff < 0) {
						returnAge = ageDiff - 1;
					} else {
						returnAge = ageDiff;
					}
				} else {
					var monthDiff = nowMonth - birthMonth;//月之差
					if (monthDiff < 0) {
						returnAge = ageDiff - 1;
					} else {
						returnAge = ageDiff;
					}
				}
			} else {
				returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
			}
		}
		return returnAge;//返回周岁年龄
	}

	/**
	 * 计算性别
	 * @param strBirthday
	 * @returns {String}
	 */
	function jsGetSex(strBirthday) {
		if (strBirthday.length != 18) {
			return "1";
		}
		var returnSex = "";
		if (parseInt(strBirthday.substr(16, 1)) % 2 == 1) {
			returnSex = "1";// 男
		} else {
			returnSex = "2";// 女
		}
		return returnSex;
	}
</script>