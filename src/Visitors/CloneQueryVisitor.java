package Visitors;

import java.util.Vector;

import AST.AndCondition;
import AST.Assignment;
import AST.BoolCondition;
import AST.Columns;
import AST.Condition;
import AST.DeleteQuery;
import AST.EQCondition;
import AST.Formula;
import AST.InsertQuery;
import AST.MoveQuery;
import AST.NEQCondition;
import AST.OneColumn;
import AST.OrCondition;
import AST.Query;
import AST.SelectColumn;
import AST.SelectQuery;
import AST.StarColumns;
import AST.TwoColumns;
import AST.UpdateQuery;
import AST.Value;
import AST.Var;
import AST.inCondition;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;

public class CloneQueryVisitor implements QueryVisitor {

	@Override
	public Object visit(MoveQuery moveQuery) {
		return new MoveQuery(moveQuery.table,(Columns) moveQuery.columns.accept(this),moveQuery.table2,(Columns) moveQuery.columns2.accept(this),(Condition) moveQuery.condition.accept(this));
	}

	@Override
	public Object visit(DeleteQuery deleteQuery) {
		return new DeleteQuery(deleteQuery.table,(Condition) deleteQuery.condition.accept(this));
	}

	@Override
	public Object visit(SelectQuery selectQuery) {
		return new SelectQuery(selectQuery.table,(Columns)selectQuery.columns.accept(this),selectQuery.table,(Condition) selectQuery.condition.accept(this));
	}

	@Override
	public Object visit(UpdateQuery updateQuery) {
		return new UpdateQuery(updateQuery.table,(Assignment) updateQuery.assignment.accept(this),(Condition) updateQuery.condition.accept(this));
	}

	@Override
	public Object visit(OneColumn oneColumn) {
		return new OneColumn(oneColumn.column);
	}

	@Override
	public Object visit(OrCondition orCondition) {
		return new OrCondition((Condition)orCondition.first.accept(this),(Condition)orCondition.second.accept(this));
	}

	@Override
	public Object visit(SelectColumn selectColumn) {
		return new SelectColumn(selectColumn.table,selectColumn.column,selectColumn.table,(Condition) selectColumn.condition.accept(this));
	}

	@Override
	public Object visit(StarColumns starColumns) {
		return new StarColumns();
	}

	@Override
	public Object visit(TwoColumns twoColumns) {
		return new TwoColumns(twoColumns.first,twoColumns.second);
	}

	@Override
	public Object visit(Value value) {
		return null;
	}

	@Override
	public Object visit(EQCondition eqCondition) {
		// TODO Auto-generated method stub
		return new EQCondition(eqCondition.column,eqCondition.value);
	}

	@Override
	public Object visit(Assignment assignment) {
		return new Assignment(assignment.column,assignment.value);
	}

	@Override
	public Object visit(AndCondition andCondition) {
		return new AndCondition((Condition)andCondition.first.accept(this),(Condition)andCondition.second.accept(this));
	}

	@Override
	public Object visit(isNotEmptyCondition is) {
		return new isNotEmptyCondition((SelectColumn) is.selectColumn.accept(this));
	}

	@Override
	public Object visit(isEmptyCondition is) {
		return new isEmptyCondition((SelectColumn) is.selectColumn.accept(this));
	}

	@Override
	public Object visit(InsertQuery insertQuery) {
		return new InsertQuery(insertQuery.table,(Vector<Var>) insertQuery.terms.clone());
	}

	@Override
	public Object visit(inCondition inCondition) {
		return new inCondition(inCondition.column,(Query)inCondition.sc.accept(this));
	}

	@Override
	public Object visit(BoolCondition boolCondition) {
		return new BoolCondition(boolCondition.c);
	}

	@Override
	public Object visit(NEQCondition neqCondition) {
		// TODO Auto-generated method stub
		return new NEQCondition(neqCondition.column,neqCondition.value);
	}

}
