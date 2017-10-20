<?php
/**
  * DatabaseTest
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
echo "Connected successfully";
?>
