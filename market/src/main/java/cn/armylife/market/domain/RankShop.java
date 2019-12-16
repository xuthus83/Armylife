package cn.armylife.market.domain;

import cn.armylife.common.domain.Product;

import java.util.List;

public class RankShop {

    private String rankDay;
    private String rankMonth;
    private List<Product> product;

    public String getRankDay() {
        return rankDay;
    }

    public void setRankDay(String rankDay) {
        this.rankDay = rankDay;
    }

    public String getRankMonth() {
        return rankMonth;
    }

    public void setRankMonth(String rankMonth) {
        this.rankMonth = rankMonth;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "RankShop{" +
                "rankDay='" + rankDay + '\'' +
                ", rankMonth='" + rankMonth + '\'' +
                ", product=" + product +
                '}';
    }
}
