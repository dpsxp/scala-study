package com.example.app.tables

import com.example.app.models.MongoDocument
import com.mongodb._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId
import org.scalatra.Params

abstract class DBTable[T <: MongoDocument](host: String = "localhost", databaseName: String, collectionName: String) {
  private lazy val db = MongoClient(host)
  private lazy val collection = db(databaseName)(collectionName)

  def delete(id: String): WriteResult = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    collection.remove(query)
  }

  def save(item: T): T = {
    this.collection.insert(toMongo(item))
    toModel(this.collection.last)
  }

  def find(id: String): Option[T] = {
    try {
      val query = MongoDBObject("_id" -> new ObjectId(id))
      this.collection.findOne(query).map(toModel)
    } catch {
      case e: IllegalArgumentException => {
        // TODO perform some log here
        None
      }
      case _: Throwable => None
    }
  }

  def update(item: T): WriteResult = {
    val query = MongoDBObject("_id" -> new ObjectId(item.id))
    val update = toMongo(item)
    this.collection.update(query, update, upsert = false)
  }

  def all(limit: Int = 10): List[T] = {
    this.collection.find().limit(limit).map(toModel).toList
  }

  def toMongo(item: T): DBObject = ???
  def toModel(obj: DBObject): T = ???
  def fromRequest(params: Params): T = ???
}
