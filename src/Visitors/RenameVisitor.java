package Visitors;

import java.util.Map;

import AST.FormulaASTNode;
import AST.Var;

public class RenameVisitor extends CloneVisitor{
	
	public Map<Var,Var> map;
	
	public RenameVisitor(Map<Var,Var> m){
		map = m;
	}
	
	public FormulaASTNode visit(Var var) {
		
		if(map.get(var) != null){
			if(var.isTied)
				throw new RuntimeException("cannot rename bound variable '" + var.name + "'");
			return super.visit(map.get(var));
		}
		else
			return super.visit(var);
	}
}
