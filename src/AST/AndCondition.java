package AST;

import Visitors.QueryVisitor;

public class AndCondition extends Condition {

	public Condition first,second;
	public AndCondition(Condition c1, Condition c2) {
		// TODO Auto-generated constructor stub
		first = c1;
		second = c2;
	}
	

	@Override
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
