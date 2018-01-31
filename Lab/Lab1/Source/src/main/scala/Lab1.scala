/* ---------------------------------------------------------------------
*   CS5542 - Lab1
*   Find the users who have rated more than 25 items
*   Avni Mehta, Raji Muppala
* ---------------------------------------------------------------------*/

//Import library
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object Lab1 {

  def main(args: Array[String]) {

    // Setting the environment
    val sparkConf = new SparkConf().setAppName("SparkLab").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //to display only errors in the console
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)

    // Reading data
    val input = sc.textFile("u.data")

    //Map and filter
    val users = input.map(line=>line.toString.split("\t")(0))
    val userCount = users.countByValue().toSeq
                    .filter(_._2 > 25)
                    .sortBy(_._2)

    //Print Output
    println("***Movie Review Analytics***\n")
    println("Below is a list of (User id, #Reviews) ")
    userCount.foreach(println)

    //Save as text file
    sc.parallelize(userCount).saveAsTextFile("lab1")

  }
}