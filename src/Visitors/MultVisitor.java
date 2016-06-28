package Visitors;

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

public class MultVisitor extends CloneVisitor {

	public int m;

	public MultVisitor(int m){
		this.m =m;
	}
	
	@Override
	public FormulaASTNode visit(NegationFormula negationFormula) {
		if(negationFormula.formula instanceof RelationFormula){
			Relation relation = ((RelationFormula)negationFormula.formula).relation;
			Var v1 = relation.terms.elementAt(0);
			Formula f;
			switch(relation.terms.size()){
			case 0:
				throw new RuntimeException();
			case 1:
				if(v1.isTied){
					Vector<Var> v = new Vector<Var>();
					for(int i=0;i<relation.terms.size();i++){
						v.add((Var) relation.terms.get(i).accept(this));
					}
					return new NegationFormula(new RelationFormula(new Relation(relation.name,v)));
				}
				
				f = new NegationFormula(new RelationFormula(relation));
				for(int i=2;i<=m;i++){
					Vector<Var> vec = new Vector<>();
					vec.addElement(new Var(v1.name+i));
					f = new AndFormula(f,new NegationFormula(new RelationFormula(new Relation(relation.name,vec))));
				}
				return f;
			case 2:
				Var v2 = relation.terms.elementAt(1);
				if(v1.isTied && v2.isTied){
					Vector<Var> v = new Vector<Var>();
					for(int i=0;i<relation.terms.size();i++){
						v.add((Var) relation.terms.get(i).accept(this));
					}
					return new NegationFormula(new RelationFormula(new Relation(relation.name,v)));
				}
				if(v1.isTied){
					f = new NegationFormula(new  RelationFormula(relation));
					for(int i=2;i<=m;i++){
						Vector<Var> vec = new Vector<>();
						vec.addElement(new Var(v1.name));
						vec.addElement(new Var(v2.name+i));
						f = new AndFormula(f,new NegationFormula(new  RelationFormula(new Relation(relation.name,vec))));
					}
					return f;
				}
				if(v2.isTied){
					f = new NegationFormula(new  RelationFormula(relation));
					for(int i=2;i<=m;i++){
						Vector<Var> vec = new Vector<>();
						vec.addElement(new Var(v1.name+i));
						vec.addElement(new Var(v2.name));
						f = new AndFormula(f,new NegationFormula(new  RelationFormula(new Relation(relation.name,vec))));
					}
					return f;
				}
				f = new NegationFormula(new RelationFormula(relation));
				for(int i=2;i<=m;i++){
					Vector<Var> vec = new Vector<>();
					vec.addElement(new Var(v1.name+i));
					vec.addElement(new Var(v2.name+i));
					f = new AndFormula(f,new NegationFormula(new  RelationFormula(new Relation(relation.name,vec))));
				}
				return f;	
			default:
				throw new RuntimeException();
			}
		}
		else
			return new NegationFormula((Formula)negationFormula.formula.accept(this));
	}
}
