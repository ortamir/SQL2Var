package Visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

import AST.AndFormula;
import AST.AssignSTMT;
import AST.ChooseSTMT;
import AST.DeleteQuery;
import AST.ExistsFormula;
import AST.ForAllFormula;
import AST.Formula;
import AST.IdGenerator;
import AST.IfSTMT;
import AST.ImpliesFormula;
import AST.InsertQuery;
import AST.NegationFormula;
import AST.OneColumn;
import AST.OrFormula;
import AST.Query;
import AST.QuerySTMT;
import AST.Relation;
import AST.RelationFormula;
import AST.Schema;
import AST.SelectQuery;
import AST.SkipStmt;
import AST.UpdateQuery;
import AST.Var;
import AST.isEmptyCondition;
import AST.isNotEmptyCondition;

public class WPVisitor implements StatementVisitor {

	@Override
	public Formula visit(QuerySTMT querySTMT, Formula assn) {
		Query q = querySTMT.query;
		if(q instanceof DeleteQuery){
			DeleteQuery deleteQuery = (DeleteQuery)q;
			Formula cond = (Formula) deleteQuery.condition.accept(new ConditionVisitor());
			Vector<Var> vs = new Vector<>();

			switch(Schema.checkTableArity(deleteQuery.table)){
			case 1:
				vs.add(new Var("alpha"));
				break;
			case 2:
				vs.add(new Var("alpha"));
				vs.add(new Var("beta"));
				break;
			}

			Utils.addLimitedVariables(deleteQuery.table, vs);


			Formula comp = new AndFormula(new RelationFormula(new Relation(deleteQuery.table,vs)),new NegationFormula(cond));
			assn.accept(new TiedVarsVisitor(), new HashSet<Var>());
			assn = (Formula) assn.accept(new ReplaceVisitor(deleteQuery.table,comp));

		}
		else if (q instanceof InsertQuery) {
			InsertQuery insertQuery = (InsertQuery)q;
			Vector<Var> vs1=new Vector<>(),vs2 = new Vector<>();
			Vector<Var> vs = new Vector<>();
			Vector<Var> vec;
			Formula comp = null;
			switch(Schema.checkTableArity(insertQuery.table)){
			case 1:
				vs.add(new Var("alpha"));
				Utils.addLimitedVariables(insertQuery.table, vs);
				vs1.add(new Var("alpha"));
				vs1.add(insertQuery.terms.elementAt(0));
				comp = new RelationFormula(new Relation("=",vs1));

				vec=insertQuery.terms;
				for(int j=1;j<vec.size();j++){
					Vector<Var> vs3 = new Vector<>();
					vs3.add(new Var("l"+((j-1))));
					vs3.add(insertQuery.terms.elementAt(j));
					comp = new AndFormula(new RelationFormula(new Relation("=",vs3)),comp);
				}
				comp = new OrFormula(new RelationFormula(new Relation(insertQuery.table,vs)),comp);
				break;
			case 2:
				vs.add(new Var("alpha"));
				vs.add(new Var("beta"));
				Utils.addLimitedVariables(insertQuery.table, vs);
				vs1.add(new Var("alpha"));
				vs1.add(insertQuery.terms.elementAt(0));
				vs2.add(new Var("beta"));
				vs2.add(insertQuery.terms.elementAt(1));
				comp= new AndFormula(new RelationFormula(new Relation("=",vs1)),new RelationFormula(new Relation("=",vs2)));
				vec=insertQuery.terms;
				for(int j=2;j<vec.size();j++){
					Vector<Var> vs3 = new Vector<>();
					vs3.add(new Var("l"+((j-2))));
					vs3.add(insertQuery.terms.elementAt(j));
					comp = new AndFormula(new RelationFormula(new Relation("=",vs3)),comp);
				}
				comp = new OrFormula(new RelationFormula(new Relation(insertQuery.table,vs)),comp);
				break;
			}
			assn.accept(new TiedVarsVisitor(), new HashSet<Var>());
			assn = (Formula) assn.accept(new ReplaceVisitor(insertQuery.table,comp));
		}

		else if(q instanceof UpdateQuery) {
			UpdateQuery updateQuery = (UpdateQuery)q;
			Formula cond = (Formula) updateQuery.condition.accept(new ConditionVisitor());
			Vector<Var> vs = new Vector<>(),vs1 = new Vector<>(),va = new Vector<>();
			Formula comp;
			Map<Var,Var> m =new HashMap<>();
			Var a1 = new Var(IdGenerator.getFreshId());
			Var var = null;
			switch(Schema.checkTableArity(updateQuery.table)){
			case 1:
				vs.add(new Var("alpha"));
				Utils.addLimitedVariables(updateQuery.table, vs);
				va.add(a1); 
				switch(updateQuery.assignment.column) {
					case "Column1": var = vs.get(0); break;
					case "b0": if (vs.size() > 1) var = vs.get(1); break;
					case "b1": if (vs.size() > 2) var = vs.get(2); break;   // TODO generalize!
				}
				if (var == null) {
					throw new RuntimeException("no such column '" + updateQuery.assignment.column + "' in table '" + updateQuery.table + "'");
				}
				vs1.add(var);
				m.put(var, a1);
				a1.limit = var.limit;
				vs1.add(updateQuery.assignment.value);
				break;
			case 2:
				vs.add(new Var("alpha"));
				vs.add(new Var("beta"));
				Utils.addLimitedVariables(updateQuery.table, vs);
				va.add(a1);
				switch(updateQuery.assignment.column){
					case "Column1": var = vs.get(0); break;
					case "Column2": var = vs.get(1); break;
					case "b0": if (vs.size() > 1) var = vs.get(1); break;
					case "b1": if (vs.size() > 2) var = vs.get(2); break;   // TODO generalize!
				}
				if (var == null) {
					throw new RuntimeException("no such column '" + updateQuery.assignment.column + "' in table '" + updateQuery.table + "'");
				}
				vs1.add(var);
				m.put(var, a1);
				a1.limit = var.limit;
				vs1.add(updateQuery.assignment.value);
				break;
			}
			Formula condA1 = (Formula) new AndFormula(new RelationFormula(new Relation(updateQuery.table,vs)),(Formula) cond).accept(new RenameVisitor(m));
			comp = new OrFormula(
					new AndFormula(
							new RelationFormula(
									new Relation(updateQuery.table,vs)),
							new NegationFormula(cond)),
					new AndFormula(
							new ExistsFormula(va, condA1),
							new RelationFormula(
									new Relation("=",vs1))));

			assn.accept(new TiedVarsVisitor(), new HashSet<Var>());

			assn = (Formula) assn.accept(new ReplaceVisitor(updateQuery.table,comp));
		}
		else {
			throw new RuntimeException("query type not implemented (" + q.getClass().getName() + ")");
		}
		
		return assn;
	}

	@Override
	public Formula visit(AssignSTMT assignSTMT, Formula f) {
		//unused
		throw new RuntimeException("used assignSTMT not suppose too");
	}

	@Override
	public Formula visit(IfSTMT ifSTMT, Formula Q) {
		// wp[[if cond then cmd1 else cmd2]](Q) = ([[cond]] && wp[[cmd1]](Q)) || (~[[cond]] && wp[[cmd2]](Q))
		Formula cond = null;
		Vector<Var> vs;
		switch(ifSTMT.type){
		case 0:
			SelectQuery q = (SelectQuery) ifSTMT.query;
			//cond = (Formula) q.condition.accept(new ConditionVisitor());
			isEmptyCondition c = new isEmptyCondition(q);
			cond = (Formula) c.accept(new ConditionVisitor());
			//FreeVarsNode fvn=(FreeVarsNode) cond.accept(new FreeVarsVisitor());
			//Vector<Var> vs =new Vector<Var>(fvn.set);
			//cond = new ExistsFormula(vs, cond);
			break;
		case 1:
			q = (SelectQuery) ifSTMT.query;
			isNotEmptyCondition c1 = new isNotEmptyCondition(q);
			cond = (Formula) c1.accept(new ConditionVisitor());
			
			//fvn=(FreeVarsNode) cond.accept(new FreeVarsVisitor());
			//vs =new Vector<Var>(fvn.set);
			//cond = new NegationFormula(new ExistsFormula(vs, cond));
			break;
		case 3:
			vs =new Vector<Var>();
			vs.add(new Var(ifSTMT.var));
			vs.add(ifSTMT.val);
			cond = new RelationFormula(new Relation("=", vs));
			break;
		case 4:
		    cond = ifSTMT.f;
			break;
		}
		
		
		Formula trueBranch = Q;
		for (AST.Stmt stmt : reversed(ifSTMT.trueStmts)) {
			trueBranch = stmt.accept(this, trueBranch);
		}
		
		Formula falseBranch = Q;
		for (AST.Stmt stmt : reversed(ifSTMT.falseStmts)) {
			falseBranch = stmt.accept(this, falseBranch);
		}
		
		return new OrFormula(new AndFormula((Formula) cond.accept(new CloneVisitor()), trueBranch),
				new AndFormula(new NegationFormula((Formula) cond.accept(new CloneVisitor())), falseBranch));
	}

	@Override
	public Formula visit(ChooseSTMT chooseSTMT, Formula f) {
		// wp[[v = CHOOSE A]](Q) = forall x, A(x) -> Q[x/v]
		Var s1 =new Var(chooseSTMT.var);
		SelectQuery query = (SelectQuery) chooseSTMT.query;
		int numberOfUnbound = Schema.checkTableArity(query.table),numberOfBounded=Schema.getTableBoundedColumns(query.table).size();
		Formula cond  = (Formula) (query.condition.accept(new ConditionVisitor()));
		OneColumn oc = (OneColumn)query.columns;
		Var x= new Var(IdGenerator.getFreshId()),v;
		Vector<Var> vx=new Vector<Var>(),vs=new Vector<Var>(),all=new Vector<Var>();
		vx.add(x);
		Map<Var,Var> m2 = new HashMap<Var,Var>();
		switch(oc.column){
		case "Column1":
			all.add(x);
			m2.put(new Var("alpha"), x);
			if(numberOfUnbound == 2){
				v = new Var(IdGenerator.getFreshId());
				m2.put(new Var("beta"), v);
				all.add(v);
				vs.add(v);
			}
			for(int i=0;i<numberOfBounded;i++){
				v = new Var(IdGenerator.getFreshId());
				m2.put(new Var("l"+i), v);
				all.add(v);
				vs.add(v);
			}
			break;
		case "Column2":
			v = new Var(IdGenerator.getFreshId());
			m2.put(new Var("alpha"), v);
			all.add(v);
			vs.add(v);
			all.add(x);
			for(int i=0;i<numberOfBounded;i++){
				v = new Var(IdGenerator.getFreshId());
				m2.put(new Var("l"+i), v);
				all.add(v);
				vs.add(v);
			}
			
			break;
		default:
			if(oc.column.startsWith("b"))
			{
				int k=Integer.parseInt(oc.column.substring(1, oc.column.length()));
				v = new Var(IdGenerator.getFreshId()); //first unbound
				m2.put(new Var("alpha"), v);
				all.add(v);
				vs.add(v);
				if(numberOfUnbound == 2){  //second unbound
					v = new Var(IdGenerator.getFreshId());
					m2.put(new Var("beta"), v);
					all.add(v);
					vs.add(v);
				}
				for(int i=0;i<k;i++){
					v = new Var(IdGenerator.getFreshId());
					m2.put(new Var("l"+i), v);
					all.add(v);
					vs.add(v);
				}
				all.add(x);
				for(int i=k+1;i<numberOfBounded;i++){
					v = new Var(IdGenerator.getFreshId());
					m2.put(new Var("l"+i), v);
					all.add(v);
					vs.add(v);
				}
			}
			else
				throw new RuntimeException("unrecognized column:"+oc.column);
		}
		cond = (Formula) cond.accept(new RenameVisitor(m2));
		Map<Var,Var> m = new HashMap<Var,Var>();
		m.put(s1, x);
		return new ForAllFormula(vx, 
				new ImpliesFormula(
						new ExistsFormula(vs, 
								new AndFormula(new RelationFormula(new Relation(query.table, all)), cond)), (Formula) f.accept(new RenameVisitor(m))));
		
		/*
		QuerySTMT qs = new QuerySTMT(chooseSTMT.query);
		FreeVarsNode fvn=(FreeVarsNode) A.accept(new FreeVarsVisitor());
		if(fvn.set.size()==1)
			throw new RuntimeException("more than one free var in choose query");
		Var x = fvn.set.iterator().next();
		
		Formula Q = (Formula) f.accept(new CloneVisitor());
		Map<Var,Var> map = new HashMap<Var,Var>();
		map.put(v,x);
		Q.accept(new RenameVisitor(map));
		Vector<Var> vs = new Vector<Var>();
		vs.add(x);
		return new ForAllFormula(vs,new ImpliesFormula(A, Q));*/
	}

	@Override
	public Formula visit(SkipStmt skipStmt, Formula q) {
		return q;
	}

	static <E> Iterable<E> reversed(List<E> l) {
		ListIterator<E> it = l.listIterator(l.size());
		return Stream.generate(() -> it.previous()).limit(l.size())::iterator; // that's Java 8 for you
	}
	
	public Formula sequence(Vector<AST.Stmt> stmts, Formula postcondition) {
		Formula ret = postcondition;
		for (AST.Stmt stmt : reversed(stmts)) {
			ret = stmt.accept(this, ret);
		}
		return ret;
	}
}
