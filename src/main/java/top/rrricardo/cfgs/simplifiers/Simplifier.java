package top.rrricardo.cfgs.simplifiers;

import top.rrricardo.cfgs.exceptions.IllegalContextFreeGrammarException;
import top.rrricardo.cfgs.models.ContextFreeGrammar;

/**
 * 需要实现的化简接口
 */
public interface Simplifier {
    /**
     * 化简指定的上下文无关语法
     * @param grammar 需要化简的语法
     * @return 化简之后的结果
     */
    ContextFreeGrammar simplify(ContextFreeGrammar grammar) throws IllegalContextFreeGrammarException;
}
