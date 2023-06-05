package top.rrricardo.cfgs;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;
import top.rrricardo.cfgs.simplifiers.SimplifierAlgorithmA;
import top.rrricardo.cfgs.simplifiers.SimplifierAlgorithmB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        try {
            var input = Files.readString(Paths.get("input.txt"));

            var grammar = ContextFreeGrammar.parse(input);

            var simplifierA = new SimplifierAlgorithmA();
            var simplifierB = new SimplifierAlgorithmB();
            System.out.println("原始输入:");
            System.out.println(grammar);

            grammar = simplifierA.simplify(grammar);
            System.out.println("消除无用符号算法一:");
            System.out.println(grammar);

            grammar = simplifierB.simplify(grammar);
            System.out.println("消除无用符号算法二:");
            System.out.println(grammar);
        } catch (IOException | IllegalContextFreeGrammarException exception) {
            exception.printStackTrace();
        }

    }
}
