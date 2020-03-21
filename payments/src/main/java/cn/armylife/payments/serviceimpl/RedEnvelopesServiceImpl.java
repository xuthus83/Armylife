package cn.armylife.payments.serviceimpl;

import cn.armylife.payments.domain.RedEnvelopes;
import cn.armylife.payments.mapper.RedEnvelopesMapper;
import cn.armylife.payments.service.RedEnvelopesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RedEnvelopesServiceImpl implements RedEnvelopesService {

    @Autowired
    private RedEnvelopesMapper redEnvelopesMapper;

    public int insert(RedEnvelopes redEnvelopes){
        redEnvelopes.setStatus(0);
        SimpleDateFormat simpleFormatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        String time=simpleFormatter.format(date);
        redEnvelopes.setCreatTime(time);
        return redEnvelopesMapper.insert(redEnvelopes);
    };


    public int update(RedEnvelopes redEnvelopes){
        return redEnvelopesMapper.update(redEnvelopes);
    };

    public RedEnvelopes selectRedForId(Long redId){
        return redEnvelopesMapper.selectRedForId(redId);
    };

    public List<RedEnvelopes> selectRedForUser(Long userId){
        return redEnvelopesMapper.selectRedForUser(userId);
    };

    public int updateRedStatus(Long redId){
        return redEnvelopesMapper.updateRedStatus(redId);
    };
}
