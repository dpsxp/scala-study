package com.example.app.controllers

import com.example.app.tables.ProductTable
import com.example.app.models.Product
import org.scalatra._
import org.json4s.DefaultFormats
import org.scalatra.json._

class ProductsController(collection: ProductTable) extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    collection.all(10)
  }

  get("/:id") {
    val id = params("id")
    collection.find(id).getOrElse(notFound(id))
  }

  def notFound(id: String) {
    halt(404, body = Map("error" -> "Can't find product with id ".concat(id)))
  }

  post("/") {
    val product = collection.fromRequest(params)

    product.save match {
      case true => halt(201, body = Map("success" -> "Product created with success"))
      case _ => halt(422, body = Map("error" -> "Invalid data"))
    }
  }

  delete("/:id") {
    val id = params("id")

    collection.find(id) match {
      case Some(product) => destroy(product)
      case _ => notFound(id)
    }
  }

  put("/:id") {
    val id = params("id")

    collection.find(id) match {
      case Some(product) => update(product)
      case _ => notFound(id)
    }
  }

  private def update(product: Product) {
    if (product.update(params)) {
      val message = "Product with id " + product.id + " updated"
      halt(200, body = Map("success" -> message))
    } else {
      halt(422, body = Map("error" -> "Invalid data"))
    }
  }

  private def destroy(product: Product) {
    product.delete
    val message = "Product with id " + product.id + " removed"
    halt(200, body = Map("success" -> message))
  }
}
