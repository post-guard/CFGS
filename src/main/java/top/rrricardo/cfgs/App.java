package top.rrricardo.cfgs;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;
import top.rrricardo.cfgs.simplifiers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        try {
            var input = Files.readString(Paths.get("input.txt"));

            var grammar = ContextFreeGrammar.parse(input);

            System.out.println("原始输入:");
            System.out.println(grammar);

            var simplifier = new ArrayList<Simplifier>();
            simplifier.add(new RemoveEpsilonSimplifier());
            simplifier.add(new RemoveSingleSimplifier());
            simplifier.add(new RemoveUselessSymbolSimplifierA());
            simplifier.add(new RemoveUselessSymbolSimplifierB());

            for (var s : simplifier) {
                grammar = s.simplify(grammar);
                System.out.println();
                System.out.println(grammar);
            }
        } catch (IOException | IllegalContextFreeGrammarException exception) {
            exception.printStackTrace();
        }

    }
}
