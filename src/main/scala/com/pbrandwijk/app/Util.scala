package com.pbrandwijk.app

import net.liftweb.json.JsonParser.ParseException
import net.liftweb.json._
import org.slf4j.LoggerFactory

/**
  * Object containing helper methods used in the servlets.
  *
  */
object Util {

  // create logger for class
  val logger =  LoggerFactory.getLogger(getClass)

  /**
    * Helper method to do the exception handling of parsing a JSON string.
    *
    * @param jsonString
    * @return Parsed JValue if no exception, else None
    */
  def parseJsonString(jsonString: String): Option[JValue] = {
    try {
      return Some(parse(jsonString))
    } catch {
      case e: ParseException => {
        logger.warn("Parse exception: " + e.getMessage)
        return None
      }
    }
  }

  /**
    * Helper method to parse strings into integers, or fail gracefully
    *
    * @param in The string to parse as an integer
    * @return An Option value for the integer
    */
  def toInt(in: String): Option[Int] = {
    try {
      Some(Integer.parseInt(in.trim))
    } catch {
      case e: NumberFormatException => None
    }
  }

  /**
    * Helper method to parse strings into doubles, or fail gracefully
    *
    * @param in The string to parse as a double
    * @return An Option value for the double
    */
  def toDouble(in: String): Option[Double] = {
    try {
      Some(in.toDouble)
    } catch {
      case e: NumberFormatException => None
    }
  }

}
