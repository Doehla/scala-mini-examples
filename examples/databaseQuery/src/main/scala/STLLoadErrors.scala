//import java.sql.{Connection, DriverManager}
//
//object STLLoadErrors {
//  def main(args: Array[String]): Unit = {
//    val driver = "com.amazon.redshift.jdbc42.Driver"
//    val url = "jdbc:redshift://styleseat-data-warehouse.cze9b28s0jdw.us-east-1.redshift.amazonaws.com:5439/dev"
//    val username = "styleseat"
//    val password = "aL4j4o8K9CUwOxzK9E5O"
//
//    val sql =
//      """select * from stl_load_errors
//        |order by starttime desc
//        |limit 1
//        |""".stripMargin
//
//    Class.forName(driver)
//    val conn = DriverManager.getConnection(url, username, password)
//    val stmt = conn.createStatement()
//    stmt.setFetchSize(1000)
//    val resultSet = stmt.executeQuery(sql)
//
//    while (resultSet.next) {
//      println("-"*100)
//      println("stl_load_errors detail:")
//      println("|--filename: " + resultSet.getString("filename").trim())
//      println("|--line_number: " + resultSet.getInt("line_number"))
//      println("|--colname: " + resultSet.getString("colname").trim())
//      println("|--raw_line: " + resultSet.getString("raw_line").trim())
//      println("|--raw_field_value: " + resultSet.getString("raw_field_value").trim())
//      println("|--err_reason: " + resultSet.getString("err_reason").trim())
//    }
//
//    conn.close()
//  }
//}
