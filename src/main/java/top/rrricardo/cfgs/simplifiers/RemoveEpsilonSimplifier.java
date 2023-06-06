package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * 消除空字符串化简器
 */
public class RemoveEpsilonSimplifier implements Simplifier {
    @Override
    public ContextFreeGrammar simplify(ContextFreeGrammar grammar) throws IllegalContextFreeGrammarException {
        var generateEpsilonSet = findNonTerminalSetGeneratingEpsilon(grammar);

        if (generateEpsilonSet.size() == 0) {
            // 不用化简
            return grammar;
        }

        var newProductionSet = new Hashtable<Character, HashSet<String>>();
        var productionSet = grammar.getProductionSet();

        for (var entry : productionSet.entrySet()) {
            var production = new HashSet<String>();

            for (var item : entry.getValue()) {
                if (item.equals("ε")) {
                    continue;
                }

                // 本身也要添加到生成式中
                production.add(item);

                // 下面使用一种排列的思想
                var charArray = item.toCharArray();
                var middle = new HashSet<String>();

                for (var c : charArray) {
                    if (generateEpsilonSet.contains(c)) {
                        var temp = item.replaceFirst(String.valueOf(c), "");
                        middle.add(temp);
                        production.add(temp);
                    }
                }

                // 处理到中间过程集合中仅有终结符
                while (!notContains(middle, generateEpsilonSet)) {
                    var nextMiddle = new HashSet<String>();

                    for (var value : middle) {
                        charArray = value.toCharArray();

                        for (var c : charArray) {
                            if (generateEpsilonSet.contains(c)) {
                                var temp = value.replaceFirst(String.valueOf(c), "");
                                production.add(temp);
                                nextMiddle.add(temp);
                            }
                        }
                    }

                    middle = nextMiddle;
                }
            }

            newProductionSet.put(entry.getKey(), production);
        }

        var newNonTerminalSet = new HashSet<>(grammar.getNonTerminalSet());
        var starter = grammar.getStarter();
        // 新建的起始符
        if (generateEpsilonSet.contains(grammar.getStarter())) {
            newNonTerminalSet.add('%');
            var startProductionSet = new HashSet<String>();
            startProductionSet.add("ε");
            startProductionSet.add(String.valueOf(grammar.getStarter()));
            newProductionSet.put('%', startProductionSet);
            starter = '%';
        }

        return new ContextFreeGrammar(
                newNonTerminalSet,
                new HashSet<>(grammar.getTerminalSet()),
                newProductionSet,
                starter
        );
    }

    /**
     * 获得可以生成空字符串的非终结符集合
     *
     * @param grammar 文法
     * @return 非终结符集合
     */
    private HashSet<Character> findNonTerminalSetGeneratingEpsilon(ContextFreeGrammar grammar) {
        var result = new HashSet<Character>();

        for (var entry : grammar.getProductionSet().entrySet()) {
            for (var value : entry.getValue()) {
                if (value.equals("ε")) {
                    result.add(entry.getKey());
                }
            }
        }

        while (true) {
            var middle = new HashSet<>(result);

            for (var entry : grammar.getProductionSet().entrySet()) {
                for (var value : entry.getValue()) {
                    if (onlyContains(value, result)) {
                        middle.add(entry.getKey());
                    }
                }
            }

            if (middle.equals(result)) {
                break;
            } else {
                result = middle;
            }
        }

        return result;
    }

    private static boolean onlyContains(String input, HashSet<Character> elements) {
        var array = input.toCharArray();

        var flag = true;

        for (var c : array) {
            if (!elements.contains(c)) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    private static boolean notContains(String input, HashSet<Character> elements) {
        var array = input.toCharArray();

        var flag = true;

        for (var c : array) {
            if (elements.contains(c)) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    private static boolean notContains(HashSet<String> inputs, HashSet<Character> elements) {
        var flag = true;

        for (var item : inputs) {
            flag = notContains(item, elements);

            if (!flag) {
                break;
            }
        }

        return flag;
    }
}
