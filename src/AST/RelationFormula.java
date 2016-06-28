package AST;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class RelationFormula extends Formula {
	
	public Relation relation;
	
	public RelationFormula(Relation at) {
		// TODO Auto-generated constructor stub
		relation = at;
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
