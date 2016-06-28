package Visitors;

import AST.*;

public interface StatementVisitor {

	Formula visit(QuerySTMT querySTMT,Formula f);

	Formula visit(AssignSTMT assignSTMT,Formula f);

	Formula visit(IfSTMT ifSTMT,Formula f);

	Formula visit(ChooseSTMT chooseSTMT,Formula f);

	Formula visit(SkipStmt skipStmt, Formula q);

}
