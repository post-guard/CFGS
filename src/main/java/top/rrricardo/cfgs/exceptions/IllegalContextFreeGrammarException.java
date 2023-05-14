package top.rrricardo.cfgs.exceptions;

/**
 * 非法上下文无关文法引发的错误
 */
public class IllegalContextFreeGrammarException extends Exception {
    public IllegalContextFreeGrammarException() {
        super();
    }

    public IllegalContextFreeGrammarException(String message) {
        super(message);
    }
}
