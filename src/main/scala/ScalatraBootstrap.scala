import com.example.app.controllers._
import com.example.app.tables.ProductTable
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val collection = new ProductTable(collectionName = "pismo")
    context.mount(new RootController, "/*")
    context.mount(new ProductsController(collection), "/products")
  }
}
