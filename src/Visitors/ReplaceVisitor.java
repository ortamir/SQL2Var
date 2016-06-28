package Visitors;

import java.util.HashMap;
import java.util.Map;

import AST.Formula;
import AST.FormulaASTNode;
import AST.RelationFormula;
import AST.Schema;
import AST.Schema.TableDef;
import AST.Var;

public class ReplaceVisitor extends CloneVisitor{

	public String relationName;
	public Formula dst;



	public ReplaceVisitor(String relationName, Formula dst) {
		this.relationName = relationName;
		this.dst = dst;
	}



	@Override
	public FormulaASTNode visit(RelationFormula relationFormula) {
		if(relationFormula.relation.name.equals(relationName)){
			CloneVisitor c = new CloneVisitor();
			Formula f = (Formula) dst.accept(c);
			Map<Var,Var> m = new HashMap<Var,Var>();
			
			TableDef t = Schema.tables.get(relationName);
			int s = 0;
			if(t.boundedColumns != null)
				s=t.boundedColumns.size();
			if(s+t.numColumns != relationFormula.relation.terms.size())
				throw new RuntimeException("Unmactched table "+relationName+" arguments got:"+relationFormula.relation.terms.size()+" excepected:"+s+t.numColumns);
			switch(t.numColumns){
			case 1:
				m.put(new Var("alpha"), relationFormula.relation.terms.get(0));
				for(int i=1;i<relationFormula.relation.terms.size();i++){
					int d=i-1;
					m.put(new Var("l"+d), relationFormula.relation.terms.get(i));
				}
				break;
				
			case 2:
				m.put(new Var("alpha"), relationFormula.relation.terms.get(0));
				m.put(new Var("beta"), relationFormula.relation.terms.get(1));
				for(int i=2;i<relationFormula.relation.terms.size();i++){
					int d = i-2;
					m.put(new Var("l"+d), relationFormula.relation.terms.get(i));
				}
				break;
			}

			RenameVisitor rv = new RenameVisitor(m);


			return f.accept(rv) ;//new RelationFormula((Relation)relationFormula.relation.accept(this));
		}
		else
			return super.visit(relationFormula);
	}

}
