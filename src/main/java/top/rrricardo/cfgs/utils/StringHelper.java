package top.rrricardo.cfgs.utils;

import java.util.HashSet;

public class StringHelper {
    /**
     * 判断给定的字符串是否只有集合中的字符组成
     * @param string 需要判断的字符串
     * @param elements 字符集合
     * @return 弱威震
     */
    public static boolean onlyContains(String string, HashSet<String> elements) {
        var array = string.split("");

        for (var c : array) {
            if (!elements.contains(c)) {
                return false;
            }
        }

        return true;
    }
}
