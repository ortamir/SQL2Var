package AST;

import java.util.Map;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class TypeVarsNode extends Formula {

	public Map<String,Integer> map;
	
	public TypeVarsNode(Map<String,Integer> m){
		map=m;
	}
	
	@Override
	public FormulaASTNode accept(FormulaVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaASTNode accept(FormulaContextVisitor visitor, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

}
