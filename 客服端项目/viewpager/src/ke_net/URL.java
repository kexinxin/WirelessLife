package ke_net;

/**
 * 访问网络的地址
 * 
 * 
 */
public class URL {

	// 网络
	public final static String HOST = "http://www.fs365.com.cn";


	public final static String URL_SPLITTER = "/";

	public final static String URL_HOST = HOST + URL_SPLITTER;

	public final static String UPDATE_VERSOIN = URL_HOST + "";

	public final static String LOGIN_URL = URL_HOST + "fortel/login";

	public final static int PC_PORT = 8624;

	public final static int PC_DOWN_PORT = 8625;

	public final static int PC_UP_PORT = 8626;


	// 获取盘符
	public static final String GET_DISK = "getDisk";
}
