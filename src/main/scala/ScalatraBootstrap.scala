import com.pbrandwijk.app._
import com.pbrandwijk.app.servlets.ProductServlet
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new ProductServlet, "/*")
  }
}
