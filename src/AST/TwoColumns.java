package AST;

import Visitors.QueryVisitor;

public class TwoColumns extends Columns {
	
	public String first,second;
	
	public TwoColumns(String c1, String c2) {
		// TODO Auto-generated constructor stub
		first = c1;
		second = c2;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
