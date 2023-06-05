package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * 消除无用符号算法二:找出有用符号
 */
public class SimplifierAlgorithmB implements Simplifier{
    @Override
    public ContextFreeGrammar simplify(ContextFreeGrammar grammar) {
        HashSet<Character> nonTerminalSetZero = new HashSet<>();//N0
        HashSet<Character> nonTerminalSetApostrophe = new HashSet<>();//N'

        nonTerminalSetZero.add(grammar.getStarter());//第一步 N0 = {S}

        HashSet<Character> nonTPlusT = new HashSet<>();
        nonTPlusT.addAll(grammar.getNonTerminalSet());
        nonTPlusT.addAll(grammar.getTerminalSet());



        while (true){
            //第二步
            for(var nonTerminal : nonTerminalSetZero){
                for (var index : nonTPlusT){
                    if(grammar.getProductionSet().get(nonTerminal)!=null){
                        for(var product : grammar.getProductionSet().get(nonTerminal)){
                            if(product.contains(String.valueOf(index))){
                                nonTerminalSetApostrophe.add(index);
                            }
                        }
                    }
                }
            }
            nonTerminalSetApostrophe.addAll(nonTerminalSetZero);

            //第三步
            if(!nonTerminalSetZero.equals(nonTerminalSetApostrophe)){
                //第四步
                nonTerminalSetZero = new HashSet<>(nonTerminalSetApostrophe);
                //接下来会返回执行第二步
            }
            else{
                break;//跳到第五步
            }
        }

        //第五步
        HashSet<Character> resultNonTerminalSet = new HashSet<>();
        HashSet<Character> resultTerminalSet = new HashSet<>();

        for(var nonTerminal : nonTerminalSetApostrophe){
            if(grammar.getNonTerminalSet().contains(nonTerminal)){
                resultNonTerminalSet.add(nonTerminal);
            }
            if(grammar.getTerminalSet().contains(nonTerminal)){
                resultTerminalSet.add(nonTerminal);
            }
        }

        Hashtable<Character,HashSet<String>> resultProductionTable = new Hashtable<>();
        //接下来进行新生成式的构造
        for(var nonTerminal:grammar.getNonTerminalSet()){
            HashSet<String> resultProductionSet = new HashSet<>();
            for(var production : grammar.getProductionSet().get(nonTerminal)){
                if(nonTerminalSetApostrophe.contains(nonTerminal)){
                    if(containsOnly(production,nonTerminalSetApostrophe)){
                        resultProductionSet.add(production);
                    }
                }
            }
            if(!resultProductionSet.isEmpty()){
                resultProductionTable.put(nonTerminal,resultProductionSet);
            }
        }
        /*System.out.println(resultNonTerminalSet);
        System.out.println(resultTerminalSet);
        System.out.println(resultProductionTable);*/
        try {
            return new ContextFreeGrammar(resultNonTerminalSet,resultTerminalSet,resultProductionTable,grammar.getStarter());
        } catch (IllegalContextFreeGrammarException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * 检查字符串中是否只包含所给字符串
     * @param str 要检查的字符串
     * @param contain 应该包含的字符串集合
     * @return false 如果不包含应该包含的字符串，或者出现其它字符串 true 只包含应该包含的字符串
     */
    private static boolean containsOnly(String str, HashSet<Character> contain){

        for (char c : str.toCharArray()) {
            if (!contain.contains(c)) {
                return false;
            }
        }
        return true;
    }

}
