package com.example.app.controllers

import com.example.app.models.Product
import com.mongodb.casbah.MongoDB
import org.scalatra._
import org.json4s.DefaultFormats
import org.scalatra.json._

class ProductsController(mongo: MongoDB) extends ScalatraServlet with JacksonJsonSupport {
  protected val collection = mongo("products")
  protected implicit lazy val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Product.all(collection)
  }

  get("/:id") {
    val id = params("id")

    Product.find(collection, id) getOrElse halt(404, body = Map("error" -> "Can't find product with ".concat(id.toString)))
  }
}
