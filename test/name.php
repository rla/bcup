<?php
	require("osad/algus.php");
	
	if (is_null($usrid)) {
		die("Requires logged-in user");
	}
	
	$databaseEnvKey = "";
	switch($_SERVER['SERVER_NAME']){
		case "ee.bcup.pri.ee":
			$databaseEnvKey = "ee";
			break;
		case "fi.bcup.pri.ee":
			$databaseEnvKey = "fi";
			break;
		case "alpha.bcup.pri.ee";
			$databaseEnvKey = "alpha";
			break;
		default:
			$databaseEnvKey = "ee";
}
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Test</title>
</head>
<body>
	<applet code="ee.pri.bcup.client.NameTestApplet" archive="bcup-client.jar" width="200" height="200">
		<param name="host" value="serverid.ee" />
		<param name="port" value="3003" />
		<param name="userId" value="<?= $usrid; ?>" />
		<param name="gameId" value="1" />
		<param name="roomId" value="1" />
		<param name="sessionKey" value="<?= session_id(); ?>" />
		<param name="databaseEnvKey" value="<?= $databaseEnvKey; ?>" />
	</applet>
</body>
</html>
