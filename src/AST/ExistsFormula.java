package AST;

import java.util.Vector;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class ExistsFormula extends Formula {
	
	public Vector<Var> variables;
	public Formula formula;
	public ExistsFormula(Vector<Var> vs, Formula f) {
		// TODO Auto-generated constructor stub
		variables = vs;
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
