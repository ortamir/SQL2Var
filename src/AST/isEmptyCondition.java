package AST;

import Visitors.QueryVisitor;

public class isEmptyCondition extends Condition {

	public SelectColumn selectColumn;
	
	public isEmptyCondition(SelectColumn sc) {
		// TODO Auto-generated constructor stub
		sc= selectColumn;
	}
	
	public isEmptyCondition(SelectQuery sq) {
		// TODO Auto-generated constructor stub
		selectColumn = new SelectColumn(sq.table, ((OneColumn)sq.columns).column	, sq.table, sq.condition);
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
