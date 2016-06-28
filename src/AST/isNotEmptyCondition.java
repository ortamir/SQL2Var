package AST;

import Visitors.QueryVisitor;

public class isNotEmptyCondition extends Condition {

	public SelectColumn selectColumn;
	public isNotEmptyCondition(SelectColumn sc) {
		// TODO Auto-generated constructor stub
		selectColumn = sc;
	}
	
	public isNotEmptyCondition(SelectQuery sq) {
		// TODO Auto-generated constructor stub
		selectColumn = new SelectColumn(sq.table, ((OneColumn)sq.columns).column	, sq.table, sq.condition);
	}
	
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
