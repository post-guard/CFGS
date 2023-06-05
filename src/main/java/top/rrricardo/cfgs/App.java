package top.rrricardo.cfgs;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;

import java.util.HashSet;
import java.util.Hashtable;

public class App {
    public static void main(String[] args) {
        var nonTerminalSet = new HashSet<Character>();
        nonTerminalSet.add('S');
        nonTerminalSet.add('B');
        nonTerminalSet.add('C');

        var terminalSet = new HashSet<Character>();
        terminalSet.add('0');
        terminalSet.add('1');

        var sProductionSet = new HashSet<String>();
        sProductionSet.add("0C");
        sProductionSet.add("1B");

        var bProductionSet = new HashSet<String>();
        bProductionSet.add("0");
        bProductionSet.add("0S");
        bProductionSet.add("1BB");

        var cProductionSet = new HashSet<String>();
        cProductionSet.add("1");
        cProductionSet.add("1S");
        cProductionSet.add("0CC");

        var productionTable = new Hashtable<Character, HashSet<String>>();
        productionTable.put('S', sProductionSet);
        productionTable.put('B', bProductionSet);
        productionTable.put('C', cProductionSet);

        try {
            var grammar = new ContextFreeGrammar(nonTerminalSet, terminalSet, productionTable, 'S');
            System.out.println(grammar);
        } catch (IllegalContextFreeGrammarException ignored) {

        }
    }
}
