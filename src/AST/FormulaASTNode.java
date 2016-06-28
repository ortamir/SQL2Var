package AST;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public abstract class FormulaASTNode {
	
	public abstract FormulaASTNode accept(FormulaVisitor visitor);
	
	public abstract FormulaASTNode accept(FormulaContextVisitor visitor,Object context);
}
