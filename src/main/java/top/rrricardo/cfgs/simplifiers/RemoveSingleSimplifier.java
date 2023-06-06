package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;


/**
 * 算法4：消除单产生式
 */
public class RemoveSingleSimplifier implements Simplifier{
    private static Tree [] trees;   //森林（用于存储生成式）

    //树类
    static class Tree{
        public TreeNode root;   //根节点（一定是一个非终结符N）
        public boolean isSolved;    //标记当前树是否已经处理

        public Tree(String value){
            this.root = new TreeNode(value);
            this.isSolved = false;
        }

    }
    //节点类
    static class TreeNode{
        String value;   //节点的内容
        HashMap<String, TreeNode> Children; //节点的孩子(在文法里，只有非终结符N才有孩子)
        public TreeNode(String value){
            this.value = value;
            this.Children = new HashMap<>();
        }
    }


    //消除一棵树的单产生式
    private static void dealTree(Tree tree, HashSet<Character> NonTerminalSet){
        //如果已经处理过，则直接返回
        if(tree.isSolved){
            return;
        }

        int i;
        //遍历根节点的孩子，看是否有单产生式
        HashSet<String> set = new HashSet<>(tree.root.Children.keySet());
        for(var value: set){
            //如果是单产生式
            if(value.length() == 1 && NonTerminalSet.contains(value.charAt(0))){
                for(i = 0; i < trees.length; i++){
                    if(trees[i].root.value.equals(value)){
                        //如果以这个单符号孩子为根的树还没处理，则先处理孩子
                        if(!trees[i].isSolved){
                            //递归
                            dealTree(trees[i], NonTerminalSet);
                        }
                        break;
                    }
                }

                //代入，从而消除当前单产生式
                tree.root.Children.remove(value);
                tree.root.Children.put(value, trees[i].root);
            }

        }


        tree.isSolved = true;

    }

    //深度优先搜索，遍历树,将叶子节点的值加入新的生成式集合
    private static void dfs(TreeNode node, Hashtable<Character, HashSet<String>> newProductionSet, String str){
        char c = str.charAt(0);

        for(var tempNode: node.Children.values()){
            if(tempNode.Children.size() == 0){
                if(newProductionSet.get(c) != null){
                    newProductionSet.get(c).add(tempNode.value);
                }
                else {
                    HashSet<String> tempSet = new HashSet<>();
                    tempSet.add(tempNode.value);
                    newProductionSet.put(c, tempSet);
                }
            }
            else {
                dfs(tempNode, newProductionSet, str);
            }
        }
    }

    @Override
    public ContextFreeGrammar simplify(ContextFreeGrammar grammar) {
        //构建森林
        trees = new Tree[grammar.getNonTerminalSet().size()];
        //为森林里每一棵树注入内容(为每一个非终结符建立一颗树)
        int i = 0;
        var NonTerminalSet = grammar.getNonTerminalSet();
        for(var value: NonTerminalSet){
            trees[i] = new Tree(String.valueOf(value));

            //如果这个非终结符不出现在任何生成式的左边(这棵树必定没有孩子)
            if(grammar.getProductionSet().get(value) == null){
                trees[i].isSolved = true;
                i++;
                continue;
            }

            var tempSet = grammar.getProductionSet().get(value);
            for(var value1: tempSet){
                trees[i].root.Children.put(value1, new TreeNode(value1));
            }
            i++;
        }

        //新的生成式集合
        Hashtable<Character, HashSet<String>> newProductionSet = new Hashtable<>();

        //处理森林里每一棵树(通过代入法消除单产生式)
        for(i = 0; i < trees.length; i++){
            dealTree(trees[i], NonTerminalSet);
            dfs(trees[i].root, newProductionSet, trees[i].root.value);
        }


        try {
            return new ContextFreeGrammar(grammar.getNonTerminalSet(),grammar.getTerminalSet(),newProductionSet,grammar.getStarter());

        } catch (IllegalContextFreeGrammarException e) {
            throw new RuntimeException(e);
        }


    }



}
