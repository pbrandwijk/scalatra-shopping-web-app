package com.pbrandwijk.app.servlets

import com.pbrandwijk.app.Model
import org.scalatra.test.scalatest._

class ProductServletTest extends ScalatraFunSuite {

  addServlet(classOf[ProductServlet], "/products/*")

  val invalidJson = """{ "id": "book001", "descr"""
  val incompleteProductBody = """{ "id": "book001", "description": "The Da Vinci Code", "price": 9.95 }"""
  val addProduct1Body = """{ "id": "book001", "description": "The Da Vinci Code", "price": 9.95, "stock": 10 }"""
  val addProduct2Body = """{ "id": "book002", "description": "Oil!", "price": 8.49, "stock": 5 }"""
  val addProduct3Body = """{ "id": "book003", "description": "The Jungle Book", "price": 4.99, "stock": "three" }"""

  test("GET /products/ on ProductServlet should return status 200") {
    get("/products/") {
      status should equal (200)
      Model.products.size should equal (0)
    }
  }

  test("POST /products/addProduct on ProductServlet with invalid JSON should do nothing and give error " +
    "message in header") {
    submit("POST", "/products/addProduct", Seq.empty, Seq.empty, invalidJson) {
      status should equal (200)
      Model.products.size should equal (0)
      header.get("ACK") should not be None
      header.get("ACK").get should equal ("Request body could not be parsed into JSON")
    }
  }

  test("POST /products/addProduct on ProductServlet with incomplete JSON should do nothing and give error " +
    "message in header") {
    submit("POST", "/products/addProduct", Seq.empty, Seq.empty, incompleteProductBody) {
      status should equal (200)
      Model.products.size should equal (0)
      header.get("ACK") should not be None
      header.get("ACK").get should equal ("JSON cannot be mapped to product model")
    }
  }

  test("POST /products/addProduct on ProductServlet with invalid stock value should give back a 200 " +
    "with error message in header") {
    submit("POST", "/products/addProduct", Seq.empty, Seq.empty, addProduct3Body) {
      status should equal (200)
      Model.products.size should equal (0)
      header.get("ACK") should not be None
      header.get("ACK").get should equal ("Parsing of product price and stock failed")
    }
  }

  test("POST /products/addProduct on ProductServlet should add product to model") {
    submit("POST", "/products/addProduct", Seq.empty, Seq.empty, addProduct1Body) {
      status should equal (200)
      val id = "book001"
      Model.products.size should equal (1)
      Model.products.get(id) should not be (None)
      Model.products.get(id).get.description should be ("The Da Vinci Code")
    }
  }

  test("POST /products/addProduct on ProductServlet with second product should add second product to model") {
    submit("POST", "/products/addProduct", Seq.empty, Seq.empty, addProduct2Body) {
      status should equal (200)
      val id = "book002"
      Model.products.size should equal (2)
      Model.products.get(id) should not be (None)
      Model.products.get(id).get.description should be ("Oil!")
    }
  }
}
