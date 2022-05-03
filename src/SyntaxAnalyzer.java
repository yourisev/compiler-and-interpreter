import java.util.ArrayList;
import java.util.List;

/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

// Base class for parsing
public class SyntaxAnalyzer {
    // Exception thrown whenever there is a parsing error
    private static class ParseError extends RuntimeException {
    }

    // List of tokens used for parsing
    private final List<Token> listOfTokens;
    // List of keywords
    private List<Token> listOfKeywords = new ArrayList<>();
    // List of statements generated from parsing
    private final List<Statement> listOfStatements = new ArrayList<>();
    // Index of the current token in the list of tokens
    private int currentTokenIndex = 0;
    // Variable used to store the name of the function
    private String functionName = "";

    // Constructor
    SyntaxAnalyzer(List<Token> tokens) {
        this.listOfTokens = tokens;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Token> getListOfKeywords() {
        return listOfKeywords;
    }

    // Method for parsing
    List<Statement> parse() {
        // Invoke the first grammar rule
        program();
        return listOfStatements;
    }

    // Subprogram for the non-terminal <program>
    private void program() {
        // First grammar rule: <program> → function id ( ) <block> end
        List<TokenType> tokenTypeList = new ArrayList<>();
        // Add the first four token types for the first grammar rule
        tokenTypeList.add(TokenType.FUNCTION);
        tokenTypeList.add(TokenType.IDENTIFIER);
        tokenTypeList.add(TokenType.LEFT_PARANTHESIS);
        tokenTypeList.add(TokenType.RIGHT_PARANTHESIS);

        /*Verify whether the first four tokens in the source code corresponds
        to the ones in the first grammar rule*/
        while (currentTokenIndex < 4) {
            consumeCurrentToken(tokenTypeList.get(currentTokenIndex), "Syntax error");
        }
        // Obtain and store the function's name
        functionName = listOfTokens.get(1).getLexeme();

        // Check whether there is a new block after the function's parenthesis
        consumeCurrentToken(TokenType.BLOCK_CHARACTER, "a new block expected");

        // Invoke subprogram for the second grammar rule
        block(null);

        // Check whether the function has the 'end' keyword
        if (!matchCurrentToken(TokenType.END) && listOfKeywords.get(listOfKeywords.size() - 1).getType()
                == TokenType.FUNCTION) {
            throw generateError(listOfTokens.get(currentTokenIndex), "Expected 'end' keyword at " +
                    "the end of function");
        }

    }

    // Subprogram for the non-terminal <block>
    // New block represents block for if , repeat or while statements
    private void block(List<Statement> newBlock) {
        // Second grammar rule: <block> → <statement> | <statement> <block>
        try {
            // Invoke subprogram for the third grammar rule
            Statement statement = statement();
            if (statement != null) {
                /*Add valid statements only to the generated list of statements
                returned by the parser in case there is no if, while or repeat block
                encountered while parsing*/
                if (newBlock == null)
                    listOfStatements.add(statement);
                    /*Add valid statements only to the corresponding if, while or repeat block*/
                else
                    newBlock.add(statement);
            }
            // Invoking the subprogram if a new block is encountered
            if (matchCurrentToken(TokenType.BLOCK_CHARACTER))
                block(newBlock);
        } catch (ParseError error) {
            // Recover after encountering an error and continue parsing
            recoverAfterErrorHasOccurred();
        }
    }

    private Statement statement() {
        /* Third grammar rule is: <statement> → <if_statement> | <assignment_statement> | <while_statement>
         | <print_statement> | <repeat_statement><statement> → <if_statement> | <assignment_statement>
           | <while_statement> | <print_statement> | <repeat_statement>*/

        if (matchCurrentToken(TokenType.IF)) {
            // Add Token for if statement to the list of keywords whenever it is encountered
            listOfKeywords.add(new Token(TokenType.IF, "if", null, currentTokenIndex));
            // Invoke subprogram for the non-terminal <if_statement>
            return ifStatement();

        } else if (matchCurrentToken(TokenType.WHILE)) {
            // Add Token for if statement to the list of keywords whenever it is encountered
            listOfKeywords.add(new Token(TokenType.WHILE, "while", null, currentTokenIndex));
            // Invoke subprogram for the non-terminal <while_statement>
            return whileStatement();

        } else if (matchCurrentToken(TokenType.PRINT))
            // Invoke subprogram for the non-terminal <print_statement>
            return printStatement();

        else if (matchCurrentToken(TokenType.IDENTIFIER))
            // Invoke subprogram for the non-terminal <assignment_statement>
            return assignmentStatement();

        else if (matchCurrentToken(TokenType.REPEAT))
            // Invoke subprogram for the non-terminal <repeat_statement>
            return repeatStatement();

        else if (matchCurrentToken(TokenType.ELSE)) {
            // Move backwards to previous token when the 'else' keyword is encountered
            currentTokenIndex--;
            return null;

        } else if (matchCurrentToken(TokenType.END)) {
            // Match the 'end' keyword to the nearest if or while keyword
            if (listOfKeywords.get(listOfKeywords.size() - 1).getType()
                    == TokenType.IF || listOfKeywords.get(listOfKeywords.size() - 1).getType()
                    == TokenType.WHILE) {

               /* Remove the last added keyword corresponding to the if, while or repeat
                which matches the encountered 'end' keyword*/
                listOfKeywords.remove(listOfKeywords.size() - 1);

                // Move backwards to previous token when the 'end' keyword is encountered
                currentTokenIndex--;
                return null;
            }
        }
        // Ignore empty blocks after if or while keywords
        else if (matchCurrentToken(TokenType.BLOCK_CHARACTER) &&
                (listOfKeywords.get(listOfKeywords.size() - 1).getType()
                        == TokenType.IF || listOfKeywords.get(listOfKeywords.size() - 1).getType()
                        == TokenType.WHILE)) {
            currentTokenIndex--;
            return null;
        }
        // Ignore empty blocks after function keyword
        else if (matchCurrentToken(TokenType.BLOCK_CHARACTER) && listOfKeywords.get(listOfKeywords.size() - 1).getType()
                == TokenType.FUNCTION) {
            return null;
        }
        // Generate a syntax error if none of the conditions are met
        throw generateError(getCurrentToken(), "Expect a statement.");
    }

    private Statement ifStatement() {
        // Fourth grammar rule is: <if_statement> → if <boolean_expression> then <block> else <block> end

        // Obtain and store condition for if statement
        Expression condition = booleanExpression();
        // Verify whether a block is found after if condition
        consumeCurrentToken(TokenType.BLOCK_CHARACTER, "Expect new block after if condition.");
        List<Statement> thenBranch = new ArrayList<>();
        // Obtain and store all the instructions to be executed if condition is valid
        block(thenBranch);
        //consumeCurrentToken(TokenType.BLOCK_CHARACTER, "Expect new block after if statements.");

        List<Statement> elseBranch = null;
        if (matchCurrentToken(TokenType.ELSE)) {
            consumeCurrentToken(TokenType.BLOCK_CHARACTER, "Expect new block after else statement.");
            elseBranch = new ArrayList<>();
            // Obtain and store all the instructions to be repeated in the else block
            block(elseBranch);
            ;
        }
        //consumeCurrentToken(TokenType.BLOCK_CHARACTER, "");

        // Verify whether the 'end' keyword is found after if statements
        consumeCurrentToken(TokenType.END, "Expect 'end' keyword after if statements.");

        // Return statement of type if
        return new Statement.IFStatement(condition, thenBranch, elseBranch);
    }

    private Statement whileStatement() {
        // Fifth grammar rule is: <while_statement> → while <boolean_expression> do <block> end

        // Obtain and store condition for while loop
        Expression condition = booleanExpression();
        // Verify whether a block is found after while condition
        consumeCurrentToken(TokenType.BLOCK_CHARACTER, "Expect new block after while condition.");
        List<Statement> body = new ArrayList<>();
        // Obtain and store all the instructions to be repeated by the loop
        block(body);
        //consumeCurrentToken(TokenType.BLOCK_CHARACTER, "");

        // Verify whether the 'end' keyword is found after while statements
        consumeCurrentToken(TokenType.END, "Expect 'end' keyword after while statements.");

        // Return statement of type while
        return new Statement.WhileStatement(condition, body);
    }

    private Statement assignmentStatement() {
        // Sixth grammar rule is: <assignment_statement> -> id <assignment_operator> <arithmetic_expression>

        // Obtain and store the variable name
        Token variableName = getPreviousToken(), equalOperator;

        if (matchCurrentToken(TokenType.ASSIGNMENT_OPERATOR)) {
            // Obtain and store the assignment operator
            equalOperator = getPreviousToken();
            // Obtain and store expression on the right of the assignment operator
            Expression arithmeticExpression = arithmeticExpression();

            // Return statement of type assignment
            return new Statement.AssignmentStatement(variableName, equalOperator, arithmeticExpression);
        }

        // Generate a syntax error for assignment
        throw generateError(getCurrentToken(), "Expect '=' after variable.");
    }

    private Statement repeatStatement() {
        // Seventh grammar rule is: <repeat_statement> -> repeat <block> until <boolean_expression>

        // Create to store the statement needed to be repeated
        List<Statement> statementToRepeat = new ArrayList<>();
        // Verify whether a block is found after 'repeat' keyword
        consumeCurrentToken(TokenType.BLOCK_CHARACTER, "Expect new block after while condition.");
        // Obtain and store the statements needed to be repeated
        block(statementToRepeat);
        // Obtain and store the condition for repeating the statements
        Expression condition = booleanExpression();

        // Return statement of type repeat
        return new Statement.RepeatStatement(statementToRepeat, condition);
    }


    private Statement printStatement() {
        // Eight grammar rule is: <print_statement> → print ( <arithmetic_expression> )

        // Verify the presence of left parenthesis after 'print' keyword
        consumeCurrentToken(TokenType.LEFT_PARANTHESIS, "Expect '(' after print.");
        // Obtain and store expression to be printed by print function
        Expression arithmeticExpression = arithmeticExpression();
        // Verify the presence of right parenthesis after 'print' keyword
        consumeCurrentToken(TokenType.RIGHT_PARANTHESIS, "Expect ')' after expression.");

        // Return statement of type print
        return new Statement.PrintStatement(arithmeticExpression);
    }


    private Expression booleanExpression() {
        // Ninth grammar rule is: <boolean_expression> → <relative_op> <arithmetic_expression> <arithmetic_expression>

        // Obtain and store expression on the left side of the logical operator
        Expression arithmeticExpression = arithmeticExpression();
        while (matchCurrentToken(TokenType.GREATER_THAN_OPERATOR, TokenType.GREATER_THAN_OR_EQUAL_TO_OPERATOR,
                TokenType.LESS_THAN_OPERATOR, TokenType.LESS_THAN_OR_EQUAL_TO_OPERATOR, TokenType.EQUAL_TO_OPERATOR,
                TokenType.NOT_EQUAL_TO_OPERATOR)) {
            // Obtain and store the logical operator
            Token relativeOperator = getPreviousToken();
            // Obtain and store expression on the right side of the logical operator
            Expression rightArithmeticExpression = arithmeticExpression();

            // Create expression of type boolean
            arithmeticExpression = new Expression.BooleanExpression(arithmeticExpression, relativeOperator, rightArithmeticExpression);
        }
        // Return expression of type boolean
        return arithmeticExpression;
    }

    private Expression arithmeticOperator() {
        // Twelfth grammar rule is: <arithmetic_op> → add_operator | sub_operator | mul_operator | div_operator

        // Return expression of type addition operator if '+' is encountered in expression
        if (matchCurrentToken(TokenType.ADDITION_OPERATOR)) {
            return new Expression.ArithmeticOperator(getPreviousToken());
        }

        // Return expression of type subtraction operator if '-' is encountered in expression
        if (matchCurrentToken(TokenType.SUBTRACTION_OPERATOR)) {
            return new Expression.ArithmeticOperator(getPreviousToken());
        }

        // Return expression of type multiplication operator if '*' is encountered in expression
        if (matchCurrentToken(TokenType.MULTIPLICATION_OPERATOR)) {
            return new Expression.ArithmeticOperator(getPreviousToken());
        }

        // Return expression of type division operator if '/' is encountered in expression
        if (matchCurrentToken(TokenType.DIVISION_OPERATOR)) {
            return new Expression.ArithmeticOperator(getPreviousToken());
        }

        // Generate syntax error otherwise
        throw generateError(getCurrentToken(), "Expect an arithmetic operator.");
    }

    private Expression arithmeticExpression() {
        // Eleventh grammar rule is: <arithmetic_expression> → <id> | <literal_integer> | <arithmetic_op>

        // Return expression of type literal if integer literal is encountered in expression
        if (matchCurrentToken(TokenType.INTEGER_LITERAL)) {
            return new Expression.Literal(getPreviousToken().getLiteral());
        }

        // Return expression of type identifier if identifier is encountered in expression
        if (matchCurrentToken(TokenType.IDENTIFIER)) {
            return new Expression.Identifier(getPreviousToken());
        }

        // Return expression of type arithmetic operator if operator is encountered in expression
        arithmeticOperator();

        // Generate syntax error otherwise
        throw generateError(getCurrentToken(), "Expect an expression.");
    }


    // Check and return the token if its type matches with the given type
    private Token consumeCurrentToken(TokenType type, String message) {
        if (checkTokenType(type)) return moveToNextToken();

        // Generate error if token type doesn't match
        throw generateError(getCurrentToken(), message);
    }

    // Compares the type of the token currently being parsed with the given type
    private boolean matchCurrentToken(TokenType... types) {
        for (TokenType type : types) {
            if (checkTokenType(type)) {
                moveToNextToken();
                return true;
            }
        }

        return false;
    }

    // check the token type of the token currently being parsed
    private boolean checkTokenType(TokenType type) {
        if (endOfFile()) return false;
        return getCurrentToken().getType() == type;
    }

    // Move to the next token for parsing
    private Token moveToNextToken() {
        if (!endOfFile()) currentTokenIndex++;
        return getPreviousToken();
    }

    // Verify whether the end of the file is reached
    private boolean endOfFile() {
        return getCurrentToken().getType() == TokenType.EOF;
    }

    // Get token currently being parsed
    private Token getCurrentToken() {
        return listOfTokens.get(currentTokenIndex);
    }

    // Get token previously parsed
    private Token getPreviousToken() {
        return listOfTokens.get(currentTokenIndex - 1);
    }

    // Generate new syntax error
    private ParseError generateError(Token token, String message) {
        Main.generateSyntaxError(token, message);
        return new ParseError();
    }

    // Method used to recover and continue parsing whenever a syntax error occurs
    private void recoverAfterErrorHasOccurred() {
        moveToNextToken();

        while (!endOfFile()) {
            if (getPreviousToken().getType() == TokenType.BLOCK_CHARACTER) {
                //currentTokenIndex--;
                return;
            }

            switch (getCurrentToken().getType()) {
                //case CLASS:
                case FUNCTION:
                    //case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case END:
                    return;
            }

            moveToNextToken();
        }
    }

}
