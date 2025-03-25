package parser;

import parser.grammar.AbstractGrammar;
import symbols.NonTerminal;
import symbols.Symbol;
import symbols.Token;
import symbols.Tokens;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * LR(0) parser.
 */
public class LR1Parser {

    // Generator for LR(0) sets
    private LR1SetGenerator generatorLR1;

    // Start symbol of grammar
    private NonTerminal start;

    /**
     * Constructor.
     *
     * @param grammar
     *            Grammar.
     */
    public LR1Parser(AbstractGrammar grammar) {
        this.generatorLR1 = new LR1SetGenerator(grammar);
        this.start = grammar.getStart();
    }

    /**
     * Parse the input via LR(0) parsing.
     *
     * @param lexOutput
     *            List of symbols
     * @return A list of rules which corresponds to the right-most analysis
     * @throws ParserException
     *             Parser exception
     */
    public List<Rule> parse(List<Symbol> lexOutput) throws ParserException {
        List<Rule> analysis = new LinkedList<Rule>();

        // TODO implement LR(1) parser

        return analysis;
    }
}
