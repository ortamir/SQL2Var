package AST;

import Visitors.QueryVisitor;

public class UpdateQuery extends Query {

	public String table;
	public Assignment assignment;
	public Condition condition;
	
	public UpdateQuery(String v1, Assignment a, Condition c) {
		// TODO Auto-generated constructor stub
		table = v1;
		assignment = a;
		condition = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
