package AST;

import Visitors.StatementVisitor;

public class SkipStmt extends Stmt {

	@Override
	public Formula accept(StatementVisitor visitor, Formula Q) {
		return visitor.visit(this, Q);
	}

}
