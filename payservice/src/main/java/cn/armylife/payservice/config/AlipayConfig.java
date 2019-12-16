package cn.armylife.payservice.config;

public class AlipayConfig {
	// 商户appid
	public static String APPID = "2016101500690081";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDBjmxX8LKYrn8luEokB1byR/e74mUiQ+VMJA35/VXctbfI0ptEZCZdydqWUaG5xmOvxIzLPH4xiQHVSb5FzJA+fIhpa4eQ7zMJffohT8vqf7Fk4Yf3vtZWyaeDCaTcN6X95R2Zb2eZNPjyUjqhzmZGX5gQECrCckzU6UkfL3XiRYZcR36ONwkF7HKwzCByXAn+s1uFCZLQSsuQjv33ydfWLJGJYItyj8Q292SIeSzPpl+LQxcl/mqA0bszTHa6+nDZGQPws45vBWQDd/Dh2v6A4eP2RmTNraqOTcAZP4CUiKpsOk1MYK/9Rl+okuU5qpaTP29IdZONcRWVAKiQWo8lAgMBAAECggEBAKvDZeYMVZEkmPSyPCkI0mGi9/clizYDJoKRJE4KQJ3iLLvsGZlxfpKQRIhM3MXthh0+wOrCPgJbYOMT0+RBCmhJPwy+PcmiW5o6zzOsinsI4jVx5qU2FyNhRGQGtOQwBf/vyHqW4EqXNaxkyF86uIwO3PS0RB3WLNRsSVWBWo12NEyXH9jOrnG4PdB5pY6c4T9/IPLpUryCOT/512mC9WpKDrd8Yr/ALZPAi9B7fj/8l4noRFgp5zzlWw1QrM9uViN7kIrKqluUB4hH/Jv70lRUeMsVY/+0bSJMvp/Tr0JyMA85cRMQ3I1s87hC9euXUowqLHjoVaeNMyuszds1U+kCgYEA9NgByAyBanAZXS/rTAlk0FlY4OcB9TaDE89hMCf31HFDfD+9e4dLVomH8Bn9Nz+FTkN284u3M31FU1Yu4hB+w+ZL6z/vs8876+67tZiyb5qR72F4wrKX0f5vtiy1m53Is+xQLloxlLJtM/3ZWm3Yfy8wzlAX6Uw7kPYwk+YoghMCgYEAymAr2JOxYSJjgBgk5igtloo10945dFJDpa4Pvgng5WUSPo+aaD62a6KmgXKUuHWgaem20w58Ewnv8IKkYYZ9KlAD2JA/ftz2Jrm8CvRBK/SNk1+y+J0u4TQ0amixy909TC3ONk797CyZ8CPwSD0OUzRtLTu1u9zIV2R0LpUkEOcCgYANmFzo9cDN3zGZWgrPqwndA8kKm+kg64j1taMo4F9DfZKIuKVV0ZpeUL6ql1bXFYDaaqaVOWHPyxnpWbS6ur2/NO9sPZ2at1wVaqkjmTn3Nnb5+siQzwfdEiqzy+G9pVI6o1mEaV+taQblRyglfEn0LfOg0ROy0ZfRIcpqu/Ht3wKBgBb5dADSQ4DTdphEJWw7g3Fjn1i+Eaey7xsP95mLZYylPLz9cHoFJJBzlpN7foayU9OyRJ8RNeSzbBnRuveXXP5Aty8OsSalzwv2afKUaBGlYa0pYBf5QCkw1AKAgC60nxDW4mIn9YuqRI+OhOiuhcHMYdiQvsDk/5lsPgRLyhxhAoGBAPB9zB08YgIWnQ+BwE3cyt0GUr/KFoaBAOlZP1lde779lx7v7fjXFLmGHfh9YkMo0+vfzYQBsK4TdOpyW+ZGvODMaz5G+xe64Dk8K2GQ12RWpIGzeRV7upqNAztPbppnSQxWCddYHYiZsx03RLkJQxShT0z4n9P/BGofidk2ybhZ";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://api.xuthus83.cn:6081/PayMents/AlipayCallback";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://api.xuthus83.cn:6081/hua/index.html";
	// 请求网关地址
	public static String URL = "https://openapi.alipaydev.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgProLYvUy3CvRTvKcoSw8CYyAToXtWwExKDoROX/PlfYODo/2LEDmnHcmGUrOLggI20ribN+fcXwrr/XobVrAZEKuG9L99D19mUdjCiMB+9TwtZ2chEHceeYuwxjsQ8xsXh7D9DU6xy2V/CITiEPR6Ln8RktYMr2am16pee3lzlc8ljBBbr0roEs61785t/VDsq5O1mdpUw2NcJ7uei0cmmpp4IyCVcGKp9EzRITIoMrhXP9iBYrg7PmGOBxYSLBEhw6QOjEUgK0Qwcf9GKIak6+yLKKsVnXLjKRUPWWQpSF8C6QAqLIs7cG1Yq7bFTMF3RJ961IwRPiIsaPYATckwIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";

}
