package top.rrricardo.cfgs;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;
import top.rrricardo.cfgs.simplifiers.SimplifierAlgorithmA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        try {
            var input = Files.readString(Paths.get("input.txt"));

            var grammar = ContextFreeGrammar.parse(input);

            var simplifierA = new SimplifierAlgorithmA();

            System.out.println(grammar);

            System.out.println(simplifierA.simplify(grammar).toString());
        } catch (IOException | IllegalContextFreeGrammarException exception) {
            exception.printStackTrace();
        }

    }
}
