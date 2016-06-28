package AST;

import java.util.Vector;

import Visitors.StatementVisitor;

public class IfSTMT extends Stmt {
	public Vector<Stmt> trueStmts,falseStmts;
	public int type;  // 0=if(IsEmpty(...)), 1=if(IsNonEmpty(...)), 3=if(var=const)
	public Query query;
	public String var;
	public Var val;
	
	
	public IfSTMT(int i, Query q, Vector<Stmt> th, Vector<Stmt> el) {//type 0 or 1
		type = i ;
		query = q;
		trueStmts = th;
		falseStmts = el;
	}


	public IfSTMT(int i, String v, Var n, Vector<Stmt> s1, Vector<Stmt> s2) { //type 3
		type = i;
		this.var = v;
		this.val = n;
		trueStmts = s1;
		falseStmts = s2;
	}
	
	@Override
	public Formula accept(StatementVisitor visitor,Formula f) {
		return visitor.visit(this, f);
	}

}
