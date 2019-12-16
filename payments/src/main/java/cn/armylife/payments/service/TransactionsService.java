package cn.armylife.payments.service;

import cn.armylife.common.domain.Transactions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TransactionsService {


    int insert(Transactions transactions);
}
