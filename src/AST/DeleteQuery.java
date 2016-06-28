package AST;

import Visitors.QueryVisitor;

public class DeleteQuery extends Query {

	public String table;
	public Condition condition;
	
	public DeleteQuery(String v1, Condition c) {
		// TODO Auto-generated constructor stub
		table =v1;
		condition = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
