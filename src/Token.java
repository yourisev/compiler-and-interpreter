/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

public class Token {
    // The type of the token
    private final TokenType type;

    // The lexeme belonging to this token category
    private final String lexeme;

    // The literal associated to the token
    private final Object literal;

    // The current line in the source code in which the token is found
    private final int line;

    // Token constructor
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return type + " " + type.ordinal() + " " + lexeme + " " + literal;
    }
}
