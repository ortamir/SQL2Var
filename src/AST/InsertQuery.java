package AST;

import java.util.Vector;

import Visitors.QueryVisitor;

public class InsertQuery extends Query {
	
	public String table;
	public Vector<Var> terms;
	
	/*
	public InsertQuery(String v1, String v2, String v3) {
		table = v1;
		first = new Var(v2);
		second = new Var(v3);
	}
*/
	public InsertQuery(String v1, Vector<Var> tl) {
		table = v1;
		terms = tl;
		
	}

	@Override
	public Object accept(QueryVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

}
