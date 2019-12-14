package cn.armylife.payments.Config;

public class AlipayConfig {
	// 商户appid
	public static String APPID = "2016101500690081";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOk98S/OmMaTvicn5YbcuP1ZzdhCbXnk8/tvJKgKq7zpH6o0v6pwl8V6jV6ybH/4kT2c0nHlJwRzJkRXSJt0L8onvpooLaXnkEOvJAxe8MLBh4z4hjyDzsUinSf7cKAcYOw4xRoR8W0fhYUBThfkC9vE1CMH289CKCkU4EdgW/XWksFBfyJu9PBhBYPc4J5gAnIzxuNoaCbJX7qyRNe2UuvP6gWmaV5tRwkaAMNellRwS7o25AGPT4oIMfR+RVni2YY5di4dDEmvNfeE02Mjiux+eYzlfBWtm1moqKOAhBj47FYvj48dO+2qSn6hLUthuqHDNPyPwHCimDNZ/JcTe7AgMBAAECggEBAIcvZ7R+PwcWBGTKQEeiGv+O/8P8+EM65FM6KHNBmiCmVT07NeA8Hr9Z8kiaRNQe0x8SRPWRR4MTzpDKVhv5ujcF7M1ye/WG2+GJ7oZB5k05Rh7OT7ikdDdyFBt1YtzHH76B5PrE9VBS19EMZTAE4GfGMc7PYXkWVd5M3ZdXPE3bWpCOZFCDWbddG8arZJ3WKI8KGGlGf/RDVESDTermKD7j2aMY34RZAL11i6H4eiJypUb+1nCvucfECotCjStn4EWTlIDoBOTLC73gJ/YU69xomrX58FaOQLqxXJMG+36Gokh1rMv9XQkF9NfbdJ2RYXTqvOPxQLjdfZlhXPCX75kCgYEA3WDao6qqqT0WQu/oUcMGD6m43+s/62sQ7tjOQYjir5/hK4TykWqTBwpF/rnEoGuOiqT/VQWt7RvGH9OmlZNxmUNdHli1/esspRsyy4AE8+yeZoVFomyXlwLUmfN7xQaITVVH1wVnHGdLblimsQqWnw2uyII3N3zbBTFryHE0200CgYEApOAi/l3wkP1fFfIac6QMivX5eAfAwDtwImJ4Wk+tKMazvr49Ca//xk2rlG/zwSNLXHKvXUj6DTdnHmtgeg25B1nRNSflwUVcjjGiTCpou+SySxWAJ4JbszbcmgjPbP62yNBj0S8iUPrgXD3JKpJi5AEcPMcojbvQgACAO0IViycCgYA+TV+65/EXqAV32SS8vfIavl01R3O5wWwGjCWf8/I4Evq/0K6jlibCuHVydr2LmDmJc14Yg3yrWkGr+1jKh7BUvcybGcQ+Lc1mufzVoLXEIDeECsXKLF2NNlK28PtIO8poWTPao76dE8T6zSA8ewpgwO0LqPn5N747++ilhJankQKBgG1T1sB3CCsMZ73EoTenQ03t1zN5aJ3PjAaw5v9zr+3G/K9xtEW1N9SF8k1mgurB/Znazl/8IRscbrtkFkA400WFkE1AHICw/rOnQ+hfgr1846EYK7eHIElRCqz1RJ4QtKGqVVYDfCzYfmeL7NXW5c4s0k2W/lQlgxctvdoNcv2nAoGACiTZfAMbGHJHztjsltergOB0O9YfpO/kHuzJ+G0Shj9kqXFld93t544sL5K8kLqHtwWjAfQ5rC1R/c+A11Yl/jopmNmJWXH7rF8zxrZOP/ELKoj3U2EQyiN9kiMi3kVq0/WWLMylHb0RB7HP3Qmwm1ZUlLQnJ+BEMXaas8+XH8g=";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://www.xuthus83.cn:6081/Pays/AlipayCallback";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://www.xuthus83.cn:6081/kdn/OneOffer-admin/wechat.html";
	// 请求网关地址
	public static String URL = "https://openapi.alipaydev.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjpPfEvzpjGk74nJ+WG3Lj9Wc3YQm155PP7bySoCqu86R+qNL+qcJfFeo1esmx/+JE9nNJx5ScEcyZEV0ibdC/KJ76aKC2l55BDryQMXvDCwYeM+IY8g87FIp0n+3CgHGDsOMUaEfFtH4WFAU4X5AvbxNQjB9vPQigpFOBHYFv11pLBQX8ibvTwYQWD3OCeYAJyM8bjaGgmyV+6skTXtlLrz+oFpmlebUcJGgDDXpZUcEu6NuQBj0+KCDH0fkVZ4tmGOXYuHQxJrzX3hNNjI4rsfnmM5XwVrZtZqKijgIQY+OxWL4+PHTvtqkp+oS1LYbqhwzT8j8BwopgzWfyXE3uwIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";

}
