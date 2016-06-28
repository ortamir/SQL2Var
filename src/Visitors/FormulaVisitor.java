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

public interface FormulaVisitor {

	FormulaASTNode visit(AndFormula andFormula);

	FormulaASTNode visit(ExistsFormula existsFormula);

	FormulaASTNode visit(ForAllFormula forAllFormula);

	FormulaASTNode visit(IffFormula iffFormula);

	FormulaASTNode visit(ImpliesFormula impliesFormula);

	FormulaASTNode visit(NegationFormula negationFormula);

	FormulaASTNode visit(OrFormula orFormula);

	FormulaASTNode visit(RelationFormula realationFormula);

	FormulaASTNode visit(Relation relation);

	FormulaASTNode visit(Var var);
	
	

}
