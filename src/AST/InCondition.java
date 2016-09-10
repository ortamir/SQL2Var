package AST;

import Visitors.QueryVisitor;

public class InCondition extends Condition {

	public Query sc;
	public String column;
	
	public InCondition(String c, Query q) {
		column = c;
		if (!(q instanceof SelectQuery))
			throw new RuntimeException("in structure used with more than one column");
		sc = (SelectQuery)q;
		
			
	}

	@Override
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
