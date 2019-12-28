package cn.armylife.member.util;

import cn.armylife.common.domain.Alid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IsAlid {

    public static boolean isAlid(String code){
        Alid alid=new Alid();
        List<String> alids=alid.getAppId();
        for (int i=0;i<alids.size();i++){
            if (code.equals(alids.get(i))){
                return true;
            }
            return false;
        }
        return false;
    }
}
