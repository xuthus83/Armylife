package cn.armylife.payments.ServiceImpl;

import cn.armylife.payments.Service.TransactionsService;
import cn.armylife.common.Domain.Transactions;
import org.springframework.stereotype.Service;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Override
    public int insert(Transactions transactions){
        return insert(transactions);
    };
}
