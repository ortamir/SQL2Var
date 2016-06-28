package AST;

import java.util.regex.Pattern;

import Visitors.FormulaContextVisitor;
import Visitors.FormulaVisitor;

public class Var extends FormulaASTNode{
	public String name;
	public boolean isTied;
	public int limit;
	
	public Var(String id) {
		name = id;
		isTied = false;
		limit = 0;
	}
	
	public Var(String id, int limit) {
		name = id;
		isTied = false;
		this.limit = limit;
	}
	
	public Var(int i) {
		name = ""+i;
		isTied = false;
		limit = 0;
	}
	
	public Var(int i, int limit) {
		name = ""+i;
		isTied = false;
		this.limit = limit;
	}

	@Override
	public FormulaASTNode accept(FormulaVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object that) {
		// TODO Auto-generated method stub
		if(that instanceof Var)
			return ((Var)that).name.equals(this.name);
		return false;//super.equals(arg0);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return name.hashCode();
	}

	@Override
	public String toString() {
		if (limit == 0)
			return name;
		else
			return name + ":" + limit;
	}
	
	@Override
	public FormulaASTNode accept(FormulaContextVisitor visitor, Object context) {
		// TODO Auto-generated method stub
		return visitor.visit(this,context);
	}

	public boolean isNumeral() {
		return Pattern.matches("\\d+", name);
	}
	
}
