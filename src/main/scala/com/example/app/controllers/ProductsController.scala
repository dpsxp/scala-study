package com.example.app.controllers

import com.example.app.models.Product
import com.mongodb.casbah.MongoDB
import org.scalatra._
import org.json4s.DefaultFormats
import org.scalatra.json._

class ProductsController extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Product.all()
  }

  get("/:id") {
    implicit val id = params("id")

    Product.find getOrElse halt(404, body = Map("error" -> "Can't find product with ".concat(id)))
  }

  post("/") {
    val product = Product.fromRequest(params)

    product.save match {
      case true => halt(201, body = Map("success" -> "Product created with success"))
      case _ => halt(422, body = Map("error" -> "Invalid data"))
    }
  }
}
