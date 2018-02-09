/* ---------------------------------------------------------------------
*   CS5542 - ICE 4
*   Confusion Matrix
* ---------------------------------------------------------------------*/

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import scala.collection.mutable.ListBuffer

object ConfusionMatrix {

  def main(args: Array[String]) {

    //configuration
    val sparkConf = new SparkConf().setAppName("Spark").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //read data
    val input = sc.textFile("data//ice4data.txt")
    val lables = input.map(line=>{line.split("\t")}) //split each user information by tab
                      .map(line=>(line(0),line(1)))

    // 1 = man, 0 = woman
    val predictionAndLabels =  lables.map(x => (if (x._1.equals("man"))  "0".toDouble
                                                  else "1".toDouble,
                                                if (x._2.equals("man"))  "0".toDouble
                                                  else "1".toDouble))
    val out=lables.collect()

    //printing dataset
    var s:String="\nDataset: \n"
    out.foreach{case(expect, predict )=>{
      s+=" Expected = " + expect + " , Predicted = " + predict + "\n"
    }}
    println(s)

    //calculate metrics
    val metrics = new MulticlassMetrics(predictionAndLabels)

    //  |=================== Confusion matrix ==============
    //  |          | Predicted = +         Predicted = -
    //  |----------+----------------------------------------
    //  |Actual = +|    TP                     FN
    //  |Actual = -|    FP                     TN
    //  |===================================================
    val confusionMatrix = metrics.confusionMatrix
    val a = confusionMatrix(0, 0)   // True positive
    val b = confusionMatrix(0, 1)   // False negative
    val c = confusionMatrix(1, 0)   // False positive
    val d = confusionMatrix(1, 1)   // True negative
    val total = a + b + c + d

    //Printing the values
    println("\n--------")
    println("Metrics:")
    println("--------")
    println("\nConfusion Matrix:\n" + confusionMatrix + "\n")

    //Accuracy = (a + d) / (a + b + c + d)
    println("Accuracy = " + "%.2f".format((a + d) / total))
    //Misclassification Rate = Error Rate = 1 - Accuracy
    println("Misclassification Rate = Error Rate = " + "%.2f".format((b + c) / total))
    //Precision = a / (a + c)
    println("Precision = " + "%.2f".format(a / (a + c)))
    //True Positive Rate = Recall = Sensitivity = a / (a + b)
    println("True Positive Rate = Recall = Sensitivity =  " + "%.2f".format(a / (a + b)))
    //False Positive Rate = c / (c + d)
    println("False Positive Rate = " + "%.2f".format(c / (c + d)))
    //True Negative Rate = Specificity = d / (c + d)
    println("True Negative Rate = Specificity = " + "%.2f".format(d / (c + d)))
    //False Negative Rate = b / (a + b)
    println("False Negative Rate = " + "%.2f".format(b / (a + b)))
    //Prevalence = (a + b) / (a + b + c + d)
    println("Prevalence = " + "%.2f".format((a + b) / total))
    //F-measure = 2a / (2a + b + c)
    println("F-measure = " + "%.2f".format(2*a / (2*a + b + c)))


      }

}