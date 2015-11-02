package com.example.app.models

import com.mongodb._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

case class Product(name: String) {
  var price = 30
  protected var id = ""

  def this(name: String, price: Int, id: String) {
    this(name)
    this.id = id
    this.price = price
  }

  def toMongo = {
    id match {
      case "" => MongoDBObject("name" -> name, "price" -> price)
      case _ => MongoDBObject("name" -> name, "price" -> price, "_id" -> id)
    }
  }
}

object Product {
  private val mongo = MongoClient("localhost")
  private val collection = mongo("pismo")("products")

  def save(product: Product): WriteResult = {
    this.collection.insert(product.toMongo)
  }

  def find(implicit id: String): Option[Product] = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    this.collection.findOne(query).map(toProduct)
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
