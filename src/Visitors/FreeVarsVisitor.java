package Visitors;

import java.util.HashSet;
import java.util.Set;

import AST.AndFormula;
import AST.ExistsFormula;
import AST.ForAllFormula;
import AST.FormulaASTNode;
import AST.FreeVarsNode;
import AST.IffFormula;
import AST.ImpliesFormula;
import AST.NegationFormula;
import AST.OrFormula;
import AST.Relation;
import AST.RelationFormula;
import AST.Var;

public class FreeVarsVisitor implements FormulaVisitor{

	@Override
	public FormulaASTNode visit(AndFormula andFormula) {
		FreeVarsNode first,second;
		first = (FreeVarsNode) andFormula.first.accept(this);
		second = (FreeVarsNode) andFormula.second.accept(this);
		
		first.set.addAll(second.set);
		
		return first;
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula) {
		FreeVarsNode formula;
		formula = (FreeVarsNode) existsFormula.formula.accept(this);
		formula.set.removeAll(existsFormula.variables);
		return formula;
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula) {
		FreeVarsNode formula;
		formula = (FreeVarsNode) forAllFormula.formula.accept(this);
		formula.set.removeAll(forAllFormula.variables);
		return formula;
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula) {
		FreeVarsNode first,second;
		first = (FreeVarsNode) iffFormula.first.accept(this);
		second = (FreeVarsNode) iffFormula.second.accept(this);
		
		first.set.addAll(second.set);
		
		return first;
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula) {
		FreeVarsNode first,second;
		first = (FreeVarsNode) impliesFormula.first.accept(this);
		second = (FreeVarsNode) impliesFormula.second.accept(this);
		
		first.set.addAll(second.set);
		
		return first;
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula) {

		return negationFormula.formula.accept(this);
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula) {
		FreeVarsNode first,second;
		first = (FreeVarsNode) orFormula.first.accept(this);
		second = (FreeVarsNode) orFormula.second.accept(this);
		
		first.set.addAll(second.set);
		
		return first;
	}

	@Override
	public FormulaASTNode visit(RelationFormula realationFormula) {
		return realationFormula.relation.accept(this);
	}

	@Override
	public FormulaASTNode visit(Relation relation) {
		Set<Var> s = new HashSet<Var>(relation.terms);
		return new FreeVarsNode(s);
	}

	@Override
	public FormulaASTNode visit(Var var) {
		Set<Var> s = new HashSet<Var>();
		s.add(var);
		return new FreeVarsNode(s);
	}
	
	

}
