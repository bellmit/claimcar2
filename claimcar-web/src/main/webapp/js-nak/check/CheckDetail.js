
$(function() {
	// 控件事件
	$("button.btn-submit").click(function(){
		var type = $("#type").val();
		var checkInReason = "";
		var checkOutReason = "";
		if(type == "checkIn"){
			checkInReason = $("#checkInReason").val();
		}
		if(type == "checkOut"){
			checkOutReason = $("#checkOutReason").val();
		}
		$.ajax({
			type : "POST",
			url : contextPath + "/checkDetail/checkDetail/" + $("#type").val() + "/" + $("#workDay").val(),
			async : false,
			data : {
				checkInReason : checkInReason,
				checkOutReason : checkOutReason
			},
			success : function(obj) {
				if (obj.statusText == 'checkIn') {
					bootbox.alert("签到成功");
					location.href =  contextPath + "/checkDetail/preCheckDetail";
				} else if(obj.statusText == 'checkOut'){
					bootbox.alert("签退成功");
					location.href =  contextPath + "/checkDetail/preCheckDetail";
				} else{
					bootbox.alert("签到/签退不成功，请尝试重新刷新页面！");
				}
			},
		})
	});
	
});