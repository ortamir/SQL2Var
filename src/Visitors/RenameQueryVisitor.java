package Visitors;

import java.util.Map;
import java.util.Vector;

import AST.AndCondition;
import AST.Assignment;
import AST.Columns;
import AST.Condition;
import AST.DeleteQuery;
import AST.EQCondition;
import AST.InsertQuery;
import AST.MoveQuery;
import AST.OneColumn;
import AST.OrCondition;
import AST.SelectColumn;
import AST.SelectQuery;
import AST.StarColumns;
import AST.TwoColumns;
import AST.UpdateQuery;
import AST.Value;
import AST.Var;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;

public class RenameQueryVisitor extends CloneQueryVisitor {
	
	public Map<Var,Var> map;
	
	public RenameQueryVisitor(Map<Var,Var> m){
		map = m;
	}
	

	@Override
	public Object visit(EQCondition eqCondition) {
		Var val = eqCondition.value;
		if(map.containsKey(val))
			val = map.get(val);
		return new EQCondition(eqCondition.column,val);
	}

	@Override
	public Object visit(Assignment assignment) {
		Var val = assignment.value;
		if(map.containsKey(val))
			val = map.get(val);
		return new Assignment(assignment.column,val);
	}

	
	@Override
	public Object visit(InsertQuery insertQuery) {
		Vector<Var> vec = insertQuery.terms;
		for(int i=0;i<vec.size();i++){
			Var v = vec.elementAt(i);
			if(map.containsKey(v))
				v = map.get(v);
		}

		return new InsertQuery(insertQuery.table,(Vector<Var>) vec.clone());
	}

}
