package AST;

import Visitors.QueryVisitor;

public class OneColumn extends Columns {
	
	public String column;
	public OneColumn(String c) {
		// TODO Auto-generated constructor stub
		column = c;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
