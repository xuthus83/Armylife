package cn.armylife.payments.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class RedEnvelopes implements Serializable {

    private Long redId;
    private BigDecimal amountNumber;
    private Long userId;
    private int status;
    private String creatTime;
    private String endTime;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getRedId() {
        return redId;
    }

    public void setRedId(Long redId) {
        this.redId = redId;
    }

    public BigDecimal getAmountNumber() {
        return amountNumber;
    }

    public void setAmountNumber(BigDecimal amountNumber) {
        this.amountNumber = amountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
