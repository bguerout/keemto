// Put all your lovely jQuery / Javascript goodies right down here.
var toggleFlag = 0;
$('#toggleHeaderButton').click(function() {
	$('#header').slideToggle(500);

	var flagStatus = toggleFlag++ % 2;
	if (flagStatus == 0) {
		$(this).html("Show Notifications");
	} else {
		$(this).html("Hide Notifications");
	}

});
