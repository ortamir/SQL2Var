package AST;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class Schema {

	public static Map<String,TableDef> tables;
	
	static{
		tables = new HashMap<>();
		tables.put("Users", new TableDef(1));
		tables.put("Subscriptions", new TableDef(2));
	}

public static class TableDef{
	public int numColumns; //number of unbounded columns. may be 1 or 2.
	public Vector<Integer> boundedColumns;

	public TableDef(int numColumns) {
		super();
		this.numColumns = numColumns;
		this.boundedColumns = new Vector<>();
	}

	public TableDef(String n) {
		this.numColumns = Integer.parseInt(n);
		this.boundedColumns = new Vector<>();
	}

	public TableDef(Integer n, Vector<Integer> nl) {
		super();
		this.numColumns = n;
		this.boundedColumns =nl;
	}
	
	public List<String> signature() {
		List<String> sig = new LinkedList<>();
		for (int i = 0; i < numColumns; i++) sig.add(sortForSize(0));
		for (int k : boundedColumns) sig.add(sortForSize(k));
		return sig;
	}
	
	public String getAllDeclarations(String tableName){
		String ret="";
		Vector<Integer> v = new Vector<Integer>();
		for(int i=0;i<this.boundedColumns.size();i++){
			v.add(0);
		}
		do{
			String s = vector2FormatedString(v);
			if(this.numColumns==1)
				ret += "(declare-fun "+tableName+"_"+s+" (V) Bool)\n";
			else
				ret += "(declare-fun "+tableName+"_"+s+" (V V) Bool)\n";
			
		}while(incrementVector(v,this.boundedColumns));
		
		return ret;
	}
	
	private String vector2FormatedString(Vector<Integer> v) {
		String ret="";
		if (!v.isEmpty()) {
			ret+=v.elementAt(0).intValue();
			for(int i=1;i<v.size();i++)
				ret+="_"+v.elementAt(i).intValue();
		}
		return ret;
	}

	private boolean incrementVector(Vector<Integer> v,Vector<Integer> boundedColumns) {
		for(int i=v.size()-1;i>=0;i--){
			int bound = boundedColumns.elementAt(i);
			int val = v.elementAt(i);
			if(val<bound-1){ //found someone to increment
				v.setElementAt(val+1, i);
				return true;
			}
			v.setElementAt(0, i);
		}
		return false;
	}

	public static String sortForSize(int sz) {
		if (sz == 0) return "V"; else return "B$" + sz;
	}
}

public static int checkTableArity(String table) {
	TableDef td = tables.get(table);
	if(td == null)
		throw new RuntimeException("no table found!"+table);
	if(td.numColumns > 2)
		throw new RuntimeException("table size is "+td.numColumns);
	return td.numColumns;
}

public static Vector<Integer> getTableBoundedColumns(String table) {
	TableDef td = tables.get(table);
	if(td == null)
		throw new RuntimeException("no table found!"+table);
	return td.boundedColumns;
}

public static String getAllBlastedTableDefs() {
	String ret="";
	for(Iterator<Entry<String, TableDef>> i=tables.entrySet().iterator();i.hasNext();){
		Entry<String, TableDef> e = i.next();
		ret += e.getValue().getAllDeclarations(e.getKey());
	}
	return ret;
}


	
}



