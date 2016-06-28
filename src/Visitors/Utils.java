package Visitors;
import java.util.Vector;

import AST.IdGenerator;
import AST.Schema;
import AST.Var;


public class Utils {

	public static void addLimitedVariables(String table,Vector<Var> vs) {
		Vector<Integer> vec = Schema.getTableBoundedColumns(table);
		if(vec == null) // no bounded columns
			return;
		int bound = vec.size();

		for(int j=0;j<bound;j++){
			vs.add(new Var("l"+j, vec.elementAt(j)));
		}
		
	}

}
