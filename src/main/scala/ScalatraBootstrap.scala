import com.pbrandwijk.app.servlets.{ProductServlet, UserServlet}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new ProductServlet, "/products/*")
    context.mount(new UserServlet, "/users/*")
  }
}
