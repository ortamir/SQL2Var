package Visitors;

import AST.*;
import AST.Assignment;
import AST.BoolCondition;
import AST.DeleteQuery;
import AST.EQCondition;
import AST.Formula;
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
import AST.InCondition;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;

public interface QueryVisitor {

	Object visit(MoveQuery moveQuery);
	
	Object visit(DeleteQuery deleteQuery);

	Object visit(SelectQuery selectQuery);
	
	Object visit(UpdateQuery updateQuery);

	Object visit(OneColumn oneColumn);

	Object visit(OrCondition orCondition);

	Object visit(SelectColumn selectColumn);

	Object visit(StarColumns starColumns);

	Object visit(TwoColumns twoColumns);

	Object visit(Value value);

	Object visit(EQCondition eqCondition);

	Object visit(Assignment assignment);

	Object visit(AndCondition andCondition);
	
	Object visit(isNotEmptyCondition isNotEmptyCondition);

	Object visit(isEmptyCondition isEmptyCondition);

	Object visit(InsertQuery insertQuery);
	
	Object visit(InCondition inCondition);

	Object visit(BoolCondition boolCondition);

	Object visit(NEQCondition neqCondition);

	Object visit(NotCondition notCondition);
}
