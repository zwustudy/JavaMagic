/**
 * 
 */
package com.zwustudy.javamagic.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author zwustudy
 * 没有任何操作的HTTP服务，给请求返回Hello Vertx!，浏览器输入 http://localhost:8090/ 测试
 */
public class SimpleWebServer extends AbstractVerticle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vertx.vertx().createHttpServer().requestHandler(request -> {
			
			request.response().putHeader("Content-Type", "text/plain").end("Hello Vertx!");
		}).listen(8090);
		System.out.println("SimpleWebServer is listening at 8090 port.");
	}

}
