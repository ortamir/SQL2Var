package AST;

import Visitors.QueryVisitor;

public abstract class Value extends QueryASTNode{

	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}
}
