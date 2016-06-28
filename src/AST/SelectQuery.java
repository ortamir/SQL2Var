package AST;

import Visitors.QueryVisitor;

public class SelectQuery extends Query {

	public String table;
	public Columns columns;
	public Condition condition;
	public SelectQuery(String v1, Columns cl, String v2, Condition c) {
		// TODO Auto-generated constructor stub
		table = v1;
		columns = cl;
		condition = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
