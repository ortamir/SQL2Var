package AST;

import Visitors.QueryVisitor;

public class MoveQuery extends Query {

	public String table,table2;
	public Columns columns,columns2;
	public Condition condition;
	
	public MoveQuery(String v1, Columns cl, String v2, Columns cl2, Condition c) {
		// TODO Auto-generated constructor stub
		table = v1;
		columns = cl;
		table2= v2;
		columns2 = cl2;
		condition = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}
	
	

}
