/**
 * @fileoverview for group_edit.html
 * @author royguo1988@gmail.com
 */

var UserGroup = function() {
    this.name = null;
    this.detail = null;
    this.resources = [];
}

function FormValid() {
	// Gather form data
	var group = new UserGroup();
	group.name = $("#group\\.name").val();
	group.detail = $("#group\\.detail").val();
	// Resources
	$.each($(".resource-id"), function(index, r){
		if($(r).prop("checked")) {
			$('<input>').attr({
				'type' : 'hidden',
				'name' : 'group.resources.id',
				'value': $(r).val()
			}).appendTo('#group-edit-form');
			group.resources.push($(r).val());
		}
	});
	// TODO(Roy Guo) Do some validation works.
	return true;
}

$(document).ready(function(){
	$("#group-edit-form").submit(function(){
		if(!FormValid()){
			return false;
		}
		return true;
	});
});