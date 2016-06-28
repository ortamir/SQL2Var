package Visitors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import AST.AndFormula;
import AST.ExistsFormula;
import AST.ForAllFormula;
import AST.Formula;
import AST.FormulaASTNode;
import AST.OrFormula;
import AST.Var;

public class ReleaseBoundVarVisitor extends CloneVisitor implements
		FormulaVisitor {
	
	
	Formula blast(Formula f, Var v, BinaryOperator<Formula> connective) {
		Formula ret = null;
		
		for(int i=0;i<v.limit;i++){
			Formula copy=(Formula) f.accept(new CloneVisitor());
			Map<Var,Var> map = new HashMap<Var,Var>();
			map.put(v, new Var(i, v.limit));
			copy = (Formula) copy.accept(new RenameVisitor(map));
			ret = (ret == null) ? copy : connective.apply(ret, copy);
		}
		return ret;
	}
	
	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula) {
		Formula base;
		
		if(existsFormula.variables.size() == 1){
			Var v = existsFormula.variables.elementAt(0);
			base = (Formula) existsFormula.formula.accept(this);
			if(v.limit == 0){
				return new ExistsFormula((Vector<Var>)existsFormula.variables.clone(),base);
			}
			else {
				return blast(base, v, (x,y) -> new OrFormula(x,y)); 
			}
		}
		Formula ret = (Formula) existsFormula.formula.accept(new CloneVisitor());
		Vector<Var> vec = existsFormula.variables;
		for(int i=vec.size()-1;i>=0;i--){
			Vector<Var> one=new Vector<>();
			Var v = vec.elementAt(i);
			one.add(v);
			ret = new ExistsFormula(one, ret);
		}
		return ret.accept(this);
	}
	
	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula) {
		Formula base;
		
		if(forAllFormula.variables.size() == 1){
			Var v = forAllFormula.variables.elementAt(0);
			base = (Formula) forAllFormula.formula.accept(this);
			if(v.limit == 0){
				return new ForAllFormula((Vector<Var>)forAllFormula.variables.clone(),base);
			}
			else {
				return blast(base, v, (x,y) -> new AndFormula(x, y));
			}
		}
		Formula ret = (Formula) forAllFormula.formula.accept(new CloneVisitor());
		Vector<Var> vec = forAllFormula.variables;
		for(int i=vec.size()-1;i>=0;i--){
			Vector<Var> one=new Vector<>();
			Var v = vec.elementAt(i);
			one.add(v);
			ret = new ForAllFormula(one, ret);
		}
		return ret.accept(this);
		/*
		Vector<Var> vec = (Vector<Var>) forAllFormula.variables.clone(),v2 = new Vector<Var>();
		Var v = vec.remove(vec.size()-1);
		v2.add(v);
		
		Formula f = new ForAllFormula(v2,forAllFormula.formula);
		f = (Formula) f.accept(this);
		Formula f2 = new ForAllFormula(vec,f);
		return f2.accept(this);*/
	}

}
