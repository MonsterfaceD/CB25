import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class ThompsonAutomatonCreator extends RegexGrammarBaseListener {
	Automaton result;
	Map<String, Symbol> encounteredSymbols = new HashMap<String, Symbol>();
	ParseTreeProperty<Automaton> automata = new ParseTreeProperty<Automaton>();

	void setAutomaton(ParseTree ctx, Automaton aut) {
		automata.put(ctx, aut);
	}

	Automaton getAutomaton(ParseTree ctx) {
		return automata.get(ctx);
	}

	ParseTreeProperty<Set<Symbol>> sets = new ParseTreeProperty<Set<Symbol>>();

	void setSet(ParseTree ctx, Set<Symbol> set) {
		sets.put(ctx, set);
	}

	Set<Symbol> getSet(ParseTree ctx) {
		return sets.get(ctx);
	}

	@Override
	public void exitUnion(RegexGrammarParser.UnionContext ctx) {
		Set<Symbol> newSet = new HashSet<Symbol>();
		for (ParseTree cctx : ctx.children) {
			newSet.addAll(getSet(cctx));
		}
		setSet(ctx, newSet);
	}

	@Override
	public void exitMinus(RegexGrammarParser.MinusContext ctx) {
		Set<Symbol> newSet = new HashSet<Symbol>();
		newSet.addAll(getSet(ctx.lhs));
		newSet.removeAll(getSet(ctx.rhs));
		setSet(ctx, newSet);
	}

	@Override
	public void exitIntersect(RegexGrammarParser.IntersectContext ctx) {
		Set<Symbol> newSet = new HashSet<Symbol>();
		newSet.addAll(getSet(ctx.children.get(0)));
		for (ParseTree cctx : ctx.children) {
			newSet.retainAll(getSet(cctx));
		}
		setSet(ctx, newSet);
	}

	@Override
	public void exitSetdecl(RegexGrammarParser.SetdeclContext ctx) {
		Set<Symbol> newSet = new HashSet<Symbol>();
		for (Token tok : ctx.symbols) {
			newSet.add(registerSymbol(tok.getText()));
		}
		setSet(ctx, newSet);
	}

	@Override
	public void exitSetgroup(RegexGrammarParser.SetgroupContext ctx) {
		setSet(ctx, getSet(ctx.setdesc()));
	}

	@Override
	public void exitSet(RegexGrammarParser.SetContext ctx) {
		Automaton aut = new Automaton();
		State init = aut.addNewState(true, false);
		State target = aut.addNewState(false, true);
		for (Symbol s : getSet(ctx.setdesc())) {
			aut.addTransition(init, s, target);
		}
		setAutomaton(ctx, aut);

	}

	@Override
	public void exitStart(RegexGrammarParser.StartContext ctx) {
		result = getAutomaton(ctx.getChild(ctx.getChildCount() - 1));
	}

	private Symbol registerSymbol(String representation) {
		if (encounteredSymbols.containsKey(representation)) {
			return encounteredSymbols.get(representation);
		} else {
			Symbol sym = new Symbol(representation);
			encounteredSymbols.put(representation, sym);
			return sym;
		}
	}

	@Override
	public void exitSymb(RegexGrammarParser.SymbContext ctx) {
		Automaton aut = new Automaton();
		State init = aut.addNewState(true, false);
		State target = aut.addNewState(false, true);
		Symbol sym = registerSymbol(ctx.ID().getText());
		aut.addTransition(init, sym, target);
		setAutomaton(ctx, aut);
	}

	@Override
	public void exitEps(RegexGrammarParser.EpsContext ctx) {
		Automaton aut = new Automaton();
		State init = aut.addNewState(true, false);
		State target = aut.addNewState(false, true);
		aut.addTransition(init, Symbol.EPS, target);
		setAutomaton(ctx, aut);
	}

	@Override
	public void exitGroup(RegexGrammarParser.GroupContext ctx) {
		setAutomaton(ctx, getAutomaton(ctx.regex()));
	}

	@Override
	public void exitChoice(RegexGrammarParser.ChoiceContext ctx) {
		Automaton aut = new Automaton();

		State init = aut.addNewState(true, false);
		State accept = aut.addNewState(false, true);
		for (RegexGrammarParser.RegexContext subctx : ctx.CEX) {
			Automaton sub = getAutomaton(subctx);
			aut.addAutomaton(sub);
			for (State in : sub.getInitial()) {
				aut.addTransition(init, Symbol.EPS, in);
			}
			for (State out : sub.getAccepting()) {
				aut.addTransition(out, Symbol.EPS, accept);
			}
		}
		setAutomaton(ctx, aut);
	}

	@Override
	public void exitConcat(RegexGrammarParser.ConcatContext ctx) {
		Automaton aut = new Automaton();
		Automaton sub = getAutomaton(ctx.getChild(0));
		aut.addAutomaton(sub);
		for (State in : getAutomaton(ctx.getChild(0)).getInitial()) {
			aut.makeInitial(in);
		}

		for (int i = 1; i < ctx.getChildCount(); ++i) {
			Automaton nextAut = getAutomaton(ctx.getChild(i));
			aut.addAutomaton(nextAut);
			Automaton lastAut = getAutomaton(ctx.getChild(i - 1));

			for (State out : lastAut.getAccepting()) {
				for (State in : nextAut.getInitial()) {
					aut.addTransition(out, Symbol.EPS, in);
				}
			}
		}

		for (State acc : getAutomaton(ctx.getChild(ctx.getChildCount() - 1)).getAccepting()) {
			aut.makeAccepting(acc);
		}
		setAutomaton(ctx, aut);
	}

	@Override
	public void exitKleene(RegexGrammarParser.KleeneContext ctx) {
		Automaton aut = new Automaton();
		Automaton sub = getAutomaton(ctx.regex());
		aut.addAutomaton(sub);
		State init = aut.addNewState(true, false);
		State accept = aut.addNewState(false, true);
		for (State in : sub.getInitial()) {
			aut.addTransition(init, Symbol.EPS, in);
		}
		for (State out : sub.getAccepting()) {
			aut.addTransition(out, Symbol.EPS, accept);
			aut.addTransition(out, Symbol.EPS, init);
		}
		aut.addTransition(init, Symbol.EPS, accept);

		setAutomaton(ctx, aut);
	}

	public Automaton obtainResult() {
		// System.out.print(result.dotFormat());
		result.removeEpsilonTransitions();
		// System.out.print(result.dotFormat());
		return result;
	}
}
