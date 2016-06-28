package AST;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class AndFormula extends Formula {
	
public Formula first,second;
	
	public AndFormula(Formula f1, Formula f2) {
		// TODO Auto-generated constructor stub
		first = f1;
		second = f2;
	}

	@Override
	public FormulaASTNode accept(FormulaVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	@Override
	public FormulaASTNode accept(FormulaContextVisitor visitor, Object context) {
		// TODO Auto-generated method stub
		return visitor.visit(this,context);
	}


}
