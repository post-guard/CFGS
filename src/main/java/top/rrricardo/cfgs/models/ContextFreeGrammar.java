package top.rrricardo.cfgs.models;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * 上下文无关文法
 */
public class ContextFreeGrammar {
    private final HashSet<String> nonTerminalSet;
    private final HashSet<String> terminalSet;
    private final Hashtable<String, HashSet<String>> productionSet;

    private final String starter;

    public ContextFreeGrammar(
            HashSet<String> nonTerminalSet,
            HashSet<String> terminalSet,
            Hashtable<String, HashSet<String>> productionSet,
            String starter) throws IllegalContextFreeGrammarException {
        this.nonTerminalSet = nonTerminalSet;
        this.terminalSet = terminalSet;
        this.productionSet = productionSet;
        this.starter = starter;

        checkLegality();
    }


    public HashSet<String> getNonTerminalSet() {
        return nonTerminalSet;
    }

    public HashSet<String> getTerminalSet() {
        return terminalSet;
    }

    public Hashtable<String, HashSet<String>> getProductionSet() {
        return productionSet;
    }

    public String getStarter() {
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
    }
}