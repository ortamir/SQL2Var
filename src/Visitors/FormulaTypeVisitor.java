package Visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

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
import AST.Schema;
import AST.Schema.TableDef;
import AST.TypeVarsNode;
import AST.Var;

public class FormulaTypeVisitor implements FormulaVisitor {

	@Override
	public FormulaASTNode visit(AndFormula andFormula) {
		TypeVarsNode f = (TypeVarsNode) andFormula.first.accept(this);
		TypeVarsNode f2 = (TypeVarsNode) andFormula.second.accept(this);
		f.map.putAll(f2.map);
		return f;
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula) {
		TypeVarsNode f = (TypeVarsNode)existsFormula.formula.accept(this);
		Map<String,Integer> m = f.map;
		Vector<Var> vec=existsFormula.variables;
		for(int i=0;i<vec.size();i++){
			Var v = vec.elementAt(i);
			if(m.containsKey(v.name)){
				int lim = m.get(v.name);
				v.limit = lim;
				m.remove(v.name);
			}
		}
		return f;
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula) {
		TypeVarsNode f = (TypeVarsNode)forAllFormula.formula.accept(this);
		Map<String,Integer> m = f.map;
		Vector<Var> vec=forAllFormula.variables;
		for(int i=0;i<vec.size();i++){
			Var v = vec.elementAt(i);
			if(m.containsKey(v.name)){
				int lim = m.get(v.name);
				v.limit = lim;
				m.remove(v.name);
			}
		}
		return f;
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula) {
		TypeVarsNode f = (TypeVarsNode) iffFormula.first.accept(this);
		TypeVarsNode f2 = (TypeVarsNode) iffFormula.second.accept(this);
		f.map.putAll(f2.map);
		return f;
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula) {
		TypeVarsNode f = (TypeVarsNode) impliesFormula.first.accept(this);
		TypeVarsNode f2 = (TypeVarsNode) impliesFormula.second.accept(this);
		f.map.putAll(f2.map);
		return f;
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula) {
		TypeVarsNode f = (TypeVarsNode) negationFormula.formula.accept(this);
		return f;
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula) {
		TypeVarsNode f = (TypeVarsNode) orFormula.first.accept(this);
		TypeVarsNode f2 = (TypeVarsNode) orFormula.second.accept(this);
		f.map.putAll(f2.map);
		return f;
	}

	@Override
	public FormulaASTNode visit(RelationFormula realationFormula) {
		return realationFormula.relation.accept(this);
	}

	@Override
	public FormulaASTNode visit(Relation relation) {
		Map<String,Integer> m= new HashMap<String,Integer>();
		TypeVarsNode f = new TypeVarsNode(m);

		if (relation.name.equals("=")) {
			int limit = 0;
			for (Var v : relation.terms) {
				if (v.limit > 0) {
					if (limit > 0 && limit != v.limit) throw new RuntimeException("type mismatch in '= " + relation.terms.stream().map(Var::toString).collect(Collectors.joining(" ")) + "'");
					else limit = v.limit;
				}
			}
			if (limit > 0) {
				for (Var v : relation.terms) {
					v.limit = limit;
					m.put(v.name, limit);
				}
			}
		}
		else {
			
			TableDef td = Schema.tables.get(relation.name);
			
			if(td == null) //Unrecognized table
				return f;
			
			int i=td.numColumns;
			int c=0;
			while(i<relation.terms.size()){
				Var v = relation.terms.elementAt(i);
				v.limit=td.boundedColumns.elementAt(c);
				m.put(v.name,v.limit);
				c++;
				i++;
			}
		}
		
		return f;
	}

	@Override
	public FormulaASTNode visit(Var var) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
