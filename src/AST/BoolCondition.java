package AST;

import Visitors.QueryVisitor;

public class BoolCondition extends Condition {

	public boolean c;
	public BoolCondition(boolean b) {
		c = b;
	}
	@Override
	public Object accept(QueryVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

}
