package SparkPkg

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.log4j.{Level, Logger}

object MovieRatingLab1 {

  def main(args: Array[String]) {

    //in windows needed this hadoop home
    System.setProperty("hadoop.home.dir","C:\\Users\\mraje_000\\Documents\\hadoopforspark");

    val sparkConf = new SparkConf().setAppName("UserMovieRating").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    //to display only errors in the console
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)

    val input=sc.textFile("Spark WordCount\\u.data")
    val userdetails=input.map(line=>{line.split("\t")}) //split each user information by tab
                    .map(line=>(line(0),line(1))) //get user id , item id
    val useritems = userdetails.groupByKey() //group all the items for each user


    val userd = useritems.filter( (x ) => x._2.size > 25 ) // get the user rated more than 25 items

    val output=userd.map(x => (x._1, x._2.size)).cache() // get userid and number of items




    
    output.saveAsTextFile("Spark WordCount\\mvrated")

    val mvout=output.collect()

    var s:String="Movie rating by User details \n"
    mvout.foreach{case(userid, mvrated)=>{

      s+="User id  = " + userid   + " , No of movies  Rated " + mvrated + "\n"

    }}
    print(s)
  }

}

//s+="User id  = " + userid   + " , No of movies  Rated " + items.size + "\n"
//s+="user id  = " + userid  + " , items = " +  items.mkString("(",",",")") + " , No of movies items Rated " + items.size + "\n"
