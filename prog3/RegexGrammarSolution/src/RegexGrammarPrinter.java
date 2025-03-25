public class RegexGrammarPrinter extends RegexGrammarBaseListener {

	@Override
	public void enterUnion(RegexGrammarParser.UnionContext ctx) {
		System.out.print("(UNION");
	}

	@Override
	public void exitUnion(RegexGrammarParser.UnionContext ctx) {
		System.out.print(")");
	}

	@Override
	public void enterIntersect(RegexGrammarParser.IntersectContext ctx) {
		System.out.print("(INTERSECT");
	}

	@Override
	public void exitIntersect(RegexGrammarParser.IntersectContext ctx) {
		System.out.print(")");
	}

	@Override
	public void exitSet(RegexGrammarParser.SetContext ctx) {
		System.out.print(')');

	}

	@Override
	public void enterEps(RegexGrammarParser.EpsContext ctx) {
		System.out.print("EPS");
	}

	@Override
	public void enterConcat(RegexGrammarParser.ConcatContext ctx) {
		System.out.print("(CONCAT ");

	}

	@Override
	public void exitConcat(RegexGrammarParser.ConcatContext ctx) {
		System.out.print(')');

	}

	@Override
	public void enterChoice(RegexGrammarParser.ChoiceContext ctx) {
		System.out.print("(CHOICE ");

	}

	@Override
	public void exitChoice(RegexGrammarParser.ChoiceContext ctx) {
		System.out.print(')');

	}

	@Override
	public void enterSymb(RegexGrammarParser.SymbContext ctx) {
		System.out.print(ctx.ID().getText());

	}

	@Override
	public void enterKleene(RegexGrammarParser.KleeneContext ctx) {
		System.out.print("(KLEENE ");
	}

	@Override
	public void exitKleene(RegexGrammarParser.KleeneContext ctx) {
		System.out.print(')');

	}

}
