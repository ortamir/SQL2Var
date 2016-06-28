import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.cli.*;

import AST.*;
import AST.Schema.TableDef;
import Parser.Lexer;
import Parser.SqlCup;
import Visitors.*;
import java_cup.runtime.Symbol;


public class Main {

	public static final boolean DEBUG = true;

	public static final String HARD_CODED_DECLARATIONS = 
	"(declare-fun NS_0_0 (V V) Bool)\n" +
	"(declare-fun NS_0_1 (V V) Bool)\n" +
	"(declare-fun NS_0_2 (V V) Bool)\n" +
	"(declare-fun NS_1_0 (V V) Bool)\n" +
	"(declare-fun NS_1_1 (V V) Bool)\n" +
	"(declare-fun NS_1_2 (V V) Bool)\n" +
	"(declare-fun NSgho_0_0 (V V) Bool)\n" +
	"(declare-fun NSgho_0_1 (V V) Bool)\n" +
	"(declare-fun NSgho_0_2 (V V) Bool)\n" +
	"(declare-fun NSgho_1_0 (V V) Bool)\n" +
	"(declare-fun NSgho_1_1 (V V) Bool)\n" +
	"(declare-fun NSgho_1_2 (V V) Bool)\n";

	static CommandLine cmdline(String[] args) {
		try {
			Options options = new Options();

			options.addOption(Option.builder().longOpt("path").hasArg().build());
			options.addOption(Option.builder().longOpt("collate").build());
			options.addOption(Option.builder().longOpt("no-blast").build());

			return new DefaultParser().parse(options, args);
		}
		catch (ParseException e) {
			System.err.println("error parsing arguments: " + e.getMessage());
			System.exit(1);
			return null;
		}
	}
			
	
	public static void main(String [] args){
		CommandLine opts = cmdline(args);
		
		File path = new File(opts.getOptionValue("path", ".")); // "C:\\Users\\admin\\Dropbox\\programs\\bounded\\";
		String[] filenames = opts.getArgs(); // {"incorrect\\confirmIC.txt","correct\\subscribeC.txt"};//{"incorrect\\networkIC.txt","incorrect\\subscribeIC.txt","incorrect\\unsubscribeIC.txt","correct\\networkC.txt","correct\\subscribeC.txt","correct\\unsubscribeC.txt"};
		
		boolean collateRelations = opts.hasOption("collate");
		boolean blastVars = !opts.hasOption("no-blast");
		//int [] mul = {1,10,100,1000};

		//convert(path+"correct\\networkC.txt", 2);

		for (String filename : filenames){

			//for(int j=0;j<mul.length;j++)
			//	convert(path+filenames[i], mul[j]);
			File file = new File(path, filename);
			Symbol parseSymbol;
			try {

				Reader txtFile = new FileReader(file);
				Lexer scanner = new Lexer(txtFile);
				SqlCup parser = new SqlCup(scanner);
				
				
				
				
				
				try {
					parseSymbol = parser.parse();
					Program p  = (Program)parseSymbol.value;
					Formula assn = (Formula) p.postCondition.accept(new CloneVisitor());

					Vector<Stmt> sv = p.stmts;
					
					assn = new WPVisitor().sequence(sv, assn);
					/*for(int k=sv.size()-1;k>=0;k--){
						assn = sv.get(sv.size()-1).accept(new WPVisitor(), assn);
					}*/

					System.err.println(assn.accept(new PrinterVisitor()));
					
					Formula check = new NegationFormula(new ImpliesFormula(p.preCondition, assn));
					check.accept(new FormulaTypeVisitor());
					if (blastVars) {
						check = (Formula) check.accept(new ReleaseBoundVarVisitor());
						check.accept(new FormulaTypeVisitor());
					}

					Set<Var> free = ((FreeVarsNode)check.accept(new FreeVarsVisitor())).set;
					String out ="(declare-sort V 0)\n(declare-datatypes () ((B$2 _0@2 _1@2) (B$3 _0@3 _1@3 _2@3)))\n";
					for(Entry<String,TableDef> e : Schema.tables.entrySet()) {
						String sig = String.join(" ", e.getValue().signature());
						out+="(declare-fun "+e.getKey()+" ("+sig+") Bool)\n";
					}
					
					String s=Schema.getAllBlastedTableDefs();
					//out += HARD_CODED_DECLARATIONS;
					out += s;
					
					for(Var v : free) {
						if (!v.isNumeral()) {
							String sort = TableDef.sortForSize(v.limit);
							out += "(declare-const "+v.name+" "+sort+")\n";
						}
					}

					if (blastVars) {
						Map<Var,Var> m = new HashMap<Var,Var>();
						m.put(new Var("x"), new Var(IdGenerator.getFreshId()));
						m.put(new Var("y"), new Var(IdGenerator.getFreshId()));
						check.accept(new RenameVisitor(m)); //rename existing x and y as new vars
	
						//System.err.println("// BLASTED //\n" + check.accept(new PrinterVisitor()));
						
						check.accept(new MakeOnly2Visitor(), null);
					}

					String str= "(assert "+((StringNode)check.accept(new PrinterVisitor(collateRelations))).str+ " )";
					out += str + "\n";
					out += "(check-sat)\n";
					out += "(get-model)\n";
					System.out.println(out);
					//System.out.println(mul);
					//PrintWriter outFiles = new PrintWriter(filename.substring(0, filename.length()-4)+"x"+mul+".smt2");
					String outFilename = file.getPath().replaceFirst("(.txt)?$", ".smt2");
					try {
						PrintWriter outFiles = new PrintWriter(outFilename);
						outFiles.println(out);
						outFiles.close();
					} catch (FileNotFoundException e) {
						System.err.println("error: cannot write '" + outFilename + "'");
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				System.err.println("error: cannot read '" + file.getPath() + "'");
				e.printStackTrace();
			}
		}


	}

}


/*
	private static void convert(String filename, int mul) {
		Symbol parseSymbol;
		try {
			Reader txtFile = new FileReader(filename);
			Lexer scanner = new Lexer(txtFile);
			SqlCup parser = new SqlCup(scanner);

			try {
				parseSymbol = parser.parse();
				Program p  = (Program)parseSymbol.value;


				if(mul > 1){
					Set<Var> freePre = ((FreeVarsNode)p.preCondition.accept(new FreeVarsVisitor())).set;
					Set<Var> freeQuery = new HashSet<>();
					Vector<Query> addQueries = new Vector<>();
					for(Iterator<Query> i=p.queries.iterator();i.hasNext();){
						Query q = i.next();
						freeQuery.addAll(((FreeVarsNode)q.accept(new FreeVarsQueryVisitor())).set);
					}
					Set<Var> freePost = ((FreeVarsNode)p.postCondition.accept(new FreeVarsVisitor())).set;
					Set<Var> free= new HashSet<>();
					free.addAll(freePre);free.addAll(freeQuery);free.addAll(freePost);

					p.preCondition.accept(new TiedVarsVisitor(),new HashSet<Var>());
					Formula nPre = (Formula) p.preCondition.accept(new MultVisitor(mul));

					for(Iterator<Query> i=p.queries.iterator();i.hasNext();){
						Query q = i.next();
						addQueries.addElement(q);
					}

					p.postCondition.accept(new TiedVarsVisitor(),new HashSet<Var>());
					Formula nPost = (Formula) p.postCondition.accept(new MultVisitor(mul));
					for (int i=1;i<mul;i++){
						Map<Var,Var> m = new HashMap<Var,Var>();
						for(Iterator<Var> it=free.iterator();it.hasNext();){
							Var v = it.next();
							m.put(v, new Var(v.name+(i+1)));
						}


						//nPre = new AndFormula(nPre,(Formula) p.preCondition.accept(new RenameVisitor(m)));
						for(Iterator<Query> it=p.queries.iterator();it.hasNext();){
							Query q = it.next();
							addQueries.addElement((Query) q.accept(new RenameQueryVisitor(m)));
						}
						//nPost = new AndFormula(nPost,(Formula) p.postCondition.accept(new RenameVisitor(m)));
					}
					p.preCondition = nPre;
					p.queries = addQueries;
					p.postCondition = nPost;
				}

				Formula assn = (Formula) p.postCondition.accept(new CloneVisitor());

				for(int i = p.queries.size() -1 ; i>= 0 ;i--){
					Query q = p.queries.elementAt(i);

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
					if(q instanceof InsertQuery){
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
							for(int j=1;j<vec.size();j++){
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

					if(q instanceof UpdateQuery){
						UpdateQuery updateQuery = (UpdateQuery)q;
						Formula cond = (Formula) updateQuery.condition.accept(new ConditionVisitor());
						Vector<Var> vs = new Vector<>(),vs1 = new Vector<>(),va = new Vector<>();
						Formula comp;
						Map<Var,Var> m =new HashMap<>();
						Var a1 = new Var(IdGenerator.getFreshId());
						switch(Schema.checkTableArity(updateQuery.table)){
						case 1:
							vs.add(new Var("alpha"));
							Utils.addLimitedVariables(updateQuery.table, vs);
							vs1.add(new Var("alpha"));
							vs1.add(new Var(updateQuery.assignment.value));
							va.add(a1);
							m.put(new Var("alpha"), a1);
							break;
						case 2:
							vs.add(new Var("alpha"));
							vs.add(new Var("beta"));
							Utils.addLimitedVariables(updateQuery.table, vs);
							va.add(a1);
							switch(updateQuery.assignment.column){
							case "Column1":
								vs1.add(new Var("alpha"));
								m.put(new Var("alpha"), a1);
								break;

							case "Column2":
								vs1.add(new Var("beta"));
								m.put(new Var("beta"), a1);
								break;
							}
							vs1.add(new Var(updateQuery.assignment.value));
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
				}
				Formula check = new NegationFormula(new ImpliesFormula(p.preCondition, assn));
				check.accept(new FormulaTypeVisitor());
				check = (Formula) check.accept(new ReleaseBoundVarVisitor());

				Set<Var> free = ((FreeVarsNode)check.accept(new FreeVarsVisitor())).set;
				String out ="(declare-sort V 0)\n";
				for(Iterator<Entry<String,TableDef>> i =Schema.tables.entrySet().iterator();i.hasNext();){
					Entry<String,TableDef> e = i.next();
					switch(e.getValue().numColumns){
					case 1:
						out+="(declare-fun "+e.getKey()+" (V) Bool)\n";
						break;
					case 2:
						out+="(declare-fun "+e.getKey()+" (V V) Bool)\n";
						break;
					default:
						throw new RuntimeErrorException(null);
					}
				}

				for(Iterator<Var> i=free.iterator();i.hasNext();){
					Var v=i.next();
					out += "(declare-const "+v.name+" V)\n";
				}

				Map<Var,Var> m = new HashMap<Var,Var>();
				m.put(new Var("x"), new Var(IdGenerator.getFreshId()));
				m.put(new Var("y"), new Var(IdGenerator.getFreshId()));
				check.accept(new RenameVisitor(m)); //rename existing x and y as new vars

				check.accept(new MakeOnly2Visitor(), null);

				String str= "(assert "+((StringNode)check.accept(new PrinterVisitor())).str+ " )";
				out += str + "\n";
				out += "(check-sat)\n";
				out += "(get-model)\n";
				//System.out.println(out);
				System.out.println(mul);
				PrintWriter outFiles = new PrintWriter(filename.substring(0, filename.length()-4)+"x"+mul+".smt2");
				outFiles.println(out);
				outFiles.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/


