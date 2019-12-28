package cn.armylife.payments.serviceimpl;

import cn.armylife.payments.mapper.TransactionsMapper;
import cn.armylife.payments.service.TransactionsService;
import cn.armylife.common.domain.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    @Autowired
    TransactionsMapper transactionsMapper;

    @Override
    public int insert(Transactions transactions){
        return  transactionsMapper.insert(transactions);
    };
}
