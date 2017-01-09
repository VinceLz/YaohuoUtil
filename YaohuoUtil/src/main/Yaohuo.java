package main;

/**
 * 签到
 * 
 * @author kernel
 * 
 */
public class Yaohuo {
	public Data data = new Data();

	/*
	 * 登陆功能
	 */
	public boolean login(String uname, String password) {
		String loginReturn = HttpRequest
				.login("http://yaohuo.me/waplogin.aspx",
						"action=login&savesid=0&classid=0&siteid=1000&sid=-3-0-0-0-0&backurl=wapindex.aspx?sid=-2&logpass="
								+ password + "&logname=" + uname, data);
		
		if (loginReturn.contains("密码错误")) {
			return false;
		}
		return true;
	}

	/*
	 * 签到
	 */
	public boolean singIn() {

		String qiandao = "http://yaohuo.me/signin/Signin.aspx?Action=index&Mod=Signin&siteid=1000";
		String sendPost = HttpRequest.sendPost(qiandao,
				"content=哈哈，今天心情还不错哦&FaceSelect=2.gif", data);
		if (sendPost.contains("一天只能签到一次哦")) {
			System.out.println(sendPost);
			return false;
		}
		
		return true;

	}

	public static void main(String[] args) throws InterruptedException {
		Yaohuo yao = new Yaohuo();
		yao.login("18292882168", "2168");
		Thread.sleep(3000);
		System.out.println(yao.data.sidyaohuo);
		Thread.sleep(3000);
		yao.singIn();
		
	}
}
