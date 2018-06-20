import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * 
 */

/**
 * @author zwustudy
 *
 */
public class ProxyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
			URL url = new URL("http://httpbin.org/ip");
			HttpURLConnection con = null;
			StringBuffer buffer = new StringBuffer();
			if (args.length > 0) {
				System.out.println("测试代理ip：" + args[0]);
				String[] parts = args[0].split(":");
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));
				con = (HttpURLConnection) url.openConnection(proxy);
			} else {
				System.out.println("测试本地ip：null");
				con = (HttpURLConnection) url.openConnection();
			}
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			/*osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(param);
			osw.flush();
			osw.close();*/
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
			System.out.println(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
