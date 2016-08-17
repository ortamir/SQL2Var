package AST;

import Visitors.QueryVisitor;

public class inCondition extends Condition {

	public Query sc;
	public String column;
	
	public inCondition(String c, Query q) {
		column = c;
		if (!(q instanceof SelectQuery))
			throw new RuntimeException("in structure used with more than one column");
		sc = (SelectQuery)q;
		
			
	}

	@Override
	public Object accept(QueryVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

}
