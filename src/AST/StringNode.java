package AST;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class StringNode extends FormulaASTNode {

	public String str;
	
	
	public StringNode(String str) {
		super();
		this.str = str;
	}


	@Override
	public FormulaASTNode accept(FormulaVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return str;
	}
	
	@Override
	public FormulaASTNode accept(FormulaContextVisitor visitor, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

}
