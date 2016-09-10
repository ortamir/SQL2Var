package AST;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class NegationFormula extends Formula {
	
	public Formula formula;
	public NegationFormula(Formula f) {
		// TODO Auto-generated constructor stub
		if (f == null) throw new NullPointerException();
		formula = f;
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
