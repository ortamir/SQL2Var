package AST;

import Visitors.StatementVisitor;

public class QuerySTMT extends Stmt {
	
	public Query query;
	public QuerySTMT(Query q) {
		query = q;
	}
	@Override
	public Formula accept(StatementVisitor visitor,Formula f) {
		return visitor.visit(this, f);
	}
	

}
