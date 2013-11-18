/**
 * @fileoverview For Users/register.html
 * @author Roy Guo
 */

var validator = new play.utils.FormatValidator();
// 由于邮箱是Ajax请求，所以等待邮箱验证数据返回再提交
var useEmailCheckSubmit = false;

$(document).ready(function(){
	$("#user-create-btn").click(function() {
		if(!FormValid()) return;
		useEmailCheckSubmit = true;
	});
});

function FormValid() {
	if(CheckEmail()  && CheckNickName() 
						&& CheckPassword() 
						&& ConfirmPassword()) {
		return true;
	}
	return false;
}

function CheckEmail() {
	if(!validator.isEmailAddress($("#user\\.email").val())) {
		$("#email-text").html("<font color=red>邮箱格式不正确</font>");
		return false;
	} else {
		// 后台验证邮箱是否已被占用
		$.post(ctx+"Users/checkEmail",{email:$("#user\\.email").val()},function(result){
			if(result.valid){
				$("#email-text").html("<font color=green>邮箱可用</font>");
				if(useEmailCheckSubmit) {
					$("#user-register-form").submit();
				}
			}else{
				$("#email-text").html("<font color=red>该邮箱已被占用</font>");
			}
		});
	}
	return true;
}

function CheckNickName() {
	if(!validator.fitLength($("#user\\.nickName").val(), 2, 8)) {
		$("#nickName-text").html("<font color=red>昵称需要2到8个字符</font>");
		return false;
	}
	$("#nickName-text").html("<font color=green>正确</font>");
	return true;
}

function CheckPassword() {
	if(!validator.fitLength( $("#user\\.password").val(), 8, 100)) {
		$("#password-text").html("<font color=red>密码至少8个字符</font>");
		return false;
	}
	$("#password-text").html("<font color=green>正确</font>");
	return true;
}

function ConfirmPassword() {
	if($("#user\\.password").val() != $("#user\\.passwordConfirm").val()) {
		$("#passwordConfirm-text").html("<font color=red>两次输入的密码不一致</font>");
		return false;
	}else if(!validator.fitLength( $("#user\\.passwordConfirm").val(), 8, 100)) {
		$("#passwordConfirm-text").html("<font color=red>密码至少8个字符</font>");
		return false;
	}
	$("#passwordConfirm-text").html("<font color=green>正确</font>");
	return true;
}