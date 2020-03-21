package cn.armylife.payments.service;

import cn.armylife.payments.domain.RedEnvelopes;

import java.util.List;

public interface RedEnvelopesService {

    int insert(RedEnvelopes redEnvelopes);

    int update(RedEnvelopes redEnvelopes);

    RedEnvelopes selectRedForId(Long redId);

    List<RedEnvelopes> selectRedForUser(Long userId);

    int updateRedStatus(Long redId);
}
