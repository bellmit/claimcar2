//--------------------------------------------------------------------------------//
//--------------高院信息查询-------------
var ajaxEdit;
var vinNoFlag =true;
//高院信息详情
function showCourtView(id,type) {
	var url = "";
	var titile = "";
	var height = "95%";
	if(type == 1){
		url = "/claimcar/courtMessage/viewCourtParty.do/";
		title = "当事人基本信息";
		height = "70%";
	} else if(type == 2){
		url = "/claimcar/courtMessage/viewCourtIdentify.do/";
		height="95%";
	} else if(type == 3){
		url = "/claimcar/courtMessage/viewCourtCompensation.do/";
		height="70%";
	} else if(type == 4){
		url = "/claimcar/courtMessage/viewCourtClaim.do/";
	} else if(type == 5){
		url = "/claimcar/courtMessage/viewCourtLitigation.do/";
		height = "70%";
	} else if(type == 6){
		url = "/claimcar/courtMessage/viewCourtConfirm.do/";
		height="70%";
	} else if(type == 7){
		url = "/claimcar/courtMessage/viewCourtMediation.do/";
		height="95%";
	}
	layerIndex = layer.open({
		title : titile,
		type : 2,
		area : [ '90%', height ],
		fix : true, // 固定
		maxmin : true,
		content : url+id
	});
	
}

/**
 * setQtip
 * @param element
 */
function setQtip(element){
	$(element).removeAttr("datatype").removeClass("Validform_error").qtip('destroy',true);
}

/**
 * setSelectMust
 * @param element
 */
function setSelectMust(element){
	$(element).attr("datatype","selectMust");
}



