package cn.armylife.common.domain;

import java.io.Serializable;
import java.util.List;

public class Alid implements Serializable {

    private final String frui="FRUI0101";
    private final String rest="REST0202";
    private final String book="BOOK0303";
    private final String shot="SHOT0404";
    private final String hair="HAIR0505";
    private final String mtea="MTEA0606";

    private List<String> appId;

    public List<String> getAppId(){
        appId.add(1,frui);
        appId.add(2,rest);
        appId.add(3,shot);
        appId.add(4,book);
        appId.add(5,hair);
        appId.add(6,mtea);
        return appId;
    }
}