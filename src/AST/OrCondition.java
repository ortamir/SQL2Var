package AST;

import Visitors.QueryVisitor;

public class OrCondition extends Condition {

	public Condition first,second;
	public OrCondition(Condition c1, Condition c2) {
		// TODO Auto-generated constructor stub
		first = c1;
		second = c2;
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
