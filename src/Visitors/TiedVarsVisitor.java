package Visitors;

import java.util.Iterator;
import java.util.Set;

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

public class TiedVarsVisitor implements FormulaContextVisitor{

	@Override
	public FormulaASTNode visit(AndFormula andFormula, Object context) {
		andFormula.first.accept(this, context);
		andFormula.second.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula, Object context) {
		Set<Var> c = (Set<Var>)context;
		c.addAll(existsFormula.variables);
		existsFormula.formula.accept(this, c);
		return null;
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula, Object context) {
		Set<Var> c = (Set<Var>)context;
		c.addAll(forAllFormula.variables);
		forAllFormula.formula.accept(this, c);
		return null;
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula, Object context) {
		iffFormula.first.accept(this, context);
		iffFormula.second.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula, Object context) {
		impliesFormula.first.accept(this, context);
		impliesFormula.second.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula, Object context) {
		negationFormula.formula.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula, Object context) {
		orFormula.first.accept(this, context);
		orFormula.second.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(RelationFormula realationFormula, Object context) {
		realationFormula.relation.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(Relation relation, Object context) {
		
		for(Iterator<Var> i=relation.terms.iterator();i.hasNext();){
			i.next().accept(this, context);
		}
		return null;
	}

	@Override
	public FormulaASTNode visit(Var var, Object context) {
		Set<Var> c = (Set<Var>)context;
		if(c.contains(var))
			var.isTied = true;
		
		return null;
	}

}
