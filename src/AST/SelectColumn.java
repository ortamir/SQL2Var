package AST;

import Visitors.QueryVisitor;

public class SelectColumn extends QueryASTNode{

	public String table;
	public String column;
	public Condition condition;
	public SelectColumn(String t, String col, String t2, Condition c) {
		// TODO Auto-generated constructor stub
		table = t;
		column = col;
		condition = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
