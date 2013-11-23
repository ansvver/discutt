/**
 * @fileoverview for user_edit.html
 * @author royguo1988@gmail.com
 */

var User = function() {
    this.nickName = null;
    this.email = null;
    this.password = null;
    this.confirmPassword = null;
    this.userGroup = null;
}

User.prototype.isValid = function() {
    if(!this.nickName || !this.email || !this.password) {
        return false;
    }
    return true;
}


function FormValid() {
	// Gather form data
	var user = new User();
	user.nickName = $("#user\\.nickName").val();
	user.email= $("#user\\.email").val();
	user.password= $("#user\\.password").val();
	user.userGroup = $("#user\\.userGroup").val();
	if(user.isValid()){
		return true;
	}
	return false;
}

$(document).ready(function(){
	$("#user-edit-form").submit(function(){
		if(!FormValid()){
			alert("请完整填写数据");
			return false;
		}
		return true;
	});
});