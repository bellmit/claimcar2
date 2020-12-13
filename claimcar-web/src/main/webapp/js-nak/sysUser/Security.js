

$(function(){
		$("#changePassword").validate({
			rules:{
				oldPassword:{
					required:true,
					minlength:6
				},
				password:{
					required:true,
					minlength:6
				},
				confirm_password:{
					required:true,
					minlength:6,
					equalTo:"#newPassword"
				}
				
			},
			focusCleanup: true,
			
		});
		$("#email").validate({
			rules:{
				email:{
					required:true,
					email:true
				},
				emailcode:{
					required:true
				}
			},
			focusCleanup: true,
			
		});
		//发送邮件事件控件
		$("#button0").click(function(){
			$.ajax({
				url:contextPath + "/sysUser/sendEmail",
				type:"GET",
				data:{email:$("#inputEmail3").val()},
				dataType:"json",
				success:function(obj){
					if(obj.statusText =="true"){
						alert("邮件已发送，请登录邮箱查看验证码");
						$("#code").val(obj.data);
					}else{
							$("#message3").html("<i style='color: red;'>该邮箱已经被注册,请重新输入</i>");
						
					}
					
				}
			});
		});
		//提交事件
		$("#submit").click(function(){
			var emailCode = "您的验证码是:"+$("#emailcode").val();
			if(emailCode==$("#code").val()){
				$("#email").submit();
			}else{
				$("#message2").html("<i style='color: red;'>验证码不正确,请重新输入</i>");
				
			}
		});
		//密码异步校验
		$("#inputPassword").blur(function(){
			$.ajax({
				url:contextPath + "/sysUser/checkedPassword",
				type:"POST",
				data:{"userCode":$("#userCode").val(),"password":$("#inputPassword").val()},
				dataType:"json",
				success:function(data){
					if(data==true){
						$("#message1").html("<i style='color: green;'>密码正确</i>");
					}else{
						$("#message1").html("<i style='color: red;'>密码不正确,请重新输入</i>");
					}
				}
				
			});
		});
		$("#inputPassword").focus(function(){
			$("#message1").empty();
		});
		$("#emailcode").focus(function(){
			$("#message2").empty();
		});
		$("#inputEmail3").focus(function(){
			$("#message3").empty();
		});
	});