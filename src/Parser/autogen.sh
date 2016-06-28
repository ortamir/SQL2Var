# This script should run from src/Parser as working directory
jflex -nobak sql.lex
java -jar ../../lib/java-cup-11b.jar -parser SqlCup -symbols SqlSym sql.cup
