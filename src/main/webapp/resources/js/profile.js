// datetimepicker
	// http://xdsoft.net/jqplugins/datetimepicker/
$('#dob').datetimepicker({
	timepicker : false,
	format : 'd/m/Y',
	formatDate : 'Y/m/d'
});
// append photo to img
function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#avatar').attr('src', e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
$("#photo").change(function() {
	readURL(this);
});