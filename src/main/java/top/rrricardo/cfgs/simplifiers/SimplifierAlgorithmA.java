package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;


import java.util.HashSet;
import java.util.Hashtable;

/**
 * 消除无用符号算法一:找出有用非终结符
 */
public class SimplifierAlgorithmA implements Simplifier{
    @Override
    public ContextFreeGrammar simplify(ContextFreeGrammar grammar) {

        HashSet<Character> nonTerminalSetZero = new HashSet<>();//N0 第一步 N0 = {}
        HashSet<Character> nonTerminalSetApostrophe = new HashSet<>();//N'

        //第二步
        for(Character nonTerminal : grammar.getNonTerminalSet()){
            HashSet<String> currentMap = grammar.getProductionSet().get(nonTerminal);
            for(Character terminal : grammar.getTerminalSet()){
                if(currentMap!=null){
                    for(var currentIndex : currentMap){
                        if(currentIndex.contains(String.valueOf(terminal))){
                            nonTerminalSetApostrophe.add(nonTerminal);
                        }
                    }
                }
            }
            if(currentMap!=null && currentMap.contains("ε")){//对epsilon进行操作
                nonTerminalSetApostrophe.add(nonTerminal);
            }
        }

        //System.out.println(nonTerminalSetApostrophe.toString());
        while(true) {
            //第三步
            if (!nonTerminalSetZero.equals(nonTerminalSetApostrophe)) {

                nonTerminalSetZero = new HashSet<>(nonTerminalSetApostrophe);//第四步

                //第五步
                HashSet<Character> searchSet = new HashSet<>();
                searchSet.addAll(nonTerminalSetZero);
                searchSet.addAll(grammar.getTerminalSet());

                for (Character nonTerminal : grammar.getNonTerminalSet()) {
                    HashSet<String> currentMap = grammar.getProductionSet().get(nonTerminal);
                    //for (Character index : searchSet) {
                        if (currentMap != null) {
                            for(var currentIndex : currentMap){
                                /*if(currentIndex.contains(String.valueOf(index))){
                                    nonTerminalSetApostrophe.add(nonTerminal);
                                }*/
                                if(containsOnly(currentIndex,searchSet)){
                                    nonTerminalSetApostrophe.add(nonTerminal);
                                }
                            }
                        }
                   // }
                    if (currentMap!=null && currentMap.contains("ε")) {//对epsilon进行操作
                        nonTerminalSetApostrophe.add(nonTerminal);
                    }
                }

                nonTerminalSetApostrophe.addAll(nonTerminalSetZero);
            }
            else {
                    break;
            }
        }

        //把出现【不在新的非终结符集合中】的非终结符的生成式去除，构造新生成式
        Hashtable<Character,HashSet<String>> resultTable = new Hashtable<>();
        for(var nonTerminal : nonTerminalSetApostrophe){

            HashSet<String> resultSet = new HashSet<>();
            HashSet<Character> nonTPlusT = new HashSet<>();
            nonTPlusT.addAll(nonTerminalSetApostrophe);
            nonTPlusT.addAll(grammar.getTerminalSet());

            for(var set : grammar.getProductionSet().get(nonTerminal)){

                if(containsOnly(set,nonTPlusT)){
                    resultSet.add(set);
                }
            }
            resultTable.put(nonTerminal,resultSet);
        }
       /* System.out.println(nonTerminalSetApostrophe);
        System.out.println(resultTable);*/
        try {
            return new ContextFreeGrammar(nonTerminalSetApostrophe,grammar.getTerminalSet(),resultTable,grammar.getStarter());

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
