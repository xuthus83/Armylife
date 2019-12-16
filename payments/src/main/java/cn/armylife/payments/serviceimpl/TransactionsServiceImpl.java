package cn.armylife.payments.serviceimpl;

import cn.armylife.payments.service.TransactionsService;
import cn.armylife.common.domain.Transactions;
import org.springframework.stereotype.Service;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Override
    public int insert(Transactions transactions){
        return insert(transactions);
    };
}
