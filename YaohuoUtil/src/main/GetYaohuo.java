package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class GetYaohuo {
	private String sidyaohuo="B75FA2A817F74F4B0_9_42280_16_900100-3-0-0-0-0";
	private String ASP_SessionId="";
	private String yunsuo_session_verify="";
	
	public  String sendGet(String url) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url;
	            URL realUrl = new URL(urlNameString);
	            URLConnection connection = realUrl.openConnection();
	            connection.setRequestProperty("Cookie", "sidyaohuo="+sidyaohuo);
	            connection.connect();
	            String headerField = connection.getHeaderField("Set-Cookie");
	            Map<String, List<String>> headerFields = connection.getHeaderFields();
	            List<String> list = headerFields.get("Set-Cookie");
	            System.out.println(list);
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	  public static void main(String[] args) {
		  GetYaohuo g=new GetYaohuo();
	 String  a=g.sendGet("http://yaohuo.me/signin/Signin.aspx?Action=index&Mod=Signin&siteid=1000&content=哈哈，今天心情还不错哦&FaceSelect=2.gif");
	 System.out.println(a);
	 	if(a.contains("登陆")){
	 		//则取模拟登陆
	 			System.out.println("要登陆");
	 	}
	}
}
