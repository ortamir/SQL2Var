package Visitors;

import java.util.Iterator;
import java.util.Vector;

import AST.AndFormula;
import AST.ExistsFormula;
import AST.ForAllFormula;
import AST.Formula;
import AST.FormulaASTNode;
import AST.IffFormula;
import AST.ImpliesFormula;
import AST.NegationFormula;
import AST.OrFormula;
import AST.Relation;
import AST.RelationFormula;
import AST.Var;

public class CloneVisitor implements FormulaVisitor{

	@Override
	public FormulaASTNode visit(AndFormula andFormula) {
		return new AndFormula((Formula)andFormula.first.accept(this),(Formula)andFormula.second.accept(this));
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula) {
		Vector<Var> vec = new Vector<Var>();
		for(Iterator<Var> it=existsFormula.variables.iterator();it.hasNext();){
			vec.addElement((Var) it.next().accept(this));
		}
		return new ExistsFormula(vec,(Formula)existsFormula.formula.accept(this));
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula) {
		Vector<Var> vec = new Vector<Var>();
		for(Iterator<Var> it=forAllFormula.variables.iterator();it.hasNext();){
			vec.addElement((Var) it.next().accept(this));
		}
		return new ForAllFormula(vec,(Formula)forAllFormula.formula.accept(this));
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula) {
		return new IffFormula((Formula)iffFormula.first.accept(this),(Formula)iffFormula.second.accept(this));
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula) {
		return new ImpliesFormula((Formula)impliesFormula.first.accept(this),(Formula)impliesFormula.second.accept(this));
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula) {
		return new NegationFormula((Formula)negationFormula.formula.accept(this));
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula) {
		return new OrFormula((Formula)orFormula.first.accept(this),(Formula)orFormula.second.accept(this));
	}

	@Override
	public FormulaASTNode visit(RelationFormula relationFormula) {
		return new RelationFormula((Relation)relationFormula.relation.accept(this));
	}

	@Override
	public FormulaASTNode visit(Relation relation) {
		Vector<Var> v = new Vector<Var>();
		for(int i=0;i<relation.terms.size();i++){
			v.add((Var) relation.terms.get(i).accept(this));
		}
		return new Relation(relation.name,v);
	}

	@Override
	public FormulaASTNode visit(Var var) {
		return new Var(var.name,var.limit);
	}

}
