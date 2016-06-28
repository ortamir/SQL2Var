package Visitors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class MakeOnly2Visitor implements FormulaContextVisitor{

	@Override
	public FormulaASTNode visit(AndFormula andFormula, Object context) {
		Map<String,String> m1 = new HashMap<String,String>(),m2 = new HashMap<String,String>();
		if(context != null){
			m1.putAll((Map<String, String>) context);
			m2.putAll((Map<String, String>) context);
		}
		andFormula.first.accept(this, m1);
		andFormula.second.accept(this, m2);
		return null;
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula, Object context) {
		Map<String,String> m;
		if(context == null)
			m = new HashMap<String,String>();
		else
			m = (Map<String, String>) context;
		
		Iterator<Var> v = existsFormula.variables.iterator();
		Var v1 = v.next();
		if(v1 == null)
			throw new RuntimeException();
		Var v2 = null;
		if(v.hasNext())
				v2 = v.next();
		switch(m.keySet().size()){
		
		case 0: 
			m.put(v1.name, "x");
			if(v2 != null)
				m.put(v2.name, "y");
			break;
		case 1:
			m.put(v1.name, "y");
			if(v2 != null)
				throw new RuntimeException();
			break;
		case 2:
			if(m.containsKey(v1.name) && (v2==null || m.containsKey(v2.name)))
				break;
		default:
			throw new RuntimeException("too many variables! already got two (" + m.keySet() + "), and now '" + v1.name + "'");
		}
		
		v1.accept(this, m);
		if(v2!= null)
			v2.accept(this, m);
		
		existsFormula.formula.accept(this, m);
		
		return null;
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula, Object context) {
		Map<String,String> m;
		if(context == null)
			m = new HashMap<String,String>();
		else
			m = (Map<String, String>) context;
		
		Iterator<Var> v = forAllFormula.variables.iterator();
		Var v1 = v.next();
		if(v1 == null)
			throw new RuntimeException();
		Var v2 = null;
		if(v.hasNext())
				v2 = v.next();
		switch(m.keySet().size()){
		
		case 0: 
			m.put(v1.name, "x");
			if(v2 != null)
				m.put(v2.name, "y");
			break;
		case 1:
			m.put(v1.name, "y");
			if(v2 != null)
				throw new RuntimeException();
			break;
		default:
			throw new RuntimeException("too many variables! already got two (" + m.keySet() + "), and now '" + v1.name + "'");
		}
		
		v1.accept(this, m);
		if(v2!= null)
			v2.accept(this, m);
		
		forAllFormula.formula.accept(this, m);
		
		return null;
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula, Object context) {
		Map<String,String> m1 = new HashMap<String,String>(),m2 = new HashMap<String,String>();
		if(context != null){
			m1.putAll((Map<String, String>) context);
			m2.putAll((Map<String, String>) context);
		}
		iffFormula.first.accept(this, m1);
		iffFormula.second.accept(this,m2);
		return null;
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula, Object context) {
		Map<String,String> m1 = new HashMap<String,String>(),m2 = new HashMap<String,String>();
		if(context != null){
			m1.putAll((Map<String, String>) context);
			m2.putAll((Map<String, String>) context);
		}
		impliesFormula.first.accept(this, m1);
		impliesFormula.second.accept(this,m2);
		return null;
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula, Object context) {
		negationFormula.formula.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula, Object context) {
		Map<String,String> m1 = new HashMap<String,String>(),m2 = new HashMap<String,String>();
		if(context != null){
			m1.putAll((Map<String, String>) context);
			m2.putAll((Map<String, String>) context);
		}
		orFormula.first.accept(this, m1);
		orFormula.second.accept(this,m2);
		return null;
	}

	@Override
	public FormulaASTNode visit(RelationFormula relationFormula, Object context) {
		relationFormula.relation.accept(this, context);
		return null;
	}

	@Override
	public FormulaASTNode visit(Relation relation, Object context) {
		for(Iterator<Var> i =relation.terms.iterator();i.hasNext();){
			i.next().accept(this,context);
		}
		return null;
	}

	@Override
	public FormulaASTNode visit(Var var, Object context) {
		
		Map<String,String> m = (Map<String, String>) context;
		if(m== null)
			return null;
		if(m.containsKey(var.name))
			var.name = m.get(var.name);
		
		return null;
	}

}
