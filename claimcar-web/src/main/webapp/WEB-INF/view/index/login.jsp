<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>理赔系统</title>
<!-- Core CSS - Include with every page -->
<link href='plugins/bootstrap/css/bootstrap.css' rel="stylesheet">
<!--[if IE 7]> <link href='ie-patch/font-awesome-ie7.min.css'><![endif]-->

<!-- Theme CSS - Include with every page -->


<script language="javascript">
	if (self != top) {
		//top.location = self.location;
	}
</script>
<shiro:authenticated>
	<script type="text/javascript">
		window.location.href = '${ctx}/';
	</script>
</shiro:authenticated>
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="#">Arc5 新理赔系统 
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<p class="login-box-msg">Sign in to start your session</p> 
			
			<form id="form" role="form" method="post" action="<c:url value='login'/>">
				<div class="form-group has-feedback">
					<input id="username" type="username" class="form-control" name="username"
						value="admin" placeholder="UserName"> <span
						class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input id="password" type="password" class="form-control" name="password"
						placeholder="Password" value="0000"> <span
						class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
 
				<shiro:notAuthenticated>
				<p id="loginBoxMsg" class="login-box-msg">
				<c:choose>
					<c:when
						test="${shiroLoginFailure eq 'ins.platform.shiro.exception.CaptchaException'}">
							<i class="fa fa-exclamation-triangle"></i>验证码错误，请重试.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'ins.platform.shiro.exception.UsernameEmptyException'}">
							<i class="fa fa-exclamation-triangle"></i>用户名不能为空.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'ins.platform.shiro.exception.PasswordEmptyException'}">
							<i class="fa fa-exclamation-triangle"></i>密码不能为空.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'org.apache.shiro.authc.UnknownAccountException'}">
							<i class="fa fa-exclamation-triangle"></i>用户${username}不存在.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'org.apache.shiro.authc.IncorrectCredentialsException'}">
							<i class="fa fa-exclamation-triangle"></i>用户或密码错误.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'org.apache.shiro.authc.DisabledAccountException'}">
							<i class="fa fa-exclamation-triangle"></i>用户状态为无效.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'org.apache.shiro.authc.ExpiredCredentialsException'}">
							<i class="fa fa-exclamation-triangle"></i>用户密码不在有效期内.
					</c:when>
					<c:when
						test="${shiroLoginFailure eq 'org.apache.shiro.authc.ExcessiveAttemptsException'}">
							<i class="fa fa-exclamation-triangle"></i>您输入的密码错误次数过多，已被冻结.
					</c:when>
					<c:when test="${shiroLoginFailure ne null}">
						<i class="fa fa-exclamation-triangle"></i>登录认证错误，请重试.
					</c:when>
					<c:otherwise>
					<!-- 
						<p class="login-box-msg">请输入用户名密码进行登录</p>
					 -->
					</c:otherwise>
				</c:choose>
				</p>
			</shiro:notAuthenticated>
			<shiro:authenticated>
				<script type="text/javascript">
					location.href = '${ctx}/';
				</script>
			</shiro:authenticated>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label> <input type="checkbox" name="rememberMe">
								Remember Me
							</label>
						</div>
					</div>
					<!-- /.col -->
					<div class="col-xs-4">
						<button id="submit" type="submit" class="btn btn-primary btn-block btn-flat">Sign
							In</button>
					</div>
					<!-- /.col -->
				</div>
				<input type=hidden id="ssid" name="ssid"
					value="<%=request.getSession().getId()%>">
			</form>
 
		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->
	<!-- Core Scripts - Include with every page -->
	<script type='text/javascript' src='lib/jquery/1.9.1/jquery.js'></script>

	<script>
		$(function() {
			if(${param.forceLogout == "1"}){
				$("#loginBoxMsg").html("<i class='fa fa-exclamation-triangle'></i>你已被管理员强制下线.");
			}
			
			if(${param.kickout == "1"}){
				$("#loginBoxMsg").html("<i class='fa fa-exclamation-triangle'></i>您的账号已在另一地点登录，您已被迫下线.");
			}
			
			$('input').iCheck({
				checkboxClass : 'icheckbox_square-blue',
				radioClass : 'iradio_square-blue',
				increaseArea : '20%' // optional
			});
 
			$("#submit").click(function(){
				var username = $("#username").val();
				var password = $("#password").val();
				if(username == null || username == ""){
					$("#loginBoxMsg").html("<i class='fa fa-exclamation-triangle'></i>用户名不能为空.");
					$("#username").focus();
					return false;
				}else if(password == null || password == ""){
					$("#loginBoxMsg").html("<i class='fa fa-exclamation-triangle'></i>密码不能为空.");
					$("#password").focus();
					return false;
				}else{
					$("form").submit();
				}
			});
			
		});
	</script>
</body>
</html>
