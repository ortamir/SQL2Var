package Visitors;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import AST.AndCondition;
import AST.AndFormula;
import AST.Assignment;
import AST.BoolCondition;
import AST.DeleteQuery;
import AST.EQCondition;
import AST.ExistsFormula;
import AST.Formula;
import AST.FreeVarsNode;
import AST.IdGenerator;
import AST.InsertQuery;
import AST.MoveQuery;
import AST.NEQCondition;
import AST.NegationFormula;
import AST.NotCondition;
import AST.OneColumn;
import AST.OrCondition;
import AST.OrFormula;
import AST.Relation;
import AST.RelationFormula;
import AST.Schema;
import AST.SelectColumn;
import AST.SelectQuery;
import AST.StarColumns;
import AST.TwoColumns;
import AST.UpdateQuery;
import AST.Value;
import AST.Var;
import AST.InCondition;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;



public class ConditionVisitor implements QueryVisitor{

	@Override
	public Object visit(MoveQuery moveQuery) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(DeleteQuery deleteQuery) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(SelectQuery selectQuery) {
		if (selectQuery.columns instanceof OneColumn) {
			return visit(
				new SelectColumn(selectQuery.table, ((OneColumn)selectQuery.columns).column, null /* TODO this does not seem to be used? */, selectQuery.condition));
		}
		else {
			// TODO Auto-generated method stub
			throw new RuntimeException("not implemented");
		}
	}

	@Override
	public Object visit(UpdateQuery updateQuery) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(OneColumn oneColumn) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(OrCondition orCondition) {
		// TODO Auto-generated method stub
		return new OrFormula((Formula)orCondition.first.accept(this),(Formula)orCondition.second.accept(this));
	}

	@Override
	public Object visit(SelectColumn selectColumn) {
		
		Vector<Var> vs = new Vector<>();

		//int numColums = checkTableArity(selectColumn.table);
		
		switch(Schema.checkTableArity(selectColumn.table)){
		case 1:
			vs.add(new Var("alpha"));
		break;
		case 2:
			vs.add(new Var("alpha"));
			vs.add(new Var("beta"));
		break;
		}
		Utils.addLimitedVariables(selectColumn.table,vs);
		return new AndFormula((Formula) selectColumn.condition.accept(this),new RelationFormula(new Relation(selectColumn.table,vs)));
	}

	

	@Override
	public Object visit(StarColumns starColumns) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(TwoColumns twoColumns) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(Value value) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(EQCondition eqCondition) {
		// TODO Auto-generated method stub
		Vector<Var> vec = new Vector<Var>();
		switch(eqCondition.column){
		case "Column1":
			vec.add(new Var("alpha"));
		break;
		case "Column2":
			vec.add(new Var("beta"));
		break;
		default:
			if(eqCondition.column.startsWith("b"))// bounded column
				vec.add(new Var("l"+eqCondition.column.substring(1, eqCondition.column.length())));
			else
				throw new RuntimeException("undefined column "+eqCondition.column);
			
		
		}
		vec.add(eqCondition.value);
		return new RelationFormula(new Relation("=",vec));
		
	}

	@Override
	public Object visit(Assignment assignment) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(AndCondition andCondition) {
		// TODO Auto-generated method stub
		return new AndFormula((Formula)andCondition.first.accept(this),(Formula)andCondition.second.accept(this));
	}

	@Override
	public Object visit(isNotEmptyCondition isNotEmptyCondition) {
		
		SelectColumn selectColumn = isNotEmptyCondition.selectColumn;
		return notEmptyConditionFormula(selectColumn);
	}

	

	@Override
	public Object visit(isEmptyCondition isEmptyCondition) {
		
		SelectColumn selectColumn = isEmptyCondition.selectColumn;
		return new NegationFormula((Formula) notEmptyConditionFormula(selectColumn));
	}

	private Object notEmptyConditionFormula(SelectColumn selectColumn) {
		Var v1 = new Var(IdGenerator.getFreshId());
		Var v2 = new Var(IdGenerator.getFreshId());
		Formula f = (Formula) selectColumn.accept(this);
		Map<Var,Var> m = new HashMap<>();
		m.put(new Var("alpha"), v1);
		m.put(new Var("beta"), v2);
		
		Vector<Var> vs = new Vector<>();
		switch(Schema.checkTableArity(selectColumn.table))
		{
		case 1:
			vs.add(v1);
			break;
		case 2:
			vs.add(v1);
			vs.add(v2);
			break;
		}
		
		Vector<Integer> boundedDomains = Schema.getTableBoundedColumns(selectColumn.table);
		for(int i=0; i < boundedDomains.size(); i++){
			int limit = boundedDomains.get(i);
			Var v = new Var(IdGenerator.getFreshId(), limit);
			m.put(new Var("l"+i, limit), v);
			vs.add(v);
		}
		
		Formula f2 = new ExistsFormula(vs, f); 
		f2 = (Formula) f2.accept(new RenameVisitor(m));
		
		return f2;
	}

	@Override
	public Object visit(InsertQuery insertQuery) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object visit(InCondition inCondition) {
		
		Var column;
		
		switch(inCondition.column){
		case "Column1":
			column = new Var("alpha");
		break;
		case "Column2":
			column = new Var("beta");
		break;
		default:
			if(inCondition.column.startsWith("b"))// bounded column
				column = new Var("l"+inCondition.column.substring(1, inCondition.column.length()));
			else
				throw new RuntimeException("undefined column "+inCondition.column);
			
		
		}
		
		Formula f = (Formula) inCondition.sc.accept(this);
		Var v = ((FreeVarsNode)f.accept(new FreeVarsVisitor())).set.iterator().next();
		Map<Var,Var> m = new HashMap<Var,Var>();
		
		m.put(v, column);
		
		return f.accept(new RenameVisitor(m));
	}

	@Override
	public Object visit(BoolCondition boolCondition) {
		// TODO Auto-generated method stub
		return new BoolCondition(boolCondition.c);
	}

	@Override
	public Object visit(NEQCondition neqCondition) {
		// TODO Auto-generated method stub
				Vector<Var> vec = new Vector<Var>();
				switch(neqCondition.column){
				case "Column1":
					vec.add(new Var("alpha"));
				break;
				case "Column2":
					vec.add(new Var("beta"));
				break;
				default:
					if(neqCondition.column.startsWith("b"))// bounded column
						vec.add(new Var("l"+neqCondition.column.substring(1, neqCondition.column.length())));
					else
						throw new RuntimeException("undefined column "+neqCondition.column);
					
				
				}
				vec.add(neqCondition.value);
				return new NegationFormula( new RelationFormula(new Relation("=",vec)));
	}

	@Override
	public Object visit(NotCondition notCondition) {
		// TODO Auto-generated method stub
		return new NegationFormula((Formula)notCondition.cond.accept(this));
	}
}
