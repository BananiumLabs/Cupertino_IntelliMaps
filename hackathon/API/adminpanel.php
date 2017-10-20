<!--
  * AdminPanel
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
  *
-->
<html>
<head>
    <title>Displaying MySQL Data in HTML Table</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

    <style type="text/css">
        body {
            font-size: 15px;
            color: #343d44;
            font-family: "segoe-ui", "open-sans", tahoma, arial;
            padding: 0;
            margin: 0;
        }
        table {
            margin: auto;
            font-family: "Lucida Sans Unicode", "Lucida Grande", "Segoe Ui";
            font-size: 12px;
        }

        h1 {
            margin: 25px auto 0;
            text-align: center;
            text-transform: uppercase;
            font-size: 17px;
        }

        table td {
            transition: all .5s;
        }
        a { color: #fff; }
        /* Table */
        .data-table {
            border-collapse: collapse;
            font-size: 14px;
            min-width: 537px;
        }

        .data-table th,
        .data-table td {
            border: 1px solid #e1edff;
            padding: 7px 17px;
        }
        .data-table caption {
            margin: 7px;
        }

        /* Table Header */
        .data-table thead th {
            background-color: #508abb;
            color: #FFFFFF;
            border-color: #6ea1cc !important;
            text-transform: uppercase;
        }

        /* Table Body */
        .data-table tbody td {
            color: #353535;
        }
        .data-table tbody td:first-child,
        .data-table tbody td:nth-child(4),
        .data-table tbody td:last-child {
            text-align: right;
        }

        .data-table tbody tr:nth-child(odd) td {
            background-color: #f4fbff;
        }
        .data-table tbody tr:hover td {
            background-color: #ffffa2;
            border-color: #ffff0f;
        }

        /* Table Footer */
        .data-table tfoot th {
            background-color: #e5f5ff;
            text-align: right;
        }
        .data-table tfoot th:first-child {
            text-align: left;
        }
        .data-table tbody td:empty
        {
            background-color: #ffcccc;
        }
    </style>
    </head>
<body>
    <h1>Data Manager</h1>
    <table class="data-table">
        <caption class="title">Incidents Sorted By Submission ID</caption>
        <thead>
            <tr>
                <th>NO</th>
                <th>Latitude</th>
                <th>Longitude</th>
                <th><a href="adminpanel_sev.php">Severity</a></th>
                <th>Type Of Incident</th>
                <th>Email Address</th>
                <th>Location</th>
                <th>Description</th>
                <th>Delete</th>
            </tr>
        </thead>
        <tbody>
        <?php
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
        $query = mysqli_query($conn,"SELECT * FROM hackathon");
        $no     = 1;
        $total  = 0;
        while ($row = mysqli_fetch_array($query))
        {
          $description = chunk_split($row['des'], 10);
            //$amount  = $row['amount'] == 0 ? '' : number_format($row['amount']);
            echo '<tr>
                    <td>'.$no.'</td>
                    <td>'.$row['latitude'].'</td>
                    <td>'.$row['longitude'].'</td>
                    <td>'.$row['severity'].'</td>
                    <td>'.$row['type'].'</td>
                    <td>'.$row['email'].'</td>
                    <td>'.$row['street'].'</td>
                    <td>'.$description.'</td>
                    <td><a class="btn btn-danger" href="https://2tbs.club/customapi.php?request=55&index='.$row['currentInd'].'" >
                      <i class="fa fa-trash-o fa-lg"></i> Delete</a>
                    </td>

                </tr>';
            //$total += $row['amount'];
            $no++;
            $total++;


        }?>
        <!--<tr>
                <td><?php echo $no; ?></td>

                <td>Latitude: <input type="text" name="lat" id="lat"></td>
                <td>Longitude: <input type="text" name="lon" id="lon"></td>
                <td>Severity: <input type="text" name="sev" id="sev"></td>
                <td>Type: <input type="text" name="type" id="type"></td>



                <td><a class="btn btn-success" onclick="openlink()">
                  <i class="fa fa-plus-circle fa-lg"></i> Add</a>
                </td>
                <script>
                function openlink(){
                    var lat = document.getElementById(lat).value;
                  var lon = document.getElementById(lon).value;
                  var sev = document.getElementById(sev).value;
                  var type = document.getElementById(type).value;

                  var link = "https://2tbs.club/customapi.php?long="+ lon + "&lat="+lat+

                }
                </script>
        </tr>-->
        </tbody>
        <tfoot>
            <tr>
                <th colspan="4">TOTAL</th>
                <th><?=number_format($total)?></th>
            </tr>
        </tfoot>
    </table>
</body>
</html>
