/**
 * 
 */
package com.zwustudy.javamagic.vertx;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

/**
 * @author zwustudy
 * 一个使用异步请求的服务器资源的HttpClient的例子
 */
public class VertxHttpClient {
	
	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		for (int i = 0; i < 1000; i++) {
			final int index = i;
			HttpClient httpClient = vertx.createHttpClient();
			
			//httpClient.get(80, "www.baidu.com", "/", response -> {
			httpClient.get(8090, "127.0.0.1", "/zwustudy/hello/vertx", response -> {
				String contentType = response.getHeader("Content-Type");
				int statusCode = response.statusCode();
				response.bodyHandler(buffer -> {
					String content = new String(buffer.getBytes());
					
					System.out.println("第" + index + "次请求的content:" + content);
					httpClient.close();
				});
				System.out.println("第" + index + "次请求的ContentType:" + contentType);
				System.out.println("第" + index + "次请求的statusCode:" + statusCode);
			}).end();
		}
	}
}
