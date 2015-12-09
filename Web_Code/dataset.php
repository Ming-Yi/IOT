<?php

    $host = "主機IP";
    $user = "使用者帳號";
    $pass = "使用者密碼";

    //資料庫資訊
    $databaseName = "資料庫";
    $tableName = "資料表";


    //連結資料庫
    $con = mysql_connect($host,$user,$pass);
    $dbs = mysql_select_db($databaseName, $con);


    for($i=0;$i<100;$i++) {
		$sql = "INSERT INTO $tableName (value,state) VALUES (".rand(0,1023).",".rand(0,1).")";
		mysql_query($sql);
    }
?>