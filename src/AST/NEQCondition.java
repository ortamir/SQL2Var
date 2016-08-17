package AST;

import Visitors.QueryVisitor;

public class NEQCondition extends Condition {

	public String column;
	public Var value;
	public NEQCondition(String c, Var v) {
		// TODO Auto-generated constructor stub
		column = c;
		value = v;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
