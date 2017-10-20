<?php
/**
  * CustomAPI
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  * @author     Eric Qian <eric@enumc.com>
  * @copyright  2017 Eric Qian
  * @license    Apache License 2.0
  * @version    SVN: $Id$
  * @link       https://enumc.com
  */
$hostsql = getenv('OPENSHIFT_MYSQL_DB_HOST');
$hostport = getenv('OPENSHIFT_MYSQL_DB_PORT');
$username = "admin329264Z";
$password = "I6bX8ZfRv-X8";
$database = "web";
// Create connection
$conn = new mysqli($hostsql, $username, $password, $database, $hostport);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
//echo "Connected successfully!";
$get = $_GET['request'];
if($get == '') {
  die("No SourceCode For U :D");
}

if ($get == 1) {
  $sth = mysqli_query($conn,"SELECT * FROM hackathon");
  $rows = array();
  while($r = mysqli_fetch_assoc($sth)) {
      $rows[] = $r;
  }
  print json_encode($rows);
}
else if ($get == 20) {
  $sth = mysqli_query($conn,"SELECT * FROM hackathon ORDER BY severity");
  $rows = array();
  while($r = mysqli_fetch_assoc($sth)) {
      $rows[] = $r;
  }
  print json_encode($rows);
}
else if($get == 0){
  $long = $_GET['long'];
  $lat = $_GET['lat'];
  $sev = $_GET['severity'];
  $type = $_GET['type'];
  $email = $_GET['email'];
  $street = $_GET['street'];
  $desc = $_GET['des'];
  $num_rows = 2;
  $sth = mysqli_query($conn,"SELECT * FROM hackathon");
  $rows = array();
  while($r = mysqli_fetch_assoc($sth)) {
      $num_rows++;
  }
  $sql = "INSERT INTO hackathon (currentInd, latitude, longitude, severity, type, email, street, des)
    VALUES ($num_rows, $long, $lat, $sev, $type, $email, $street, $desc)";
  if ($conn->query($sql) === TRUE) {
      echo "Success: New record created.";
  } else {
      echo "Error: " . $sql . "<br>" . $conn->error;
  }
}
else if($get == 2) {
  $index = $_GET['index'];
  $sql = "DELETE FROM hackathon WHERE currentInd=" . $index;

  if ($conn->query($sql) === TRUE) {
      echo "Success: Record deleted";
  } else {
      echo "Error deleting record: " . $conn->error;
  }
}
else if($get == 55) {
  $index = $_GET['index'];
  $sql = "DELETE FROM hackathon WHERE currentInd=" . $index;

  if ($conn->query($sql) === TRUE) {
      echo "Success: Record deleted";
  } else {
      echo "Error deleting record: " . $conn->error;
  }
  //header('Location: ' . $_SERVER["HTTP_REFERER"] );
  exit;
}
else if($get == 999) {
  mysqli_query($conn,"DELETE FROM hackathon" );
  echo "Success: All Data Wiped.";
}
$conn->close();

?>
