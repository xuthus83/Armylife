package cn.armylife.union.util;

import java.util.Comparator;

/**
 * creat by xuthus on 2020/3/13.
 **/
public class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
