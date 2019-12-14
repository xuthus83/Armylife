package cn.armylife.payservice.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public interface WechatService {

    String getOpenId(String code);

    Map<Integer,String> getUserInfo(String code);
}
