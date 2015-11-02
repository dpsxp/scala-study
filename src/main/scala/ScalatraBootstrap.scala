import com.example.app.controllers.ProductsController
import com.example.app.dbs.ProductTable
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val collection = new ProductTable()
    context.mount(new ProductsController(collection), "/products")
  }
}
