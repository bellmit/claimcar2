$('a:contains("强制下线")').click(function(e){
	$.ajax({
		type : "POST",
		url : $(e.target).attr("id"),
		data : "",
		async : false,
		success : function(obj) {
			if (obj.status == '200') {
//				var displayIndex = $(e.target).parent().parent().html();
//				$("#resultDataTable").find("tr").eq(displayIndex).remove();
				bootbox.alert("强制退出成功");
			} else {
				bootbox.alert("强制退出失败，请稍后重试");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			bootbox.alert(textStatus + errorThrown);
		}
	});
});