$(function() {   // 控件事件“收信件”
	    //window.setInterval("checkMessage1()",1000);
	});
function checkMessage1(){
	    var url = contextPath + "/staLetter/findNewMessage/";
		$.post(url,
       	  function(data){
			if(data[0] =='newmsg'){
				//bootbox.alert("您有一条新信息");
				$("#showMsg").show();
			}else{
				$("#showMsg").hide("slow");
			}
			
			$("#msgList").html(data[1]);

       	  }
	    );  
	}  	