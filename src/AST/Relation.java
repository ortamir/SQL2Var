package AST;

import java.util.Vector;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class Relation extends FormulaASTNode{
	
	public String name;
	public Vector<Var> terms;
	
	public Relation(String id,Vector<Var> tl){
		name = id;
		terms = tl;
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
