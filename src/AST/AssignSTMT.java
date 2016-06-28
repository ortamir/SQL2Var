package AST;

import Visitors.StatementVisitor;

public class AssignSTMT extends Stmt {

	public String var;
	public Query query;
	public AssignSTMT(String v, Query q) {
		var = v;
		query = q;
	}
	
	@Override
	public Formula accept(StatementVisitor visitor,Formula f) {
		return visitor.visit(this, f);
	}

}
