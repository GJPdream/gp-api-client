package com.example.client;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.example.model.User;
import com.example.utils.SignUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Data
public class SdkClient {
    private String accessKey;
    private String secretKey;

    public SdkClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
//    /**
//     * @param name
//     * @descr
//     * 1、客户端发来的 ak ，sk .这边进行加密处理
//     * 2、发送到请求接口，加密之后的ak和sk,从数据库中拿出来，进行同样方式加密
//     * 3、进行对比。如果相同则通过，反之无权限。
//     * 4、防止重放，使用redis中间件，缓存数据。随机数+时间戳。进行防止重放。
//     */


    public  String getUserNameByGet(String name){

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        System.out.println("sdk中get的"+name);
        String result3= HttpUtil.get("http://localhost:8888/api/name/", paramMap);
        System.out.println("sdk中get的result ===>"+result3);
        return  result3;
    }

    public String getUserNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        System.out.println("sdk中post的"+name);
        String result= HttpUtil.post("http://localhost:8888/api/name/", paramMap);
        System.out.println("sdk中post的result ===>"+result);
        return result;
    }

    public String getUserNameByPost(@RequestBody User user) throws UnsupportedEncodingException {
        String json = JSONUtil.toJsonStr(user);
        String encode = URLEncoder.encode(json, "UTF-8");
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8888/api/name/user")
                //加密之后的ak 和sk 写进去   map
                .addHeaders(getHeader(encode))
                .body(json)
                .execute();
        System.out.println("这个是client中的 Status=====》"+httpResponse.getStatus());
        return "post 用户的名字为"+user.getUsername();
    }

    private Map<String,String> getHeader (String json) throws UnsupportedEncodingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("body", json);
        System.out.println("client JSON----->"+json);
        String s = RandomUtil.randomNumbers(4);
        map.put("nonce", s);
        String value = String.valueOf(System.currentTimeMillis() / 1000);
        map.put("timestramp",value );

        //这个地方为什么，需要传secreteKey,调用接口时，需要请求头进行传送。这个可能被拦截，所以secretKey不能放到头中。
        String sign = SignUtil.getSign(map,secretKey);
        map.put("sign",sign);
        return map;
    }
}
