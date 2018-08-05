package com.pbrandwijk.app

import com.pbrandwijk.app.servlets.ProductServlet
import org.scalatra.test.scalatest._

class ProductServletTests extends ScalatraFunSuite {

  addServlet(classOf[ProductServlet], "/*")

  test("GET / on ProductServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
