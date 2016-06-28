package AST;

import Visitors.StatementVisitor;

public abstract class Stmt {
	
	public abstract Formula accept(StatementVisitor visitor,Formula Q);

}
