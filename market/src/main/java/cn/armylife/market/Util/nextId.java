package cn.armylife.market.Util;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class nextId extends Thread {

    @Value("${server.port}")
    String port;

    public  void  run(){
        int machineId = 10001;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        String num=machineId+ String.format("%010d", hashCodeV);

        System.out.println(num);
    }
}
