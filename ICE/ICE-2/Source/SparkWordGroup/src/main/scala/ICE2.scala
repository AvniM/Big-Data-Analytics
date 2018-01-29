/* ---------------------------------------------------------------------
*   CS5542 - ICE2
*   Group words based on their first character
*   Avni Mehta, Raji Muppala
* ---------------------------------------------------------------------*/

//Import library
import org.apache.spark.{SparkContext, SparkConf}

object ICE2 {

  def main(args: Array[String]) {

    //Setting the spark configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    //Setting the spark context
    val sc=new SparkContext(sparkConf)

    //Reading data
    val input=sc.textFile("input")

    //Spark Transformations
    val wc=input.flatMap(line=>line.split(" ")).distinct()
                .map(word=>(word.charAt(0),word)).cache()
    val output=wc.groupBy(word => word._1).mapValues(_.map(_._2).mkString(","))

    //Spark Actions
    output.saveAsTextFile("ICE2Output")

    val o=output.collect()
    var s:String="Words:Count \n"
    o.foreach{case(first_letter,words)=>{

      s+=first_letter + "," + words +"\n"

    }}
  }
}