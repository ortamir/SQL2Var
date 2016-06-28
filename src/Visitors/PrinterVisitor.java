package Visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import AST.StringNode;
import AST.Var;
import AST.Schema.TableDef;

public class PrinterVisitor implements FormulaVisitor {

	boolean collate = false;
	
	public PrinterVisitor() { }
	
	public PrinterVisitor(boolean collate) { 
		this.collate = collate;
	}
	
	@Override
	public FormulaASTNode visit(AndFormula andFormula) {
		StringNode first,second;
		first = (StringNode) andFormula.first.accept(this);
		second = (StringNode) andFormula.second.accept(this);
		
		return new StringNode("(and "+first+" "+second+")");
	}

	@Override
	public FormulaASTNode visit(ExistsFormula existsFormula) {
		StringNode formula = (StringNode) existsFormula.formula.accept(this);
			
		return new StringNode("(exists ("+qvars(existsFormula.variables)+") "+formula+")");
	}

	@Override
	public FormulaASTNode visit(ForAllFormula forAllFormula) {
		StringNode formula = (StringNode) forAllFormula.formula.accept(this);
			
		return new StringNode("(forall ("+qvars(forAllFormula.variables)+") "+formula+")");
	}

	@Override
	public FormulaASTNode visit(IffFormula iffFormula) {
		StringNode first,second;
		first = (StringNode) iffFormula.first.accept(this);
		second = (StringNode) iffFormula.second.accept(this);
		
		return new StringNode("(iff "+first+" "+second+")");
	}

	@Override
	public FormulaASTNode visit(ImpliesFormula impliesFormula) {
		StringNode first,second;
		first = (StringNode) impliesFormula.first.accept(this);
		second = (StringNode) impliesFormula.second.accept(this);
		
		return new StringNode("(=> "+first+" "+second+")");
	}

	@Override
	public FormulaASTNode visit(NegationFormula negationFormula) {
		StringNode formula;
		formula = (StringNode) negationFormula.formula.accept(this);
		
		return new StringNode("(not "+formula+")");
	}

	@Override
	public FormulaASTNode visit(OrFormula orFormula) {
		StringNode first,second;
		first = (StringNode) orFormula.first.accept(this);
		second = (StringNode) orFormula.second.accept(this);
		
		return new StringNode("(or "+first+" "+second+")");
	}

	@Override
	public FormulaASTNode visit(RelationFormula relationFormula) {
		return relationFormula.relation.accept(this);
	}

	@Override
	public FormulaASTNode visit(Relation relation) {
		List<String> variables = new ArrayList<>();
		String suffix = "";
		if (collate && !relation.name.equals("=")) {
			for (Var var : relation.terms) {
				if (var.limit == 0) variables.add(var.accept(this).toString());
				else suffix += "_" + var.name;
			}
		}
		else { 
			for (Var var : relation.terms) {
				variables.add(var.accept(this).toString());
			}
		}
		return new StringNode("("+relation.name+suffix+" "+String.join(" ", variables)+")");
	}

	@Override
	public FormulaASTNode visit(Var var) {
		if (var.limit == 0 || !var.isNumeral())
			return new StringNode(var.name);
		else
			return new StringNode("_" + var.name + "@" + var.limit);
	}

	/**
	 * Auxiliary function to format list of quantified variables.
	 * @param variables
	 * @return
	 */
	static String qvars(Vector<Var> variables) {
		return
		variables.stream().map((var) -> {
			String sort = TableDef.sortForSize(var.limit);
			return "("+var.name+" "+sort+")";
		}).collect(Collectors.joining(" "));
	}

}
