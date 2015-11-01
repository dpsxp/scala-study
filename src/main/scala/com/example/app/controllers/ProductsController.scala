package com.example.app.controllers

import com.example.app.models.Product
import org.scalatra._
import org.json4s.DefaultFormats
import org.scalatra.json._

class ProductsController extends ScalatraServlet with JacksonJsonSupport {

  protected implicit lazy val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Product.all
  }

  get("/:id") {
    implicit val id = params("id").toLong

    Product.find getOrElse halt(404, body = Map("error" -> "Can't find product with ".concat(id.toString)))
  }
}
