package AST;

import java.util.Map;
import java.util.Vector;

import AST.Schema.TableDef;

public class Program {
	
	public Formula preCondition,postCondition;
	public Vector<Stmt> stmts;
	
	public Program(Map<String, TableDef> t, Formula f1, Vector<Stmt> sl, Formula f2) {
		// TODO Auto-generated constructor stub
		preCondition = f1;
		stmts = sl;
		postCondition = f2;
		if(t.keySet().size() > 0)
			Schema.tables = t;
	}

}
