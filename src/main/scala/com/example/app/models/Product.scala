package com.example.app.models

import javax.servlet.http.HttpServletRequest

import org.scalatra.Params
import com.mongodb._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

case class Product(name: String, price: Int = 30) {
  var id = ""

  def this(name: String, price: Int, id: String) {
    this(name, price)
    this.id = id
  }

  def save: Boolean = {
    if (this.valid) {
      Product.save(this).getField("ok") match {
        case x: Object => x == 1
        case _ => false
      }
    } else {
      false
    }
  }

  def toMongo = {
    id match {
      case "" => MongoDBObject("name" -> name, "price" -> price)
      case _ => MongoDBObject("name" -> name, "price" -> price, "_id" -> id)
    }
  }

  def delete {
    Product.delete(this.id)
  }

  private def valid: Boolean = {
    !this.name.isEmpty
  }
}

object Product {
  private val db = MongoClient("localhost")
  private val collection = db("pismo")("products")

  def delete(id: String): WriteResult = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    collection.remove(query)
  }

  def fromRequest(data: Params): Product = {
    val name = data.get("product[name]") match {
      case Some(name) => name
      case _ => ""
    }

    data.get("product[price]") match {
      case Some(price) => Product(name, price.toInt)
      case _ => Product(name)
    }
  }

  def save(product: Product): WriteResult = {
    this.collection.insert(product.toMongo)
  }

  def find(implicit id: String): Option[Product] = {
    try {
      val query = MongoDBObject("_id" -> new ObjectId(id))
      this.collection.findOne(query).map(toProduct)
    } catch {
      case e: IllegalArgumentException => {
        // TODO perform some log here
        None
      }
      case _: Throwable => None
    }
  }

  def all(limit: Int = 10): List[Product] = {
    this.collection.find().limit(limit).map(toProduct).toList
  }

  private def toProduct(data: DBObject): Product = {
    new Product(
      data.get("name").toString,
      data.get("price").toString.toInt,
      data.get("_id").toString
    )
  }
}
