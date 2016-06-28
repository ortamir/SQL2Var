package AST;

import Visitors.QueryVisitor;

public class Assignment extends QueryASTNode{
	
	public String column;
	public Var value;
	
	public Assignment(String col, Var v) {
		// TODO Auto-generated constructor stub
		column = col;
		value = v;
	}
	
	public Assignment(String col, Integer n) {
		column = col;
		value = new Var(n);
	}

	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
	}

}
