package AST;

import java.util.Set;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class FreeVarsNode extends Formula{

	public Set<Var> set;
	
	
	
	public FreeVarsNode(Set<Var> set) {
		super();
		this.set = set;
	}



	@Override
	public FormulaASTNode accept(FormulaVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaASTNode accept(FormulaContextVisitor visitor, Object context) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
