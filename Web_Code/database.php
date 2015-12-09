<?php

    //使用者資訊
    $host = "主機IP";
    $user = "使用者帳號";
    $pass = "使用者密碼";

    //資料庫資訊
    $databaseName = "資料庫";
    $tableName = "資料表";


    //連結資料庫
    $con = mysql_connect($host,$user,$pass);
    $dbs = mysql_select_db($databaseName, $con);


    //資料庫Sql query語法
    $sql = "SELECT * FROM $tableName";

    //執行query語法
    $result = mysql_query($sql);

    //取出資料
    $data=array();
    while ($row = mysql_fetch_array($result)){
      array_push($data, $row);
    }

    //輸出並且轉成json格式
    echo json_encode($data);
?>
