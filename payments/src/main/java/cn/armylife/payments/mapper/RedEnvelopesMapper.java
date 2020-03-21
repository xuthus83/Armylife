package cn.armylife.payments.mapper;

import cn.armylife.payments.domain.RedEnvelopes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RedEnvelopesMapper {

    int insert(RedEnvelopes redEnvelopes);

    int update(RedEnvelopes redEnvelopes);

    RedEnvelopes selectRedForId(Long redId);

    List<RedEnvelopes> selectRedForUser(Long userId);

    int updateRedStatus(Long redId);

}
