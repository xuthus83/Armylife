package cn.armylife.union.controler;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taobao.api.TaobaoClient;
@Controller
public class TaobaoController {

    @RequestMapping
    @ResponseBody
    public int tb(){

        JdClient jdClient=new DefaultJdClient("jd.union.open.order.query",);
        return 1;
    }
}
