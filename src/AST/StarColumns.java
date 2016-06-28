package AST;

import Visitors.QueryVisitor;

public class StarColumns extends Columns {
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
