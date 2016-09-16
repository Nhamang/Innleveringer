package no.uio.ifi.alboc.syntax;

/*
 * module Syntax
 */

import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.code.Code;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;
import no.uio.ifi.alboc.scanner.Token;
import static no.uio.ifi.alboc.scanner.Token.*;
import no.uio.ifi.alboc.types.*;

/*
 * Creates a syntax tree by parsing an AlboC program; 
 * prints the parse tree (if requested);
 * checks it;
 * generates executable code. 
 */
public class Syntax {
    static DeclList library;
    static Program program;

    public static void init() {
		library = new GlobalDeclList();
		library.lineNum=-1;
		library.addDecl(new FuncDecl("exit", Types.intType, Types.intType));
		library.addDecl(new FuncDecl("getchar", Types.intType, null));
		library.addDecl(new FuncDecl("getint", Types.intType, null));
		library.addDecl(new FuncDecl("putchar", Types.intType, Types.intType));
		library.addDecl(new FuncDecl("putint", Types.intType, Types.intType));
	//-- Must be changed in part* 1+2:
    }
	
	//Shouldn't be any need for finish
    public static void finish() {
	//-- Must be changed in part* 1+2:
    }

    public static void checkProgram() {
	program.check(library);
    }

    public static void genCode() {
	program.genCode(null);
    }

    public static void parseProgram() {
	program = Program.parse();
    }

    public static void printProgram() {
	program.printTree();
    }
}


/*
 * Super class for all syntactic units.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class SyntaxUnit {
    int lineNum;

    SyntaxUnit() {
	lineNum = Scanner.curLine;
    }

    abstract void check(DeclList curDecls);
    abstract void genCode(FuncDecl curFunc);
    abstract void printTree();

    void error(String message) {
	Error.error(lineNum, message);
    }
}


/*
 * A <program>
 */
class Program extends SyntaxUnit {
    DeclList progDecls;
	
    @Override void check(DeclList curDecls) {
	progDecls.check(curDecls);

	if (! AlboC.noLink) {
	    // Check that 'main' has been declared properly:
	    //-- Must be changed in part *2:
	    
	    Declaration d = progDecls.findDecl("main", Syntax.library);
	    if(d instanceof FuncDecl){
			FuncDecl fd = (FuncDecl) d;
			if(fd.type!=Types.intType){
				fd.error("Function 'main' should be int");
			}
			if(fd.funcParam.antParam() != 0){
				fd.error("There shouldn't be any paramaters on 'main'");
			}
		}else{
			d.error("'main' is not declared as a function");
		}
	}
    }
		
    @Override void genCode(FuncDecl curFunc) {
		
		progDecls.genCode(null);
    }

    static Program parse() {
	Log.enterParser("<program>");

	Program p = new Program();
	p.progDecls = GlobalDeclList.parse();
	if (Scanner.curToken != eofToken)
	    Error.expected("A declaration");

	Log.leaveParser("</program>");
	return p;
    }

    @Override void printTree() {
	progDecls.printTree();
    }
}


/*
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

abstract class DeclList extends SyntaxUnit {
    Declaration firstDecl = null;
    DeclList outerScope;

	//Should not be a need
    DeclList () {
	//-- Must be changed in part* 1:
    }

    @Override void check(DeclList curDecls) {
	outerScope = curDecls;

	Declaration dx = firstDecl;
	while (dx != null) {
	    dx.check(this);  dx = dx.nextDecl;
	}
    }

    @Override void printTree() {
	//-- Must be changed in part* 1:
		Declaration innerDecl = firstDecl;
		
		while(innerDecl != null){
			innerDecl.printTree();
			innerDecl=innerDecl.nextDecl;
			Log.wTreeLn();
		}
    }

    void addDecl(Declaration d) {
		if(firstDecl==null){
			firstDecl = d;
			return;
		}
		
		Declaration adding = firstDecl;
		
		while(true){
			if(adding.name.equals(d.name)){
				d.error("Declaration: " + d.name + " already added");
			}
			if(adding.nextDecl == null){
				adding.nextDecl = d;
				
				break;
			}
			adding = adding.nextDecl;
		}
		
	//-- Must be changed in part* 1:
    }

    int dataSize() {
	Declaration dx = firstDecl;
	int res = 0;

	while (dx != null) {
	    res += dx.declSize();  dx = dx.nextDecl;
	}
	return res;
    }
    

    Declaration findDecl(String name, SyntaxUnit use) {
	//-- Must be changed in part *2:
		Declaration d = firstDecl;
		while(d!=null){
			
			if((d.visible) && (d.name.equals(name))){
				Log.noteBinding(d.name, d.lineNum, use.lineNum);
				return d;
			}
			d=d.nextDecl;
		}
		
		if(outerScope == null){
			use.error("Could not find "+name);
		}
		
		d = outerScope.findDecl(name, use);
		return d;
    }
    
    int antParam(){
		int i = 0;
		Declaration d = firstDecl;
		
		while(d!=null){
			i+=1;
			d=d.nextDecl;
		}
		return i;
	}
}


/*
 * A list of global declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class GlobalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		Declaration d = firstDecl;
		while(d!=null){
			d.genCode(null);
			d = d.nextDecl;
		}
    }

    static GlobalDeclList parse() {
	GlobalDeclList gdl = new GlobalDeclList();

	while (Scanner.curToken == intToken) {
	    DeclType ts = DeclType.parse();
	    gdl.addDecl(Declaration.parse(ts));
	}
	return gdl;
    }
}


/*
 * A list of local declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class LocalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		Declaration d = firstDecl;
		int i = 0;
		while(d!=null){
			i-=d.declSize();
			d.assemblerName= i+"(%ebp)";
			d.genCode(curFunc);
			d=d.nextDecl;
		}
    }

    static LocalDeclList parse() {
		LocalDeclList innerList = new LocalDeclList();
		while(Scanner.curToken == intToken){
			DeclType innerType = DeclType.parse();
			LocalVarDecl tmp;
			tmp = LocalVarDecl.parse(innerType);
			innerList.addDecl(tmp);
		}
	//-- Must be changed in part* 1:
		return innerList;
    }
}


/*
 * A list of parameter declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class ParamDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		Declaration d = firstDecl;
		int i = 8;
		while(d!=null){
			d.assemblerName = i+"(%ebp)";
			i+=d.declSize();
			d.genCode(curFunc);
			d = d.nextDecl;
		}
		
    }

    static ParamDeclList parse() {
		//Log.enterParser("<>");
		ParamDeclList innerParamDeclList = new ParamDeclList();
		Scanner.skip(leftParToken);
		if(Scanner.curToken != rightParToken){
			while(Scanner.curToken!=rightParToken){
				DeclType innerDeclType = DeclType.parse();
				ParamDecl tmpDecl = ParamDecl.parse(innerDeclType);
				innerParamDeclList.addDecl(tmpDecl);
				if(Scanner.curToken==rightParToken){
					break;
				}
				Scanner.skip(commaToken);
			}
		}
		
		//-- Must be changed in part* 1:
		Scanner.skip(rightParToken);
		return innerParamDeclList;
    }

    @Override void printTree() {
	Declaration px = firstDecl;
	while (px != null) {
	    px.printTree();  px = px.nextDecl;
	    if (px != null) Log.wTree(", ");
	}    
    }
    
}


/*
 * A <type>
 */
class DeclType extends SyntaxUnit {
    int numStars = 0;
    Type type;

    @Override void check(DeclList curDecls) {
	type = Types.intType;
	for (int i = 1;  i <= numStars;  ++i)
	    type = new PointerType(type);
    }

    @Override void genCode(FuncDecl curFunc) {}

    static DeclType parse() {
	Log.enterParser("<type>");

	DeclType dt = new DeclType();

	Scanner.skip(intToken);
	while (Scanner.curToken == starToken) {
	    ++dt.numStars;
	    Scanner.readNext();
	}

	Log.leaveParser("</type>");
	return dt;
    }

    @Override void printTree() {
	Log.wTree("int");
	for (int i = 1;  i <= numStars;  ++i) Log.wTree("*");
    }
}


/*
 * Any kind of declaration.
 */
abstract class Declaration extends SyntaxUnit {
    String name, assemblerName;
    DeclType typeSpec;
    Type type;
    boolean visible = false;
    Declaration nextDecl = null;

    Declaration(String n) {
	name = n;
    }

    abstract int declSize();

    static Declaration parse(DeclType dt) {
	Declaration d = null;
	if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
	    d = FuncDecl.parse(dt);
	} else if (Scanner.curToken == nameToken) {
	    d = GlobalVarDecl.parse(dt);
	} else {
	    Error.expected("A declaration name");
	}
	d.typeSpec = dt;
	return d;
    }


    /**
     * checkWhetherVariable: Utility method to check whether this Declaration is
     * really a variable. The compiler must check that a name is used properly;
     * for instance, using a variable name a in "a()" is illegal.
     * This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be a
     *      variable, the parser will first search for a's declaration d.
     * <li> The parser will call d.checkWhetherVariable(this).
     * <li> Every sub-class of Declaration will implement a checkWhetherVariable.
     *      If the declaration is indeed a variable, checkWhetherVariable will do
     *      nothing, but if it is not, the method will give an error message.
     * </ul>
     * Examples
     * <dl>
     *  <dt>GlobalVarDecl.checkWhetherVariable(...)</dt>
     *  <dd>will do nothing, as everything is all right.</dd>
     *  <dt>FuncDecl.checkWhetherVariable(...)</dt>
     *  <dd>will give an error message.</dd>
     * </dl>
     */
    abstract void checkWhetherVariable(SyntaxUnit use);

    /**
     * checkWhetherFunction: Utility method to check whether this Declaration
     * is really a function.
     * 
     * @param nParamsUsed Number of parameters used in the actual call.
     *                    (The method will give an error message if the
     *                    function was used with too many or too few parameters.)
     * @param use From where is the check performed?
     * @see   checkWhetherVariable
     */
    abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);
}


/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
    boolean isArray = false;
    int numElems = 0;

    VarDecl(String n) {
	super(n);
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		typeSpec.check(curDecls);
		if(isArray){
			type= new ArrayType(typeSpec.type, numElems);
			if(numElems<0){
				error("Array's can be negative");
			}
		}else{
			type=typeSpec.type;
		}
		visible=true;
    }

    @Override void printTree() {
		typeSpec.printTree();
		Log.wTree(" "+name);
		if(isArray){
			Log.wTree("["); Log.wTree(""+numElems); Log.wTree("]");
		}
		Log.wTreeLn(";");
	//-- Must be changed in part* 1:
    }

    @Override int declSize() {
	return type.size();
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
	use.error(name + " is a variable and no function!");
    }
	
    @Override void checkWhetherVariable(SyntaxUnit use) {
	// OK
    }
}


/*
 * A global <var decl>.
 */
class GlobalVarDecl extends VarDecl {
    GlobalVarDecl(String n) {
	super(n);
	assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		if(isArray){
			Code.genVar(assemblerName, true, numElems, type.size()/numElems, type+" "+name);
		}else{
			Code.genVar(assemblerName, true, 1, type.size(), type+" "+name);
		}
    }

    static GlobalVarDecl parse(DeclType dt) {
		Log.enterParser("<var decl>");
		GlobalVarDecl innerGlobalValDecl = new GlobalVarDecl(Scanner.curName);
		innerGlobalValDecl.typeSpec = dt;
		Scanner.skip(nameToken);
		if(Scanner.curToken == leftBracketToken){
			innerGlobalValDecl.isArray = true;
			
			Scanner.skip(leftBracketToken);
			Scanner.check(numberToken);
			
			innerGlobalValDecl.numElems=Scanner.curNum;
		
			Scanner.skip(numberToken);
			Scanner.skip(rightBracketToken);
			Scanner.skip(semicolonToken);
		}else{
			Scanner.skip(semicolonToken);
		}
		Log.leaveParser("</var decl>");
		return innerGlobalValDecl;
	//-- Must be changed in part* 1:
    }
}


/*
 * A local variable declaration
 */
class LocalVarDecl extends VarDecl {
    LocalVarDecl(String n) {
	super(n); 
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
    }

    static LocalVarDecl parse(DeclType dt) {
		Log.enterParser("<var decl>");
		LocalVarDecl innerValDecl = new LocalVarDecl(Scanner.curName);
		innerValDecl.typeSpec = dt;
		Scanner.skip(nameToken);
		if(Scanner.curToken == leftBracketToken){
			innerValDecl.isArray = true;
			
			Scanner.skip(leftBracketToken);
			Scanner.check(numberToken);
			
			innerValDecl.numElems=Scanner.curNum;
		
			Scanner.skip(numberToken);
			Scanner.skip(rightBracketToken);
			Scanner.skip(semicolonToken);
		}else{
			Scanner.skip(semicolonToken);
		}
		//-- Must be changed in part* 1:
		Log.leaveParser("</var decl>");
		return innerValDecl;
    }
}


/*
 * A <param decl>
 */
class ParamDecl extends VarDecl {
    ParamDecl(String n) {
	super(n);
    }

	//no need
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
    }

    static ParamDecl parse(DeclType dt) {
		Log.enterParser("<param decl>");
		ParamDecl innerParam  = new ParamDecl(Scanner.curName);
		innerParam.typeSpec = dt;
		Scanner.skip(nameToken);
		Log.leaveParser("</param decl>");
		//-- Must be changed in part* 1:
		return innerParam;
    }

    @Override void printTree() {
	typeSpec.printTree();  Log.wTree(" "+name);
    }
   
}

class FuncBody extends SyntaxUnit {
	//Part *1
	LocalDeclList myDeclList;
	StatmList myStatmList;
	
	void check(DeclList curDecls){
		
		myDeclList.check(curDecls);
		myStatmList.check(myDeclList);
		//part *2
	}
	void genCode(FuncDecl curFunc){
		
		myDeclList.genCode(curFunc);
		myStatmList.genCode(curFunc);
		//part *2
	}
	
	static FuncBody parse(){
		Log.enterParser("func body>");
		FuncBody innerFuncBody = new FuncBody();
		Scanner.skip(leftCurlToken);
		innerFuncBody.myDeclList = LocalDeclList.parse();
		innerFuncBody.myStatmList = StatmList.parse();
		Scanner.skip(rightCurlToken);
		Log.leaveParser("</func body>");
		return innerFuncBody;
	}
	
	void printTree(){
		Declaration  start;
		if(myDeclList.firstDecl != null){
			start = myDeclList.firstDecl;
			while(start!=null){
				start.printTree();
				start = start.nextDecl;
			}
		}
		Log.wTreeLn();
		myStatmList.printTree();
	}
}

/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    ParamDeclList funcParam;
    String exitLabel;
	FuncBody body;
	
    FuncDecl(String n) {
	// Used for user functions:

		super(n);
		assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
		//-- Must be changed in part* 1:
		exitLabel = ".exit$"+n;
    }
    
    FuncDecl(String n, Type t1, Type t2){
		super(n);
		assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
		lineNum=-1;
		visible = true;
		type=t1;
		funcParam = new ParamDeclList();
		if(t2!=null){
			ParamDecl innerParamDecl = new ParamDecl("p1");
			innerParamDecl.type = t2;
			funcParam.addDecl(innerParamDecl);
		}
	}

    @Override int declSize() {
	return 0;
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		
		typeSpec.check(curDecls);
		type=typeSpec.type;
		funcParam.check(curDecls);
		visible=true;
		body.check(funcParam);
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
	//-- Must be changed in part *2:
		int i = funcParam.antParam();
		if(i!=nParamsUsed){
			use.error(name +" should have " + i + " not "+ nParamsUsed +" paramaters");
		}
    }
	
    @Override void checkWhetherVariable(SyntaxUnit use) {
	//-- Must be changed in part *2:
		use.error(name +" is not a variable");
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		
		int i = body.myDeclList.dataSize();
		
		Code.genInstr("", ".globl", assemblerName, "");
		Code.genInstr(assemblerName, "enter", "$"+i+",$0", "Start function "+name);
		
		funcParam.genCode(this);
		body.genCode(this);
		
		Code.genInstr(exitLabel, "leave", "", "");
		Code.genInstr("", "ret", "", "End function "+ name);
    }

    static FuncDecl parse(DeclType ts) {
		Log.enterParser("<func-decl>");
		
		FuncDecl innerFuncDecl = new FuncDecl(Scanner.curName);
		innerFuncDecl.typeSpec = ts;
		Scanner.skip(nameToken);
		
		innerFuncDecl.funcParam = ParamDeclList.parse();
		innerFuncDecl.body =FuncBody.parse();
		
		//-- Must be changed in part* 1:
		Log.leaveParser("</func-decl>");
		return innerFuncDecl;
    }

    @Override void printTree() {
		typeSpec.printTree();
		Log.wTree(" "+ name +" "); Log.wTree("(");
		funcParam.printTree(); Log.wTree(")"); Log.wTreeLn("{");
		Log.indentTree(); body.printTree();
		Log.outdentTree(); Log.wTreeLn("}");
	//-- Must be changed in part* 1:
    }
}


/*
 * An <Assiment>
 */
class Assignment extends SyntaxUnit{
	LhsVariable lhs;
	Expression asExp;
	void check(DeclList curDecls){
		//Part *2
		
		String s = "v = e";
		String stars = "";
		
		lhs.check(curDecls);
		asExp.check(curDecls);
		
		int i = 1;
		
		while(i<=lhs.numStars){
			stars+="*";
			i++;
		}
		
		s=stars+s;
		
		Log.noteTypeCheck(s, lhs.type, "v", asExp.type, "e", lineNum);
		if((!(lhs.type instanceof ValueType)) || ((!lhs.type.isSameType(asExp.type)) && (asExp.type != Types.intType))){
			error("Illegal types in assignment");
		}
	}
	
	void genCode(FuncDecl curFunc){
		//Part *2
		
		lhs.genCode(curFunc);
		
		Code.genInstr("", "pushl", "%eax", "");
		
		asExp.genCode(curFunc);
		
		Code.genInstr("", "popl","%edx","");
		Code.genInstr("", "movl", "%eax, (%edx)", "");
	}
	
	static Assignment parse(){
		Log.enterParser("<assignment>");
		Assignment innerStatm = new Assignment(); 
		innerStatm.lhs=LhsVariable.parse();
		Scanner.skip(assignToken);
		innerStatm.asExp=Expression.parse();
		Log.leaveParser("</assignment>");
		return innerStatm;
		
	}
	
	void printTree(){
		lhs.printTree(); Log.wTree("="); asExp.printTree();
	}
}

/*
 * A <Factor>
 */
class Factor extends SyntaxUnit{
	Factor next=null;
	Operator firstO=null;
    Primary firstP;
    Type type;
	
	void check(DeclList curDecls){
		//Part *2
		
	    firstP.check(curDecls);
	    if(firstO!=null){
		Primary innerPrimary = firstP;
		Primary innerPrimary2 = innerPrimary.nextPrimary;
		Operator innerOperator = firstO;
		while(innerPrimary2!=null){
		    innerPrimary2.check(curDecls);
		    Log.noteTypeCheck(" x "+innerOperator.oprToken +" y", innerPrimary.type, "x", innerPrimary2.type, "y", lineNum);
		    
		    if((innerPrimary.type != Types.intType) || (innerPrimary2.type != Types.intType)){
				error("Error on type for operator "+innerOperator.oprToken);
			}
		    
		    innerPrimary = innerPrimary2;
		    innerPrimary2=innerPrimary.nextPrimary;
		    innerOperator = innerOperator.nextOpr;
		}
		type=Types.intType;
	    }else{
		type = firstP.type;
	    }
	}
	
	void genCode(FuncDecl curFunc){
		//Part *2
		
		Primary innerPrimary = firstP;
		Operator innerOpr = firstO;
		innerPrimary.genCode(curFunc);
		innerPrimary=innerPrimary.nextPrimary;
		while(innerPrimary!=null){
			Code.genInstr("", "pushl", "%eax", "");
			
			innerPrimary.genCode(curFunc);
			innerOpr.genCode(curFunc);
			
			innerPrimary=innerPrimary.nextPrimary;
			innerOpr=innerOpr.nextOpr;
		}
	}
	
	static Factor parse(){
		//Part* 1
		Log.enterParser("<factor>");
		Factor innerFact = new Factor();
		innerFact.firstP = Primary.parse();
		
		//Primary innerPrim = new Primary();
		Primary innerPrim = innerFact.firstP;
		FactorOpr outerFactorOpr = null;
		while(Token.isFactorOperator(Scanner.curToken)){
			FactorOpr innerFactorOpr = FactorOpr.parse();
			if(outerFactorOpr==null){
				innerFact.firstO = outerFactorOpr=innerFactorOpr;
				//outerFactorOpr = FactorOpr.parse();
				//outerFactorOpr.nextOpr = outerFactorOpr;
			}else{
				outerFactorOpr.nextOpr=outerFactorOpr=innerFactorOpr;
				//outerFactorOpr=FactorOpr.parse();
				//innerFact.firstO = outerFactorOpr;
			}
			innerPrim.nextPrimary = innerPrim = Primary.parse();
			//innerPrim = Primary.parse();
			//innerPrim.nextPrimary = innerPrim;
		}
		Log.leaveParser("</factor>");
		return innerFact;
		
	}
	
	void printTree(){
		//Part* 1
		Primary innerPrimary = firstP;
		Operator innerOperator =firstO;
		innerPrimary.printTree();
		innerPrimary=innerPrimary.nextPrimary;
		while(innerOperator!=null){
			innerOperator.printTree();
			innerOperator=innerOperator.nextOpr;
			innerPrimary.printTree();
			innerPrimary=innerPrimary.nextPrimary;
		}
	
	}
}

class Primary extends SyntaxUnit{
	Primary nextPrimary=null;
	PrefixOpr myPrefixOpr;
	Operand myOperand;
	Type type;
	void check(DeclList curDecls){
		//Part *2
		
		myOperand.check(curDecls);
		type=myOperand.type;
		if(myPrefixOpr!=null){
			Log.noteTypeCheck(myPrefixOpr.oprToken +" e", myOperand.type, "e", lineNum);
			
			if(myPrefixOpr.oprToken==Token.starToken){
			    if(!(myOperand.type instanceof PointerType)){
					error("* can only be applied to pointers");
				}
				type=myOperand.type.getElemType();
			}else if(myPrefixOpr.oprToken==Token.subtractToken){
				if(myOperand.type != Types.intType){
					error("- can only be used on integers");
				}
				type=Types.intType;
			}
		}
	}
	
	void genCode(FuncDecl curFunc){
		//Part *2
		
		myOperand.genCode(curFunc);
		if(myPrefixOpr!=null){
			myPrefixOpr.genCode(curFunc);
		}
	}
	
	static Primary parse(){
		//Part* 1
		Log.enterParser("<primary>");
		Primary innerPrimary = new Primary();
		if(isPrefixOperator(Scanner.curToken)){
			innerPrimary.myPrefixOpr = PrefixOpr.parse();
		}
		innerPrimary.myOperand = Operand.parse();
		Log.leaveParser("</primary>");
		return innerPrimary;
	}
	
	void printTree(){
		//Part* 1
		if(myPrefixOpr == null){
			myOperand.printTree();
		}else{
			myPrefixOpr.printTree();
			myOperand.printTree();
		}
		
	}
}

class FactorOpr extends Operator {
	//part* 1
	
	//no need
	void check(DeclList curDecls){
	    //Part *2
	}
	
	void genCode(FuncDecl curFunc){
	    //Part *2
	    Code.genInstr("", "movl", "%eax, %ecx", "");
	    Code.genInstr("", "popl", "%eax", "");
	    
	    if(oprToken==starToken){
			Code.genInstr("", "imull", "%ecx, %eax", "Compute *");
	    }else if(oprToken==divideToken){
			Code.genInstr("", "cdq", "", "");
			Code.genInstr("", "idivl", "%ecx", "Compute /");
	    }
	}
	
	static FactorOpr parse(){
		Log.enterParser("<factor opr>");
		FactorOpr innerFactorOpr = new FactorOpr();
		innerFactorOpr.oprToken = Scanner.curToken;
		Scanner.skip(innerFactorOpr.oprToken);
		Log.leaveParser("</factor opr>");
		return innerFactorOpr;
	}
	
	void printTree(){
		if(oprToken==divideToken){
			Log.wTree("/");
		}else if(oprToken==starToken){
			Log.wTree("*");
		}
	}
}

/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part* 1:
	Statement firstStatm = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		
		Statement s = firstStatm;
		while(s!=null){
			s.check(curDecls);
			s = s.nextStatm;
		}
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		Statement s = firstStatm;
		while(s!=null){
			s.genCode(curFunc);
			s = s.nextStatm;
		}
    }

    static StatmList parse() {
	Log.enterParser("<statm list>");

	StatmList sl = new StatmList();
	Statement lastStatm = null;
	while (Scanner.curToken != rightCurlToken) {
		if(lastStatm==null){
			sl.firstStatm = Statement.parse();
			lastStatm = sl.firstStatm;
		}else{
			lastStatm.nextStatm = Statement.parse();
			lastStatm = lastStatm.nextStatm;
		}
	    //-- Must be changed in part* 1:
	}

	Log.leaveParser("</statm list>");
	return sl;
    }

    @Override void printTree() {
		Statement innerStatm = firstStatm;
		while(innerStatm!=null){
			innerStatm.printTree();
			innerStatm=innerStatm.nextStatm;
		}
	//-- Must be changed in part* 1:
    }
}


/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {
    Statement nextStatm = null;

    static Statement parse() {
	Log.enterParser("<statement>");

	Statement s = null;
	if (Scanner.curToken==nameToken && 
	    Scanner.nextToken==leftParToken) {
		s=CallStatm.parse();
	    //-- Must be changed in part* 1:
	} else if (Scanner.curToken==nameToken || Scanner.curToken==starToken) {
	    s=AssignStatm.parse();
		//-- Must be changed in part* 1:
	} else if (Scanner.curToken == forToken) {
		s=ForStatm.parse();
	    //-- Must be changed in part* 1:
	} else if (Scanner.curToken == ifToken) {
	    s = IfStatm.parse();
	} else if (Scanner.curToken == returnToken) {
		s=ReturnStatm.parse();
	    //-- Must be changed in part* 1:
	} else if (Scanner.curToken == whileToken) {
	    s = WhileStatm.parse();
	} else if (Scanner.curToken == semicolonToken) {
	    s = EmptyStatm.parse();
	} else {
	    Error.expected("A statement");
	}

	Log.leaveParser("</statement>");
	return s;
    }
}




/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
    //-- Must be changed in part* 1+2:

	//no need
    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
    }

	//no need
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
    }

    static EmptyStatm parse() {
		EmptyStatm tmpEmp = new EmptyStatm();
		Log.enterParser("<empty-statm>");
		Scanner.skip(semicolonToken);
		Log.leaveParser("</empty-statm>");
		
	//-- Must be changed in part* 1:
		return tmpEmp;
    }

    @Override void printTree() {
		Log.wTreeLn(";");
	//-- Must be changed in part* 1:
    }
}
	

/*
 * A <for-statm>.
 */
class ForStatm extends Statement{
	Assignment start, change;
	Expression endTest;
	StatmList body;
	
	void check(DeclList curDecls){
	    //Part *2
	    
	    start.check(curDecls);
	    endTest.check(curDecls);
	    change.check(curDecls);
	    body.check(curDecls);
	    
	    Log.noteTypeCheck("for(.; t; .)", endTest.type, "t", lineNum);
	    
	    if(!(endTest.type instanceof ValueType)){
		error("For test must be a value");
	    }
	    
	}
	
	void genCode(FuncDecl curFunc){
	    //Part *2
	    Code.genInstr("", "", "", "For-statm start");
	    
	    start.genCode(curFunc);
	    
	    String s = Code.getLocalLabel();
	    String s2 = Code.getLocalLabel();
	    
	    Code.genInstr(s, "", "", "");
	    
	    endTest.genCode(curFunc);
	    
	    Code.genInstr("", "cmpl", "$0,%eax", "");
	    Code.genInstr("", "je", s2, "");
	    
	    body.genCode(curFunc);
	    change.genCode(curFunc);
	    
	    Code.genInstr("", "jmp", s, "");
	    Code.genInstr(s2, "", "", "For-statm end");
	}
	
	static ForStatm parse(){
		ForStatm innerStatm = new ForStatm();
		
		Log.enterParser("<for-statm>");
		
		Scanner.skip(forToken);
		Scanner.skip(leftParToken);
		
		innerStatm.start = Assignment.parse();
		Scanner.skip(semicolonToken);
		
		innerStatm.endTest = Expression.parse();
		Scanner.skip(semicolonToken);
		
		innerStatm.change = Assignment.parse();
		Scanner.skip(rightParToken);
		Scanner.skip(leftCurlToken);
		
		innerStatm.body = StatmList.parse();
		Scanner.skip(rightCurlToken);
		Log.leaveParser("</for-statm>");
		
		return innerStatm;
	}
	
	void printTree(){
		Log.wTree("For "); Log.wTree("("); start.printTree();
		Log.wTree("; "); endTest.printTree();
		Log.wTree("; "); change.printTree(); Log.wTree(") "); Log.wTreeLn("{");
		Log.indentTree(); body.printTree();
		Log.outdentTree(); Log.wTree("}");
	}
}

/*
 * A <assign-statm>
 */
class AssignStatm extends Statement {
	Assignment assign;
	
	void check(DeclList curDecls){
		
		assign.check(curDecls);
	}
	
	void genCode(FuncDecl curFunc){
		
		assign.genCode(curFunc);
	}
	
	static AssignStatm parse(){
		Log.enterParser("<Assign-statm>");
		AssignStatm innerStatm = new AssignStatm();
		innerStatm.assign = Assignment.parse();
		Scanner.skip(semicolonToken);
		Log.leaveParser("</assign-statm>");
		return innerStatm;
	}
	
	void printTree(){
		assign.printTree();
		Log.wTreeLn("; ");
	}
}
 
 /*
 * A <call-statm>
 */
class CallStatm extends Statement {
	FunctionCall action;
	
	
	void check (DeclList curDecls){
		//Part *2
		action.check(curDecls);
	}
	
	void genCode(FuncDecl curFunc){
		//Part *2
		
		action.genCode(curFunc);
		
	}
	
	static CallStatm parse(){
		Log.enterParser("<call-statm>");
		
		CallStatm innerStatm = new CallStatm();
		innerStatm.action=FunctionCall.parse();
		Scanner.skip(semicolonToken);
		
		Log.leaveParser("</call-statm>");
		return innerStatm;
	}
	
	void printTree(){
		action.printTree();
		Log.wTreeLn(";");
	}
}
 
/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    //-- Must be changed in Part* 1+2:
	Expression IfExp;
	StatmList IfS, ElseS=null;
	

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		
		IfExp.check(curDecls);
		IfS.check(curDecls);
		if(ElseS!=null){
			ElseS.check(curDecls);
		}
		Log.noteTypeCheck("if (t) ", IfExp.type, "t", lineNum);
		
		
		if(!(IfExp.type instanceof ValueType)){
			error("if-test must be value");
		}
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		
		String s1 = Code.getLocalLabel();
		Code.genInstr("", "", "", "If-Statm Start:");
		IfExp.genCode(curFunc);
		if(ElseS==null){
			Code.genInstr("", "cmpl", "$0, %eax", "");
			Code.genInstr("", "je", s1, "");
			IfS.genCode(curFunc);
		}else{
			String s2 = Code.getLocalLabel();
			
			Code.genInstr("", "cmpl", "$0,%eax", "");
			Code.genInstr("", "je", s2, "");
			
			IfS.genCode(curFunc);
			
			Code.genInstr("", "jmp", s1, "");
			Code.genInstr(s2, "", "", "Else start");
			
			ElseS.genCode(curFunc);
			
			//Code.genInstr("", "", "", "Else end");
		}
		Code.genInstr(s1, "", "", "If-statm end");
    }

    static IfStatm parse() {
		Log.enterParser("<If-statm>");
		IfStatm innerStatm = new IfStatm();
		
		Scanner.skip(ifToken);
		Scanner.skip(leftParToken);
		
		innerStatm.IfExp=Expression.parse();
		
		Scanner.skip(rightParToken);
		Scanner.skip(leftCurlToken);
		
		innerStatm.IfS = StatmList.parse();
		
		Scanner.skip(rightCurlToken);
		
		if(Scanner.curToken==elseToken){
			Log.enterParser("<else-statm>");
			Scanner.skip(elseToken);
			Scanner.skip(leftCurlToken);
			innerStatm.ElseS = StatmList.parse();
			Scanner.skip(rightCurlToken);
			Log.leaveParser("</esle-statm>");
		}
		Log.leaveParser("</if-statm>");
	//-- Must be changed in part* 1:
		return innerStatm;
    }

    @Override void printTree() {
		Log.wTree("if "); Log.wTree("(");
		IfExp.printTree();
		Log.wTree(") "); Log.wTreeLn("{");
		Log.indentTree();
		IfS.printTree();
		
		if(ElseS!=null){
			Log.outdentTree();
			Log.wTree("} "); Log.wTree("else "); Log.wTreeLn("{");
			Log.indentTree();
			ElseS.printTree();
		}
		
		Log.outdentTree();
		Log.wTreeLn("}");
	//-- Must be changed in part* 1:
    }
}


/*
 * A <return-statm>.
 */
class ReturnStatm extends Statement{
	Expression returnExp;
	
	void check(DeclList curDecls){
		//Part *2
		
		returnExp.check(curDecls);
	}
	
	void genCode(FuncDecl curFunc){
		//Part *2
		//La noteTypeCheck her for a få den/dem på slutten av log filen 
		Log.noteTypeCheck("Retrun e; in "+ curFunc.type +" f(...)", returnExp.type, "e", lineNum);
		if(returnExp.type!=Types.intType && (!curFunc.type.isSameType(returnExp.type))){
			error("Return value is the wrong type.");
		}
		returnExp.genCode(curFunc);
		Code.genInstr("", "jmp", curFunc.exitLabel, "Return-Statm");
	}
	
	static ReturnStatm parse(){
		Log.enterParser("<return-statm>");
		ReturnStatm innerReturnStatm = new ReturnStatm();
		Scanner.skip(returnToken);
		innerReturnStatm.returnExp=Expression.parse();
		Scanner.skip(semicolonToken);
		Log.leaveParser("</return-statm>");
		return innerReturnStatm;
	}
	
	void printTree(){
		Log.wTree("return ");
		returnExp.printTree();
		Log.wTreeLn(";");
	}
}

/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {
    Expression test;
    StatmList body;

    @Override void check(DeclList curDecls) {
	test.check(curDecls);
	body.check(curDecls);

	Log.noteTypeCheck("while (t) ...", test.type, "t", lineNum);
	if (test.type instanceof ValueType) {
	    // OK
	} else {
	    error("While-test must be a value.");
	}
    }

    @Override void genCode(FuncDecl curFunc) {
	String testLabel = Code.getLocalLabel(), 
	       endLabel  = Code.getLocalLabel();

	Code.genInstr(testLabel, "", "", "Start while-statement");
	test.genCode(curFunc);
	Code.genInstr("", "cmpl", "$0,%eax", "");
	Code.genInstr("", "je", endLabel, "");
	body.genCode(curFunc);
	Code.genInstr("", "jmp", testLabel, "");
	Code.genInstr(endLabel, "", "", "End while-statement");
    }

    static WhileStatm parse() {
	Log.enterParser("<while-statm>");

	WhileStatm ws = new WhileStatm();
	Scanner.skip(whileToken);
	Scanner.skip(leftParToken);
	ws.test = Expression.parse();
	Scanner.skip(rightParToken);
	Scanner.skip(leftCurlToken);
	ws.body = StatmList.parse();
	Scanner.skip(rightCurlToken);

	Log.leaveParser("</while-statm>");
	return ws;
    }

    @Override void printTree() {
	Log.wTree("while (");  test.printTree();  Log.wTreeLn(") {");
	Log.indentTree();  body.printTree();  Log.outdentTree();
	Log.wTreeLn("}");
    }
}


/*
 * An <Lhs-variable>
 */

class LhsVariable extends SyntaxUnit {
    int numStars = 0;
    Variable var;
    Type type;

    @Override void check(DeclList curDecls) {
	var.check(curDecls);
	type = var.type;
	for (int i = 1;  i <= numStars;  ++i) {
	    Type e = type.getElemType();
	    if (e == null) 
		error("Type error in left-hand side variable!");
	    type = e;
	}
    }

    @Override void genCode(FuncDecl curFunc) {
	var.genAddressCode(curFunc);
	for (int i = 1;  i <= numStars;  ++i)
	    Code.genInstr("", "movl", "(%eax),%eax", "  *");
    }

    static LhsVariable parse() {
	Log.enterParser("<lhs-variable>");

	LhsVariable lhs = new LhsVariable();
	while (Scanner.curToken == starToken) {
	    ++lhs.numStars;  Scanner.skip(starToken);
	}
	Scanner.check(nameToken);
	lhs.var = Variable.parse();

	Log.leaveParser("</lhs-variable>");
	return lhs;
    }

    @Override void printTree() {
	for (int i = 1;  i <= numStars;  ++i) Log.wTree("*");
	var.printTree();
    }
}


/*
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
    Expression firstExpr = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		
		Expression e = firstExpr;
		while(e!=null){
			e.check(curDecls);
			e=e.nextExpr;
		}
    }
	
	//No need
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
    }
	
	//Not sure
    static ExprList parse() {
		Expression lastExpr = null;
		ExprList innerList = new ExprList();
		Log.enterParser("<expr list>");
		if(isOperand(Scanner.curToken) || isPrefixOperator(Scanner.curToken)){
			while(true){
				if(lastExpr==null){
					/*innerList.firstExpr = Expression.parse();
					lastExpr = innerList.firstExpr;*/
					innerList.firstExpr=lastExpr=Expression.parse();
				}else{
					lastExpr.nextExpr=lastExpr=Expression.parse();
					/*
					lastExpr.nextExpr = Expression.parse();
					lastExpr = lastExpr.nextExpr;
					*/
				}
				if(Scanner.curToken !=commaToken){
					break;
				}
				Scanner.skip(commaToken);
			}
		}
	//-- Must be changed in part* 1:
		Log.leaveParser("</expr list>");
		return innerList;
    }

    @Override void printTree() {
		Expression innerExpr = firstExpr;
		
		while(innerExpr!=null){
			innerExpr.printTree();
			if(innerExpr.nextExpr!=null){
				Log.wTree(",");
			}
			innerExpr=innerExpr.nextExpr;
		}
	//-- Must be changed in part* 1:
    }
    
    int antExpr(){
		Expression e = firstExpr;
		int i = 0;
		while(e!=null){
			i++;
			e=e.nextExpr;
		}
		return i;
	}
    //-- Must be changed in part* 1:
}


/*
 * An <expression>
 */
class Expression extends SyntaxUnit {
    Expression nextExpr = null;
    Term firstTerm, secondTerm = null;
    Operator relOpr = null;
    Type type = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
	
	firstTerm.check(curDecls);
	if(relOpr != null){
	    secondTerm.check(curDecls);
	    Log.noteTypeCheck("x "+relOpr.oprToken+" y", firstTerm.type, "x", secondTerm.type, "y", lineNum);
	    
	    if((relOpr.oprToken==Token.equalToken)||(relOpr.oprToken==Token.notEqualToken)){
			if((!(firstTerm.type instanceof ValueType)) || (!(secondTerm.type instanceof ValueType)) || ((!firstTerm.type.isSameType(secondTerm.type)) && (firstTerm.type != Types.intType) && (secondTerm.type != Types.intType))){
				error("Error on operator "+ relOpr.oprToken);
			}
	    }else{
			if((firstTerm.type!=Types.intType) || (secondTerm.type != Types.intType)){
				error("Error on operator "+ relOpr.oprToken);
			}
		}
	    type=Types.intType;
	}else{
	    type = firstTerm.type;
	}
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
	
		firstTerm .genCode(curFunc);
		if(relOpr!=null){
			Code.genInstr("", "pushl", "%eax", "");
			secondTerm.genCode(curFunc);
			relOpr.genCode(curFunc);
		}
    }

    static Expression parse() {
	Log.enterParser("<expression>");

	Expression e = new Expression();
	e.firstTerm = Term.parse();
	if (Token.isRelOperator(Scanner.curToken)) {
	    e.relOpr = RelOpr.parse();
	    e.secondTerm = Term.parse();
	}

	Log.leaveParser("</expression>");
	return e;
    }

    @Override void printTree() {
		firstTerm.printTree();
		if(relOpr!=null){
			relOpr.printTree();
			secondTerm.printTree();
		}
	//-- Must be changed in part* 1:
    }
}


/*
 * A <term>
 */
class Term extends SyntaxUnit {
	Factor firstFac;
	Operator firstOpr=null;
    Type type;
    //-- Must be changed in part* 1+2:

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
	
		firstFac.check(curDecls);
	    if(firstOpr!=null){
			Factor innerFactor = firstFac;
			Factor innerFactor2 = innerFactor.next;
			Operator innerOperator = firstOpr;
			while(innerFactor2!=null){
				innerFactor2.check(curDecls);
				Log.noteTypeCheck(" x "+innerOperator.oprToken +" y", innerFactor.type, "x", innerFactor2.type, "y", lineNum);
		    
				if((innerFactor.type != Types.intType) || (innerFactor2.type != Types.intType)){
					error("Error on type for operator "+innerOperator.oprToken);
				}
		    
				innerFactor = innerFactor2;
				innerFactor2=innerFactor.next;
				innerOperator = innerOperator.nextOpr;
			}
			type=Types.intType;
	    }else{
			type = firstFac.type;
	    }
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		Factor innerFactor = firstFac;
		Operator innerOpr = firstOpr;
		innerFactor.genCode(curFunc);
		innerFactor = innerFactor.next;
		while(innerFactor!=null){
			Code.genInstr("", "pushl", "%eax", "");
			innerFactor.genCode(curFunc);
			innerOpr.genCode(curFunc);
			innerFactor = innerFactor.next;
			innerOpr = innerOpr.nextOpr;
		} 
    }

    static Term parse() {
		
		Log.enterParser("<term>");
		Term innerTerm = new Term();
		innerTerm.firstFac=Factor.parse();
		Factor innerFactor = innerTerm.firstFac;
		TermOpr outerTermOpr = null;
		while(isTermOperator(Scanner.curToken)){
			//TermOpr innerTermOpr = TermOpr.parse();
			if(outerTermOpr==null){
				innerTerm.firstOpr = outerTermOpr = TermOpr.parse();
			}else{
				outerTermOpr.nextOpr = outerTermOpr = TermOpr.parse();
				//outerTermOpr = Term.parse();
				//outerTermOpr.nextOpr = outerTermOpr;
			}
			innerFactor.next = innerFactor = Factor.parse();
			//innerFactor.next = Factor.parse();
			//innerFactor = innerFactor.next;
		}
		Log.leaveParser("</Term>");
		return innerTerm;
		
    }

    @Override void printTree() {
		Factor innerFactor = firstFac;
		Operator innerOpr=firstOpr;
		while(true){
			innerFactor.printTree();
			innerFactor=innerFactor.next;
			if(innerOpr==null){
				break;
			}
			innerOpr.printTree();
			innerOpr=innerOpr.nextOpr;
		}
	//-- Must be changed in part* 1:
    }
}


/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {
    Operator nextOpr = null;
    Token oprToken;

    @Override void check(DeclList curDecls) {}  // Never needed.
}

/*
 * 
 */
class PrefixOpr extends Operator{
	//no need
	void check(DeclList curDecls){
		//part *2
	}
	void genCode(FuncDecl curFunc){
		//part *2
		
		if(oprToken==starToken){
			Code.genInstr("", "movl", "(%eax), %eax", "Prefix *");
		}else if(oprToken==subtractToken){
			Code.genInstr("", "negl", "%eax", "Prefix -");
		}
	}
	
	static PrefixOpr parse(){
		Log.enterParser("<prefix opr>");
		PrefixOpr innerPrefix = new PrefixOpr();
		innerPrefix.oprToken = Scanner.curToken;
		Scanner.skip(innerPrefix.oprToken);
		Log.leaveParser("</prefix opr>");
		return innerPrefix;
	}
	
	void printTree(){
		if(oprToken==starToken){
			Log.wTree("*");
		}else if(oprToken==subtractToken){
			Log.wTree("-");
		}
	}
}


/*
 * A <rel opr> (==, !=, <, <=, > or >=).
 */

class RelOpr extends Operator {
    @Override void genCode(FuncDecl curFunc) {
	Code.genInstr("", "popl", "%ecx", "");
	Code.genInstr("", "cmpl", "%eax,%ecx", "");
	Code.genInstr("", "movl", "$0,%eax", "");
	switch (oprToken) {
	case equalToken:        
	    Code.genInstr("", "sete", "%al", "Test ==");  break;
	case notEqualToken:
	    Code.genInstr("", "setne", "%al", "Test !=");  break;
	case lessToken:
	    Code.genInstr("", "setl", "%al", "Test <");  break;
	case lessEqualToken:
	    Code.genInstr("", "setle", "%al", "Test <=");  break;
	case greaterToken:
	    Code.genInstr("", "setg", "%al", "Test >");  break;
	case greaterEqualToken:
	    Code.genInstr("", "setge", "%al", "Test >=");  break;
	}
    }

    static RelOpr parse() {
	Log.enterParser("<rel opr>");

	RelOpr ro = new RelOpr();
	ro.oprToken = Scanner.curToken;
	Scanner.readNext();

	Log.leaveParser("</rel opr>");
	return ro;
    }

    @Override void printTree() {
	String op = "?";
	switch (oprToken) {
	case equalToken:        op = "==";  break;
	case notEqualToken:     op = "!=";  break;
	case lessToken:         op = "<";   break;
	case lessEqualToken:    op = "<=";  break;
	case greaterToken:      op = ">";   break;
	case greaterEqualToken: op = ">=";  break;
	}
	Log.wTree(" " + op + " ");
    }
}

class TermOpr extends Operator {
	
    //no need
	void check(DeclList curDecls){
	    //Part *2
	}
	
	void genCode(FuncDecl curFunc){
	    //Part *2
	    
	    Code.genInstr("", "movl", "%eax, %ecx", "");
	    Code.genInstr("", "popl", "%eax", "");

	    if(oprToken==addToken){
		Code.genInstr("", "addl", "%ecx,%eax", "Compute +");
	    }else if(oprToken==subtractToken){
		Code.genInstr("", "subl", "%ecx, %eax", "Compute -");
	    }
	}
	
	static TermOpr parse(){
		Log.enterParser("<term-opr>");
		TermOpr innerTermOpr = new TermOpr();
		innerTermOpr.oprToken=Scanner.curToken;
		Scanner.skip(innerTermOpr.oprToken);
		Log.leaveParser("</term-opr>");
		return innerTermOpr;
	}
	
	void printTree(){
		if(oprToken==addToken){
			Log.wTree("+");
		}else{
			Log.wTree("-");
		}
	}

}


/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operand nextOperand = null;
    Type type;

    static Operand parse() {
	Log.enterParser("<operand>");

	Operand o = null;
	if (Scanner.curToken == numberToken) {
	    o = Number.parse();
	} else if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
	    o = FunctionCall.parse();
	} else if (Scanner.curToken == nameToken) {
	    o = Variable.parse();
	} else if (Scanner.curToken == ampToken) {
	    o = Address.parse();
	} else if (Scanner.curToken == leftParToken) {
	    o = InnerExpr.parse();  
	} else {
	    Error.expected("An operand");
	}

	Log.leaveParser("</operand>");
	return o;
    }
}


/*
 * A <function call>.
 */
class FunctionCall extends Operand {
    //-- Must be changed in part* 1+2:
	String funcName;
	ExprList funcParam;
	FuncDecl myFuncDecl = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part *2:
		
		Declaration innerDecl = curDecls.findDecl(funcName, this);
		innerDecl.checkWhetherFunction(funcParam.antExpr(), this);
		
		myFuncDecl = (FuncDecl) innerDecl;
		type=innerDecl.type;
		
		Expression innerExpr = funcParam.firstExpr;
		Declaration innerDecl2 = myFuncDecl.funcParam.firstDecl;
		
		int i = 0;
		
		while(innerExpr!=null){
			i++;
			innerExpr.check(curDecls);
			
			Log.noteTypeCheck("Paramater #"+i+" in call on "+funcName, innerExpr.type, "actual", innerDecl2.type, "formal", lineNum);
			
			if((innerExpr.type != Types.intType) &&(!innerExpr.type.isSameType(innerDecl2.type))){
				error("Type error for parameter #"+i);
			}
			innerExpr = innerExpr.nextExpr;
			innerDecl2 = innerDecl2.nextDecl;
		}
		
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		
		int i = 0;
		if(funcParam.firstExpr != null){
			i=genParameters(i, funcParam.firstExpr, curFunc);
		}
		
		Code.genInstr("", "call", myFuncDecl.assemblerName, "Call "+myFuncDecl.name);
		
		if(funcParam.firstExpr != null){
			Code.genInstr("", "addl", "$"+i+",%esp", "Remove parameters");
		}
    }

    static FunctionCall parse() {
		Log.enterParser("<function-call>");
		
		FunctionCall innerFunc = new FunctionCall();
		innerFunc.funcName=Scanner.curName;
		
		Scanner.skip(nameToken);
		Scanner.skip(leftParToken);
		
		innerFunc.funcParam = ExprList.parse();
		Scanner.skip(rightParToken);
	//-- Must be changed in part* 1:
		
		Log.leaveParser("</function-call>");
		return innerFunc;
    }

    @Override void printTree() {
		Log.wTree(funcName); Log.wTree("("); 
		funcParam.printTree(); Log.wTree(")");
	//-- Must be changed in part* 1:
    }
    
    private int genParameters(int paramPos, Expression expr, FuncDecl myCurFunc){
		int i = 0;
		if(expr.nextExpr != null){
			i = genParameters(paramPos+1, expr.nextExpr, myCurFunc);
		}
		expr.genCode(myCurFunc);
		Code.genInstr("", "pushl", "%eax", "Push parameter #" +paramPos);
		i+=4;
		return i;
	}
    //-- Must be changed in part* 1+2:
}


/*
 * A <number>.
 */
class Number extends Operand {
    int numVal;

   @Override void check(DeclList curDecls) {
       //-- Must be changed in part *2:
       
       type = Types.intType;
    }
	
    @Override void genCode(FuncDecl curFunc) {
	Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal); 
    }

    static Number parse() {
		Number innerNum = new Number();
		Log.enterParser("<number>");
		innerNum.numVal=Scanner.curNum;
		Scanner.skip(numberToken);
	//-- Must be changed in part* 1:
		Log.leaveParser("</number>");
		return innerNum;
    }

    @Override void printTree() {
		Log.wTree("" + numVal);
    }
}


/*
 * A <variable>.
 */

class Variable extends Operand {
    String varName;
    VarDecl declRef = null;
    Expression index = null;

    @Override void check(DeclList curDecls) {
		
		Declaration d = curDecls.findDecl(varName,this);
		d.checkWhetherVariable(this);
		declRef = (VarDecl)d;

	if (index == null) {
	    type = d.type;
	} else {
	    index.check(curDecls);
	    Log.noteTypeCheck("a[e]", d.type, "a", index.type, "e", lineNum);

	    if (index.type == Types.intType) {
		// OK
	    } else {
		error("Only integers may be used as index.");
	    }
	    if (d.type.mayBeIndexed()) {
		// OK
	    } else {
		error("Only arrays and pointers may be indexed.");
	    }
	    type = d.type.getElemType();
	}
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part *2:
		
		if(index!=null){
			index.genCode(curFunc);
			if((declRef.type instanceof ArrayType)){
				Code.genInstr("", "leal", declRef.assemblerName +", %edx", varName +"[]");
			}else{
				Code.genInstr("", "movl", declRef.assemblerName +", %edx", varName +"[]");
			}
			Code.genInstr("", "movl", "(%edx, %eax, 4),%eax", "");
		}else{
			if(declRef.type instanceof ArrayType){
				Code.genInstr("", "leal", declRef.assemblerName+", %eax", varName);
			}else{
				Code.genInstr("", "movl", declRef.assemblerName+", %eax", varName);
			}
		}
    }

    void genAddressCode(FuncDecl curFunc) {
	// Generate code to load the _address_ of the variable
	// rather than its value.

	if (index == null) {
	    Code.genInstr("", "leal", declRef.assemblerName+",%eax", varName);
	} else {
	    index.genCode(curFunc);
	    if (declRef.type instanceof ArrayType) {
		Code.genInstr("", "leal", declRef.assemblerName+",%edx", 
			      varName+"[...]");
	    } else {
		Code.genInstr("", "movl", declRef.assemblerName+",%edx", 
			      varName+"[...]");
	    }
	    Code.genInstr("", "leal", "(%edx,%eax,4),%eax", "");
	}
    }

    static Variable parse() {
		Log.enterParser("<variable>");
		Variable innerVar = new Variable();
		innerVar.varName = Scanner.curName;
		Scanner.skip(nameToken);
		//innerVar.index=Expression.parse();
		if(Scanner.curToken == leftBracketToken){
			Scanner.skip(leftBracketToken);
			innerVar.index=Expression.parse();
			Scanner.skip(rightBracketToken);
		}
		//-- Must be changed in part* 1:
		Log.leaveParser("</variable>");
		return innerVar;
    }

    @Override void printTree() {
		Log.wTree(varName);
		if(index!=null){ 
			Log.wTree("["); index.printTree(); Log.wTree("]");
		}
	//-- Must be changed in part* 1:
    }
}


/*
 * An <address>.
 */
class Address extends Operand {
    Variable var;

    @Override void check(DeclList curDecls) {
	var.check(curDecls);
	type = new PointerType(var.type);
    }

    @Override void genCode(FuncDecl curFunc) {
	var.genAddressCode(curFunc);
    }

    static Address parse() {
	Log.enterParser("<address>");

	Address a = new Address();
	Scanner.skip(ampToken);
	a.var = Variable.parse();

	Log.leaveParser("</address>");
	return a;
    }

    @Override void printTree() {
	Log.wTree("&");  var.printTree();
    }
}


/*
 * An <inner expr>.
 */
class InnerExpr extends Operand {
    Expression expr;

    @Override void check(DeclList curDecls) {
	expr.check(curDecls);
	type = expr.type;
    }

    @Override void genCode(FuncDecl curFunc) {
	expr.genCode(curFunc);
    }

    static InnerExpr parse() {
	Log.enterParser("<inner expr>");

	InnerExpr ie = new InnerExpr();
	Scanner.skip(leftParToken);
	ie.expr = Expression.parse();
	Scanner.skip(rightParToken);

	Log.leaveParser("</inner expr>");
	return ie;
    }

    @Override void printTree() {
	Log.wTree("(");  expr.printTree();  Log.wTree(")");
    }
}
