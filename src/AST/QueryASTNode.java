package AST;

import Visitors.QueryVisitor;

public abstract class QueryASTNode {
	
	public abstract Object accept(QueryVisitor visitor);

}
