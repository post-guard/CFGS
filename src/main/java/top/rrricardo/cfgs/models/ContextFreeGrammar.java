package top.rrricardo.cfgs.models;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * 上下文无关文法
 */
public class ContextFreeGrammar {
    private final HashSet<Character> nonTerminalSet;
    private final HashSet<Character> terminalSet;
    private final Hashtable<Character, HashSet<String>> productionSet;

    private final char starter;

    public ContextFreeGrammar(
            HashSet<Character> nonTerminalSet,
            HashSet<Character> terminalSet,
            Hashtable<Character, HashSet<String>> productionSet,
            char starter) throws IllegalContextFreeGrammarException {
        this.nonTerminalSet = nonTerminalSet;
        this.terminalSet = terminalSet;
        this.productionSet = productionSet;
        this.starter = starter;

        checkLegality();
    }


    public HashSet<Character> getNonTerminalSet() {
        return nonTerminalSet;
    }

    public HashSet<Character> getTerminalSet() {
        return terminalSet;
    }

    public Hashtable<Character, HashSet<String>> getProductionSet() {
        return productionSet;
    }

    public char getStarter() {
        return starter;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (var key : productionSet.keySet()) {
            builder.append(key).append("->");
            var set = productionSet.get(key);
            var innerBuilder = new StringBuilder();
            for (var value : set) {
                innerBuilder.append(value).append('|');
            }
            var inner = innerBuilder.toString();
            builder.append(inner, 0, inner.length() - 1).append('\n');
        }

        return builder.toString();
    }

    public static ContextFreeGrammar parse(String input) throws IllegalContextFreeGrammarException {
        var lines = input.split(System.lineSeparator());
        var nonTerminalSet = new HashSet<Character>();
        var terminalSet = new HashSet<Character>();
        var productionSet = new Hashtable<Character, HashSet<String>>();

        // 第一行是非终结符
        var nonTerminalArray = lines[0].split(",");
        for (var item : nonTerminalArray) {
            if (item.length() != 1) {
                throw new IllegalContextFreeGrammarException(item + "不是有效的非终结符");
            }

            nonTerminalSet.add(item.charAt(0));
        }

        // 第二行是终结符
        var terminalArray = lines[1].split(",");
        for (var item : terminalArray) {
            if (item.length() != 1) {
                throw new IllegalContextFreeGrammarException(item + "不是有效的终结符");
            }

            terminalSet.add(item.charAt(0));
        }

        // 第三行是起始符
        if (lines[2].length() != 1) {
            throw new IllegalContextFreeGrammarException(lines[3] + "不是有效的起始符");
        }
        var starter = lines[2].charAt(0);

        for (var pos = 3; pos < lines.length; pos++) {
            // 生成式的模式 S->A0|B1
            var temp = lines[pos].split("->");

            if (temp.length != 2) {
                throw new IllegalContextFreeGrammarException(lines[pos] + "不是有效的生成式");
            }

            var first = temp[0];
            var second = temp[1];

            if (first.length() != 1) {
                throw new IllegalContextFreeGrammarException(first + "不是有效的非终结符");
            }

            var resultArray = second.split("\\|");

            var product = new HashSet<>(Arrays.asList(resultArray));

            productionSet.put(first.charAt(0), product);
        }

        var result = new ContextFreeGrammar(nonTerminalSet, terminalSet, productionSet, starter);

        result.checkLegality();

        return result;
    }

    /**
     * 验证是否为合法的上下文无关文法
     */
    private void checkLegality() throws IllegalContextFreeGrammarException {
        // 首先判断起始符的合法性
        if (!nonTerminalSet.contains(starter)) {
            throw new IllegalContextFreeGrammarException("起始符不在非终结符集合中");
        }

        for (var key : productionSet.keySet()) {
            if (!nonTerminalSet.contains(key)) {
                throw new IllegalContextFreeGrammarException("生成式左部： " + key + "不是单个非终结符");
            }
        }

        for (var value : productionSet.values()) {
            for (var item : value) {
                if (item.equals("ε")) {
                    // 如果是空字符串就不做处理
                    continue;
                }

                var array = item.toCharArray();

                for (var c : array) {

                    if (!nonTerminalSet.contains(c) && !terminalSet.contains(c)) {
                        throw new IllegalContextFreeGrammarException("非法的生成式右部" + item);
                    }
                }
            }
        }
    }
}
