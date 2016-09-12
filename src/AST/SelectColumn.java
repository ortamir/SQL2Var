package AST;

import Visitors.QueryVisitor;

public class SelectColumn extends QueryASTNode{

	public String table;
	public String column;
	public Condition condition;
	public SelectColumn(String t, String col, String t2, Condition c) {
		table = t;
		column = col;
		condition = c;
		/* TODO what is t2 and why is it not used? */
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
