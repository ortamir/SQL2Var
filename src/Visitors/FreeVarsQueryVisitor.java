package Visitors;

import java.util.HashMap;
import java.util.HashSet;

import AST.AndCondition;
import AST.Assignment;
import AST.BoolCondition;
import AST.DeleteQuery;
import AST.EQCondition;
import AST.Formula;
import AST.FreeVarsNode;
import AST.InsertQuery;
import AST.MoveQuery;
import AST.NEQCondition;
import AST.NotCondition;
import AST.OneColumn;
import AST.OrCondition;
import AST.SelectColumn;
import AST.SelectQuery;
import AST.StarColumns;
import AST.TwoColumns;
import AST.UpdateQuery;
import AST.Value;
import AST.Var;
import AST.InCondition;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;

public class FreeVarsQueryVisitor implements QueryVisitor {

	@Override
	public Object visit(MoveQuery moveQuery) {
		return moveQuery.condition.accept(this);
	}

	@Override
	public Object visit(DeleteQuery deleteQuery) {
		return deleteQuery.condition.accept(this);
	}

	@Override
	public Object visit(SelectQuery selectQuery) {
		return selectQuery.condition.accept(this);
	}

	@Override
	public Object visit(UpdateQuery updateQuery) {
		FreeVarsNode f = (FreeVarsNode)updateQuery.assignment.accept(this);
		f.set.addAll(((FreeVarsNode)updateQuery.condition.accept(this)).set);
		return f;
	}

	@Override
	public Object visit(OneColumn oneColumn) {
		return null;
	}

	@Override
	public Object visit(OrCondition orCondition) {
		FreeVarsNode f = (FreeVarsNode) orCondition.first.accept(this);
		f.set.addAll(((FreeVarsNode)orCondition.second.accept(this)).set);
		return f;
	}

	@Override
	public Object visit(SelectColumn selectColumn) {
		return selectColumn.condition.accept(this);
	}

	@Override
	public Object visit(StarColumns starColumns) {
		return null;
	}

	@Override
	public Object visit(TwoColumns twoColumns) {
		return null;
	}

	@Override
	public Object visit(Value value) {
		return null;
	}

	@Override
	public Object visit(EQCondition eqCondition) {
		FreeVarsNode f = new FreeVarsNode(new HashSet<>());
		f.set.add(eqCondition.value);
		return f;
	}
	
	@Override
	public Object visit(NEQCondition neqCondition) {
		FreeVarsNode f = new FreeVarsNode(new HashSet<>());
		f.set.add(neqCondition.value);
		return f;
	}

	@Override
	public Object visit(Assignment assignment) {
		FreeVarsNode f = new FreeVarsNode(new HashSet<>());
		f.set.add(assignment.value);
		return f;
	}

	@Override
	public Object visit(AndCondition andCondition) {
		FreeVarsNode f = (FreeVarsNode) andCondition.first.accept(this);
		f.set.addAll(((FreeVarsNode)andCondition.second.accept(this)).set);
		return f;
	}

	@Override
	public Object visit(isNotEmptyCondition isNotEmptyCondition) {
		return isNotEmptyCondition.selectColumn.accept(this);
	}

	@Override
	public Object visit(isEmptyCondition isEmptyCondition) {
		return isEmptyCondition.selectColumn.accept(this);
	}

	@Override
	public Object visit(InsertQuery insertQuery) {
		FreeVarsNode f = new FreeVarsNode(new HashSet<>());
		f.set.addAll(insertQuery.terms);
		return f;
	}

	@Override
	public Object visit(InCondition inCondition) {
		FreeVarsNode f = (FreeVarsNode)inCondition.sc.accept(this);
		return f.set.remove(new Var("Column1"));
	}

	@Override
	public Object visit(BoolCondition boolCondition) {
		// TODO Auto-generated method stub
		FreeVarsNode f = new FreeVarsNode(new HashSet<>());
		return f;
	}

	@Override
	public Object visit(NotCondition notCondition) {
		return (FreeVarsNode) notCondition.cond.accept(this);
	}

}
