package cn.armylife.integralmall.FeignHystrix;

import cn.armylife.common.Domain.Alipay;
import cn.armylife.integralmall.Feign.payservice;

import java.util.Map;

public class payserviceHystrix implements payservice {

    @Override
    public Map<String,String> order(String orderDesc, String Total,String orderCardID,String attach){
        return null;
    };

    @Override
    public Alipay alipay(String WIDout_trade_no, String WIDsubject, String WIDtotal_amount, String WIDbody,String passback_params){
        return null;
    }
}
