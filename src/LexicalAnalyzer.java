import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

public class LexicalAnalyzer {
    // collected characters from the source code or string representing the source code
    private final String sourceCode;

    // List of tokens generated from the source code
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;

    // Keyword table
    static {
        keywords = new HashMap<>();
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("print", TokenType.PRINT);
        keywords.put("while", TokenType.WHILE);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("end", TokenType.END);
        keywords.put("do", TokenType.DO);
        keywords.put("then", TokenType.THEN);
        keywords.put("repeat", TokenType.REPEAT);
        keywords.put("until", TokenType.UNTIL);
    }

    // Character at the beginning of the current lexeme being read
    private int startCharacterIndex = 0;

    // Character currently being read in the current lexeme
    private int currentCharacterIndex = 0;

    // Line currently being read in the source code
    private int currentLine = 1;

    // Scanner constructor based on source code as input
    public LexicalAnalyzer(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    List<Token> scanTokens() {
        // Scan all the characters of the source code until end of fie is reached
        while (!endOfFile()) {
            // We are at the beginning of the next lexeme.
            startCharacterIndex = currentCharacterIndex;
            scanToken();
        }
        // Add the end of file token after all characters have been read
        tokens.add(new Token(TokenType.EOF, "", null, currentLine));
        return tokens;
    }

    // Determines if all the characters have been read or not
    private boolean endOfFile() {
        return currentCharacterIndex >= sourceCode.length();
    }

    private void scanToken() {
        char nextCharacter = getChar();
        switch (nextCharacter) {
            case ')':
                generateToken(TokenType.RIGHT_PARANTHESIS);
                break;
            case '(':
                generateToken(TokenType.LEFT_PARANTHESIS);
                break;
            case '-':
                generateToken(TokenType.SUBTRACTION_OPERATOR);
                break;
            case '+':
                generateToken(TokenType.ADDITION_OPERATOR);
                break;
            case '*':
                generateToken(TokenType.MULTIPLICATION_OPERATOR);
                break;
            case '!':
                generateToken(match('=') ? TokenType.NOT_EQUAL_TO_OPERATOR : TokenType.NOT_OPERATOR);
                break;
            case '=':
                generateToken(match('=') ? TokenType.EQUAL_TO_OPERATOR : TokenType.ASSIGNMENT_OPERATOR);
                break;
            case '<':
                generateToken(match('=') ? TokenType.LESS_THAN_OR_EQUAL_TO_OPERATOR : TokenType.LESS_THAN_OPERATOR);
                break;
            case '>':
                generateToken(match('=') ? TokenType.GREATER_THAN_OR_EQUAL_TO_OPERATOR : TokenType.GREATER_THAN_OPERATOR);
                break;
            case '/':
                if (match('/')) {
                    // Read comment until the end of the line.
                    while (peekNextChar() != '\n' && !endOfFile()) getChar();
                } else {
                    generateToken(TokenType.DIVISION_OPERATOR);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                if (match('\t')) {
                    // Read comment until the end of the line.
                    generateToken(TokenType.BLOCK_CHARACTER);
                } /*else {
                    generateToken(TokenType.NEW_LINE_CHARACTER);
                }*/
                //generateToken(TokenType.NEW_LINE_CHARACTER);
                currentLine++;
                break;
            default:
                if (isDigit(nextCharacter)) {
                    getNumber();
                } else if (isAlpha(nextCharacter)) {
                    getIdentifier();
                } else {
                    Main.generateError(currentLine, "Unexpected character.");
                }
                break;
        }
    }

    // Get the next character input
    private char getChar() {
        return sourceCode.charAt(currentCharacterIndex++);
    }

    // Generate token without literals from the current lexeme
    private void generateToken(TokenType type) {
        generateToken(type, null);
    }

    // Generate token from the current lexeme
    private void generateToken(TokenType type, Object literal) {
        String text = sourceCode.substring(startCharacterIndex, currentCharacterIndex);
        if (type == TokenType.BLOCK_CHARACTER)
            text = "\\t";
        /*else if(type == TokenType.NEW_LINE_CHARACTER)
            text = "\\n";*/
        tokens.add(new Token(type, text, literal, currentLine));
    }

    // Recognize equality and comparison operators by matching the second character in the lexeme with the expected character
    private boolean match(char expectedCharacter) {
        if (endOfFile()) return false;
        if (sourceCode.charAt(currentCharacterIndex) != expectedCharacter) return false;

        currentCharacterIndex++;
        return true;
    }

    // Return next character in lexeme without storing
    private char peekNextChar() {
        if (endOfFile()) return '\0';
        return sourceCode.charAt(currentCharacterIndex);
    }

    // Determine whether the current character is a digit or not
    private boolean isDigit(char currentCharacter) {
        return currentCharacter >= '0' && currentCharacter <= '9';
    }

    // Read all the digits of the integer literal in case the literal has more than one digit
    private void getNumber() {
        while (isDigit(peekNextChar())) getChar();


        generateToken(TokenType.INTEGER_LITERAL,
                Integer.parseInt(sourceCode.substring(startCharacterIndex, currentCharacterIndex)));
    }

    // Read all characters of the identifier
    private void getIdentifier() {
        while (isAlphaNumeric(peekNextChar())) getChar();
        generateToken(lookUp());

    }

    // Determine whether the character is alphabetic
    private boolean isAlpha(char currentCharacter) {
        return (currentCharacter >= 'a' && currentCharacter <= 'z') ||
                (currentCharacter >= 'A' && currentCharacter <= 'Z') ||
                currentCharacter == '_';
    }

    // Determine whether the character is alphanumeric
    private boolean isAlphaNumeric(char currentCharacter) {
        return isAlpha(currentCharacter) || isDigit(currentCharacter);
    }

    // Determine whether the current character string is a keyword or not
    private TokenType lookUp() {
        String text = sourceCode.substring(startCharacterIndex, currentCharacterIndex);
        TokenType type = keywords.get(text);
        if (type == null) type = TokenType.IDENTIFIER;
        return type;
    }
}
