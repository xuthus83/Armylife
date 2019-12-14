package cn.armylife.payments.Utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NumberID {

    public static Long nextId(Integer port){
        int machineId = port;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        String num=machineId+ String.format("%010d", hashCodeV);
        Long number=Long.valueOf(num);
        return number;
    }
}
