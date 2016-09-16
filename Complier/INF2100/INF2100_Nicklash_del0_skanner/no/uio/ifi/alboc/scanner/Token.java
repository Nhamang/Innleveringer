package no.uio.ifi.alboc.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { 
    addToken, ampToken, assignToken, 
    commaToken, 
    divideToken,
    elseToken, eofToken, equalToken, 
    forToken, 
    greaterEqualToken, greaterToken, 
    ifToken, intToken, 
    leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, 
    nameToken, notEqualToken, numberToken, 
    returnToken, rightBracketToken, rightCurlToken, rightParToken, 
    semicolonToken, starToken, subtractToken, 
    whileToken;

    public static boolean isFactorOperator(Token t) {
	//-- Must be changed in part 0:
		if(t==divideToken ||t==starToken){
			return true;
		}else{
			return false;
		}
    }

    public static boolean isTermOperator(Token t) {
	//-- Must be changed in part 0:
		if(t==addToken ||t==subtractToken){
			return true;
		}else{
			return false;
		}
    }

    public static boolean isPrefixOperator(Token t) {
	//-- Must be changed in part 0:
		if(t==starToken ||t==subtractToken){
			return true;
		}else{
			return false;
		}
    }

    public static boolean isRelOperator(Token t) {
	//-- Must be changed in part 0:
		if(t==equalToken ||t==greaterEqualToken ||t==greaterToken ||t==lessEqualToken ||t==lessToken ||t==notEqualToken){
			return true;
		}else{
			return false;
		}
    }

    public static boolean isOperand(Token t) {
	//-- Must be changed in part 0:
		if(t==ampToken || t==leftCurlToken || t==nameToken || t==numberToken){
			return true;
		}else{
			return false;
		}
    }
}
