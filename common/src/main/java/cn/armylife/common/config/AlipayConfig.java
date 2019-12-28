package cn.armylife.common.config;

public class AlipayConfig {
	// 商户appid
	public static String APPID = "2019121669949579";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCieARr+bJd+JnbPQc+kVoLJmsS//EXycQtK2fbX1jCmbwZWweaFWpEhaIWn1buKFhsooixq+VBP+Ymk/24Cuxx7oDkPsoLayFFWHeOzHktlB0i8pCM8PElhY0KY7cBf3p9Eeo69YyhyvC/kKsCYAFd5DfOq4Cz3qehWgFK15zw0e71THJEAojurZpfLO2I+Imch6fywYmsrY1cqkKBz+Cfo7gqJhps+0kQQMX85Z5MpG5TfR0Njg3HbdZwVxIcKa+HQw72QgmYN7ZowmVI841puZgYzlcmuEBOJo93wsIi7tRovnM68t/Vr82ylcSIG2DTC/Dq9aUGbFn9BjEspKkDAgMBAAECggEBAJLwCM++3NqwZDYsU8j6nH1JGb3tTJc/G/XeM+frq9MUR1KHSbDJfXrLgXUEVxK92nsl5IsL9CO3LuflIKq69m10T44eW41ILqcRA9BajZ3zPwbEaoeNwgs1ZgHnfTQVnypOcZExUydg66vVo1Raj+Hi7bY01gnXbbQ4OsL9jisdwKbCGZL0lAdz+K6e94dpFVEDGyhiVQC9+CstNXkSTjZ3HGjb2d8p75Ip8YTHa3zuhMyHFxjulMtd/QOAp2MWUpYSD1qMNz8my0kuYyOpSD0Pxpp1Phx2QyAZ6Dexs9qbrd5+cFgf5Jzv3Abn5UbB0sZSryK0BBkbFvHon6N/YQECgYEA3xUJZj7DJlrYltS+ccZKc47ELrK4aqprUF+8FwSkF4iteKy92F+ROew7rViU113jQ6vamlKRFWFGiyIs/rbWt9ozRPMshFBsNMQeyEbn4GjfRthca7wwcYHj7uCR2xRZeC2Zbmh6YWYbJkG16Dju2E1hF0YXJrRldAe+MkvcQJECgYEAunFNPRA1cDVUr1wHa83fVnNhf8P32ys5qk6xbHSIqnkInhhOKLZcI9HqnPL82QT2k9HXXEX3+FJO7J/12rai3PKqQA7rkOj9vD6QR56nP8owyWGiJwvoG4vVIPsksK/pUiq2qbaV8l+ZG3BxYMFOE8SzxpUQomfeIELjXzFdGlMCgYEAqAYeSEapkjAGxsSClQt3dEZp+YpfYOfvYGBbOTMjrR4jGqtxapHAvtHxuQTpRrXOwV+ljWr3IBVentkk7s0kOOI+j4ZhV1+DwNKfJNZTPDGBSskYA4WBL11YIso+0bYoFV6zG/fRhZ70gAIqUzjenUjbAj6mnV5us9BJL3T39iECgYBLJvqcX5k6xrSBDxaKtOyOm2m0p5iUBsNyy8+myP6BwNHhcCipLoDLi4UHiMa079Q413/42NdQiFT5+qyOYcoaR9KqiZkYy2Hj7FAaHnCKPQUXV7yWAq2n1OCvL6ylb/MbxM3mcQvWLAS6U8PP3XuMamCQ7mtLG5Uk6fFe4LYHswKBgQCbwFtSfmC1h6y7Dz4CQ4jx2sy+YYp2v9nvoKEEIY4DDJ6DGuAsHO8DlqmU6U3W4J19tN6f1RHu6Tr90AG/52bOReCmh5l5vC28M/GTc8Ba8A0xsWY4qr06xFbltXt/Ok1EeHW2JjEELvO4jo+PfG29XjVWh49yvHUMFh+oY3TluA==";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "https://www.armylife.cn/api/PayMents/AlipayCallback";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "https://www.armylife.cn/Students/index.html";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "";
	public static String app_cert_path="/home/armylife/jar/payservice//appCertPublicKey_2019121669949579.crt";
	public static String alipay_cert_path="/home/armylife/jar/payservice//alipayCertPublicKey_RSA2.crt";
	public static String alipay_root_cert_path ="/home/armylife/jar/payservice//alipayRootCert.crt";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";

}
