package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.models.ContextFreeGrammar;

import java.util.HashSet;

public class RemoveEpsilonSimplifier implements Simplifier {
    @Override
    public ContextFreeGrammar simplify(ContextFreeGrammar grammar) {
        return null;
    }

    /**
     * 获得可以生成空字符串的非终结符集合
     * @param grammar 文法
     * @return 非终结符集合
     */
    private HashSet<Character> findNonTerminalSetGeneratingEpsilon(ContextFreeGrammar grammar) {
        var result = new HashSet<Character>();

        for (var entry : grammar.getProductionSet().entrySet()) {
            for (var value : entry.getValue()) {
                if (value.isEmpty()) {
                    result.add(entry.getKey());
                }
            }
        }

        while (true) {
            var middle = new HashSet<>(result);

            for (var entry : grammar.getProductionSet().entrySet()) {
                for (var value : entry.getValue()) {
                    if (onyContains(value, result)) {
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

    private static boolean onyContains(String input, HashSet<Character> elements) {
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
}
