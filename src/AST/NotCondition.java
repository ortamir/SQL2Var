package AST;

import Visitors.QueryVisitor;

public class NotCondition extends Condition {

	public Condition cond;
	
	public NotCondition(Condition c) {
		// TODO Auto-generated constructor stub
		cond = c;
	}
	
	@Override
	public Object accept(QueryVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

}
