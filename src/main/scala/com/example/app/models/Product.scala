package com.example.app.models

import com.mongodb._
import com.mongodb.casbah.MongoCollection
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
  private def toProduct(data: DBObject): Product = {
    new Product(
      data.get("name").toString,
      data.get("price").toString.toInt,
      data.get("_id").toString
    )
  }

  def save(collection: MongoCollection, product: Product): WriteResult = {
    collection.insert(product.toMongo)
  }

  def find(collection: MongoCollection, id: String): Option[Product] = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    collection.findOne(query).map(toProduct)
  }

  def all(collection: MongoCollection, limit: Int = 10): List[Product] = {
    collection.find().limit(limit).map(toProduct).toList
  }
}
