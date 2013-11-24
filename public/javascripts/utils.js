/**
 * 格式化工具类.
 */

play.utils.FormatValidator = function(){
  console.log("Hello");
};

/**
 * 检测是否是Email地址.
 */
play.utils.FormatValidator.prototype.isEmailAddress = function(address){
	var arr = address.match(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/);
	if(arr!=null){
		return true;
	}
	return false;
}

play.utils.FormatValidator.prototype.isEmailAddress = function(address) {
	var arr = address.match(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/);
	if(arr!=null){
		return true;
	}
	return false;
}

play.utils.FormatValidator.prototype.fitLength = function(text, min, max) {
	if(!text || text.length > max || text.length < min) {
		return false;
	}
	return true;
}


/**
 * 浮动提示框工具.
 */
play.utils.Tips = function(){
	this.e = null;
	this.initialize_();
};

play.utils.Tips.prototype.initialize_ = function(){
	var e = $("<div class='alert navbar-fixed-top tips'>Tips Box</div>");
	$("body").append(e);
	this.e = e;
};

play.utils.Tips.prototype.popup_ = function(msg, clazz, duration) {
	if(!msg) return;
	if(!duration) duration = 1000;
	this.e.fadeIn("fast");
	this.e.addClass(clazz);
	this.e.html(msg);
	this.e.fadeOut(duration);
};

play.utils.Tips.prototype.success = function(msg, duration) {
	this.popup_(msg, "alert-success", duration);
};

play.utils.Tips.prototype.fail = function(msg, duration) {
	this.popup_(msg, "alert-error", duration);
};