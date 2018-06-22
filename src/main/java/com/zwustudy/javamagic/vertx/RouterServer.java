/**
 * 
 */
package com.zwustudy.javamagic.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * @author zwustudy
 * Route是Vert.x-Web的核心内容，它是一个维护线路（Route）的对象，Router获得一个HTTP请求然后找到与这个请求相匹配的路径，并将这个请求转交到这个路径上。路径上可以关联处理事件，路径接收的请求后，触发处理事件，处理完成后结束或者转到下一个程序处理。
 * Vert.x-Web版Hello World，我们先创建了一个HTTPServer，然后创建了一个Router。这次我们创建了一个简单的没有任何匹配规则的Router，这意味着它将匹配所有到达服务器的请求。 
 * 然后我们为Router创建处理程序。处理程序将响应所有到达服务器的请求。 
 * 这个Router将进入标准的RoutingContext处理程序（包含标准的HttpServerRequest和HttpServerResponse）而其他的东西则使Vert.x-Web工作更简单。 
 * 每一个请求都是一个路径，每一个路径都有相应的路径的上下文实例，这些请求将被相同的上下文实例的处理程序处理。 
 * 一旦我们设置了处理程序后，所有到达服务起的请求将被处理程序响应。
 */
public class RouterServer extends AbstractVerticle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();
		
		Router router = Router.router(vertx);
		router.route().handler(routeContext -> {
			routeContext.response().putHeader("Content-Type", "text/plain").end("Hello world from Vert.x-web!");
		});
		server.requestHandler(router::accept).listen(8090);
		System.out.println("RouterServer is listening at 8090 port.");
	}

}
