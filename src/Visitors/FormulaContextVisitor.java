package Visitors;

import AST.AndFormula;
import AST.ExistsFormula;
import AST.ForAllFormula;
import AST.FormulaASTNode;
import AST.IffFormula;
import AST.ImpliesFormula;
import AST.NegationFormula;
import AST.OrFormula;
import AST.Relation;
import AST.RelationFormula;
import AST.Var;

public interface FormulaContextVisitor {

	FormulaASTNode visit(AndFormula andFormula,Object context);

	FormulaASTNode visit(ExistsFormula existsFormula,Object context);

	FormulaASTNode visit(ForAllFormula forAllFormula,Object context);

	FormulaASTNode visit(IffFormula iffFormula,Object context);

	FormulaASTNode visit(ImpliesFormula impliesFormula,Object context);

	FormulaASTNode visit(NegationFormula negationFormula,Object context);

	FormulaASTNode visit(OrFormula orFormula,Object context);

	FormulaASTNode visit(RelationFormula realationFormula,Object context);

	FormulaASTNode visit(Relation relation,Object context);

	FormulaASTNode visit(Var var,Object context);
}
