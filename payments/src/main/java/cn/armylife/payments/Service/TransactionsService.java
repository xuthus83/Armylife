package cn.armylife.payments.Service;

import cn.armylife.common.Domain.Transactions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TransactionsService {


    int insert(Transactions transactions);
}
