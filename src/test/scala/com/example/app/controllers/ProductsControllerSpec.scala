import com.example.app.controllers.ProductsController
import com.example.app.models.Product
import com.example.app.tables.DBTable
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FunSpecLike}
import org.scalatra.test.scalatest._

class FakeTable extends DBTable[Product](databaseName = "pismo_test", collectionName = "products")

class ProductsControllerSpec extends ScalatraSuite with FunSpecLike with MockFactory with BeforeAndAfter {
  val tableMock = mock[FakeTable]
  addServlet(new ProductsController(tableMock), "/products")

  describe("GET /products on ProductsController") {
    it("returns status 200") {
      (tableMock.all _).expects(10).returning(List(
        Product("test")
      ))

      get("/products") {
        status should equal (200)
      }
    }

    it("returns a list of products in json format") {
      (tableMock.all _).expects(10).returning(List(
        Product("test")
      ))

      get("/products") {
        body should include("[{\"name\":\"test\",\"price\":30,\"id\":\"\"}]")
      }
    }
  }

  describe("GET /products/:id on ProductsController") {
    describe("when the id is valid") {
      val id = "50235829385023"
      val product = new Product("teste", 30, id)

      it("returns status 200") {
        (tableMock.find _).expects(id).returning(Some(product))

        get("/products/" + id) {
          status should equal(200)
        }
      }

      it("returns a Product instance in json format") {
        (tableMock.find _).expects(id).returning(Some(product))

        get("/products/" + id) {
          body should include("{\"name\":\"teste\",\"price\":30,\"id\":\"" + id + "\"}")
        }
      }
    }

    describe("when the id is invalid") {
      implicit val id = "5203502352039"

      it("returns status 404 when the id is invalid") {
        (tableMock.find _).expects(id).returning(None)

        get("/products/" + id) {
          status should equal(404)
        }
      }

      it("returns a json with a error message when the id is invalid") {
        (tableMock.find _).expects(id).returning(None)

        get("/products/" + id) {
          body should include("\"error\":\"Can't find product with id " + id + "\"")
        }
      }
    }
  }

  describe("DELETE /products/:id on ProductsController") {
    describe("when the id is valid") {
      val id = "35235023j5230j523"
      val product = mock[Product]

      it("returns a 200 status") {
        tableMock.find _ expects id returning Some(product)
        product.delete _ expects

        delete("/products/" + id) {
          status should equal(200)
        }
      }

      it( "returns a success message in json format") {
        tableMock.find _ expects id returning Some(product)
        product.delete _ expects

        delete("/products/" + id) {
          body should include ("{\"success\":\"Product with id " + product.id + " removed\"")
        }
      }
    }

    describe("when the id is invalid") {
      val id = "1204o1204o12"

      it("returns a 404 status") {
        tableMock.find _ expects id returning None

        delete("/products/" + id) {
          status should equal (404)
        }
      }
      it("returns a error message in json format") {
        tableMock.find _ expects id returning None

        delete("/products/" + id) {
          body should include("\"error\":\"Can't find product with id " + id + "\"")
        }
      }
    }
  }

  describe("PUT /products/:id on ProductsController") {
    describe("when the id is valid") {
      val id = "1204i120i4102i4"
      val product = mock[Product]
      val params = Map("product[name]" -> "update", "id" -> id)

      it("returns a 200 status") {
        tableMock.find _ expects id returning Some(product)
        product.update _ expects params returning true

        put("/products/" + id, params) {
          status should equal(200)
        }
      }

      it("returns a success message in json format") {
        tableMock.find _ expects id returning Some(product)
        product.update _ expects params returning true

        put("/products/" + id, params) {
          body should include("\"success\":\"Product with id " + product.id + " updated\"")
        }
      }
    }

    describe("when the id is invalid") {
      val id = "1240124102j4"

      it("returns a 404 status") {
        tableMock.find _ expects id returning None

        put("/products/" + id) {
          status should equal(404)
        }
      }

      it("returns a error message in json format") {
        tableMock.find _ expects id returning None

        put("/products/" + id) {
          body should include("\"error\":\"Can't find product with id " + id + "\"")
        }

      }
    }
  }

  describe("POST /products on ProductsController") {
    describe("when the data is valid") {
      val params = Map("product[name]" -> "test", "product[price]" -> "50")
      val product = mock[Product]

      it("returns a 201 status") {
        tableMock.fromRequest _ expects * returning product
        product.save _ expects() returning true

        post("/products") {
          status should equal(201)
        }
      }

      it("returns a success message in json format") {
        tableMock.fromRequest _ expects * returning product
        product.save _ expects() returning true

        post("/products", params = params) {
          body should include("\"success\":\"Product created with success\"")
        }
      }
    }

    describe("when the data is invalid") {
      val product = mock[Product]

      it("returns a 422 status") {
        tableMock.fromRequest _ expects * returning product
        product.save _ expects() returning false

        post("/products") {
          status should equal(422)
        }
      }

      it("returns a error message in json format") {
        tableMock.fromRequest _ expects * returning product
        product.save _ expects() returning false

        post("/products") {
          body should include ("\"error\":\"Invalid data\"")

        }
      }
    }
  }
}