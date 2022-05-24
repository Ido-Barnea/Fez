package fez.main;

import static fez.main.Constants.*;
import java.util.ArrayList;
import fez.main.Exceptions.Exception;
import fez.main.Exceptions.IllegalCharacterException;
import fez.main.Exceptions.InvalidSyntaxException;
import fez.main.Objects.Position;
import fez.main.Objects.Token;
import fez.main.Objects.TokenType;
import fez.main.Objects.ResultObjects.LexerResult;

public class Lexer {

    private final String text;
    private final Position currentCharacterPosition;
    private Character currentCharacter;
    private Exception exception;

    public Lexer(String fileName, String text) {
        this.text = text;
        this.currentCharacterPosition = new Position(-1, 0, -1, fileName, text);
        this.currentCharacter = null;

        advance();
    }

    private void advance() {
        currentCharacterPosition.advance(currentCharacter);

        if (currentCharacterPosition.index() < text.length()) currentCharacter = text.charAt(currentCharacterPosition.copy().index());
        else currentCharacter = null;
    }

    public LexerResult createTokens() {
        ArrayList<Token> tokens = new ArrayList<>();
        
        while (currentCharacter != null) {
            if (" \t".indexOf(currentCharacter) != -1) advance(); // If character is space or tab
            else if (DIGITS.indexOf(currentCharacter) != -1) tokens.add(createNumberToken()); // If character is a digit
            else if (POSSIBLE_IDENTIFIER_CHARACTERS.indexOf(currentCharacter) != -1 || KEYWORDS.contains(currentCharacter.toString())) tokens.add(createIdentifier()); // If character is a letter
            else {
                switch (currentCharacter) {
                    case ';':
                    case '\n':
                        tokens.add(new Token(TokenType.NEWLINE, currentCharacterPosition.copy()));
                        break;
                    case '+':
                        createPlusToken(tokens);
                        break;
                    case '-':
                        createMinusToken(tokens);
                        break;
                    case '*':
                        tokens.add(createMultiplyOrPowerToken());
                        break;
                    case '/':
                        tokens.add(createDivisionOrComment());
                        break;
                    case '%':
                        tokens.add(new Token(TokenType.MODULO, currentCharacterPosition.copy()));
                        break;
                    case '(':
                        tokens.add(new Token(TokenType.LPAREN, currentCharacterPosition.copy()));
                        break;
                    case ')':
                        tokens.add(new Token(TokenType.RPAREN, currentCharacterPosition.copy()));
                        break;
                    case '[':
                        tokens.add(new Token(TokenType.LSQUARE, currentCharacterPosition.copy()));
                        break;
                    case ']':
                        tokens.add(new Token(TokenType.RSQUARE, currentCharacterPosition.copy()));
                        break;
                    case '{':
                        tokens.add(new Token(TokenType.LCURLY, currentCharacterPosition.copy()));
                        break;
                    case '}':
                        tokens.add(new Token(TokenType.RCURLY, currentCharacterPosition.copy()));
                        break;
                    case '=':
                        tokens.add(createEqualsToken());
                        break;
                    case '!':
                        tokens.add(createNotToken());
                        break;
                    case '&':
                        tokens.add(createAndToken());
                        break;
                    case '|':
                        tokens.add(createOrToken());
                        break;
                    case '<':
                        tokens.add(createLessThanToken());
                        break;
                    case '>':
                        tokens.add(createGreaterThanToken());
                        break;
                    case '"':
                        tokens.add(createString());
                        break;
                    case ',':
                        tokens.add(new Token(TokenType.COMMA, currentCharacterPosition.copy()));
                        break;
                    case ':':
                        tokens.add(new Token(TokenType.COLON, currentCharacterPosition.copy()));
                        break;
                    default:
                        return new LexerResult(new IllegalCharacterException(currentCharacterPosition.copy(), "'" + currentCharacter + "'"));
                }

                advance();
            }
        }

        return new LexerResult(tokens, exception);
    }

    private Token createString() {
        StringBuilder string = new StringBuilder();
        Position stringPosition = currentCharacterPosition.copy();
        boolean escapeCharacter = false;

        advance();

        while (currentCharacter != null && (currentCharacter != '"' || escapeCharacter)) {
            if (escapeCharacter) {
                string.append(escapeCharacters.getOrDefault(currentCharacter, currentCharacter));
                escapeCharacter = false;
            }
            else {
                if (currentCharacter == '\\') escapeCharacter = true;
                else string.append(currentCharacter);
            }

            advance();
        }

        if (currentCharacter == null) {
            setException(new InvalidSyntaxException(currentCharacterPosition.copy(), "Expected '\"'"));
            return null;
        }
        
        return new Token(TokenType.STRING, string.toString(), stringPosition);
    }

    private Token createNumberToken() {
        StringBuilder numStr = new StringBuilder();
        int dotCounter = 0;

        while (currentCharacter != null && (DIGITS + ".").indexOf(currentCharacter) != -1) {
            if (currentCharacter == '.') {
                if (dotCounter > 0) break;
                dotCounter++;
                numStr.append('.');
            } else numStr.append(currentCharacter);

            advance();
        }

        if (dotCounter == 0) return new Token(TokenType.INT, Integer.parseInt(numStr.toString()), currentCharacterPosition.copy());
        else return new Token(TokenType.FLOAT, Float.parseFloat(numStr.toString()), currentCharacterPosition.copy());
    }

    private Token createIdentifier() {
        StringBuilder identifier = new StringBuilder();

        if (KEYWORDS.contains(currentCharacter.toString())) {
            identifier = new StringBuilder(currentCharacter.toString());
            advance();
        }
        else {
            while (currentCharacter != null && POSSIBLE_IDENTIFIER_CHARACTERS.indexOf(currentCharacter) != -1) {
                identifier.append(currentCharacter);
                advance();
            }
        }

        TokenType tokenType;
        if (KEYWORDS.contains(identifier.toString())) tokenType = TokenType.KEYWORD;
        else tokenType = TokenType.IDENTIFIER;

        return new Token(tokenType, identifier.toString(), currentCharacterPosition.copy());
    }

    private void createPlusToken(ArrayList<Token> tokens) {
        if (currentCharacterPosition.copy().index() == text.length() - 1 || currentCharacterPosition.copy().column() == 0) tokens.add(new Token(TokenType.PLUS, currentCharacterPosition.copy()));
        else {
            char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
            if (nextCharacter == '=') {
                advance();
                tokens.add(new Token(TokenType.PLUS_EQUAL, currentCharacterPosition.copy()));
            } else {
                tokens.add(new Token(TokenType.PLUS, currentCharacterPosition.copy()));
            }
        }
    }

    private void createMinusToken(ArrayList<Token> tokens) {
        if (currentCharacterPosition.copy().index() == text.length() - 1 || currentCharacterPosition.copy().column() == 0) tokens.add(new Token(TokenType.MINUS, currentCharacterPosition.copy()));
        else {
            char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
            if (nextCharacter == '=') {
                advance();
                tokens.add(new Token(TokenType.MINUS_EQUAL, currentCharacterPosition.copy()));
            } else if (nextCharacter == '>') {
                advance();
                tokens.add(new Token(TokenType.ARROW, currentCharacterPosition.copy()));
            } else {
                tokens.add(new Token(TokenType.MINUS, currentCharacterPosition.copy()));
            }
        }
    }

    private Token createEqualsToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.EQUAL, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
        if (nextCharacter == '=') {
            advance();
            return new Token(TokenType.EQUAL_EQUAL, currentCharacterPosition.copy());
        }
        return new Token(TokenType.EQUAL, currentCharacterPosition.copy());
    }

    private Token createLessThanToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.LESS_THAN, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
        if (nextCharacter == '=') {
            advance();
            return new Token(TokenType.LESS_OR_EQUAL, currentCharacterPosition.copy());
        }
        return new Token(TokenType.LESS_THAN, currentCharacterPosition.copy());
    }

    private Token createGreaterThanToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.GREATER_THAN, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
        if (nextCharacter == '=') {
            advance();
            return new Token(TokenType.GREATER_OR_EQUAL, currentCharacterPosition.copy());
        }
        return new Token(TokenType.GREATER_THAN, currentCharacterPosition.copy());
    }

    private Token createMultiplyOrPowerToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.GREATER_THAN, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);
        if (nextCharacter == '*') {
            advance();
            return new Token(TokenType.POW, currentCharacterPosition.copy());
        }
        return new Token(TokenType.MUL, currentCharacterPosition.copy());
    }

    private Token createNotToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.NOT, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);

        if (nextCharacter == '=') {
            advance();
            return new Token(TokenType.NOT_EQUAL, currentCharacterPosition.copy());
        }
        return new Token(TokenType.NOT, currentCharacterPosition.copy());
    }

    private Token createAndToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) {
            setException(new InvalidSyntaxException(currentCharacterPosition.copy(), "Expected '&'"));
            return null;
        }

        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);

        if (nextCharacter == '&') {
            advance();
            return new Token(TokenType.AND, currentCharacterPosition.copy());
        }

        setException(new InvalidSyntaxException(currentCharacterPosition.copy(), "Expected '&'"));
        return null;
    }

    private Token createOrToken() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) {
            setException(new InvalidSyntaxException(currentCharacterPosition.copy(), "Expected '|'"));
            return null;
        }

        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);

        if (nextCharacter == '|') {
            advance();
            return new Token(TokenType.OR, currentCharacterPosition.copy());
        }

        setException(new InvalidSyntaxException(currentCharacterPosition.copy(), "Expected '|'"));
        return null;
    }

    private Token createDivisionOrComment() {
        if (currentCharacterPosition.copy().index() == text.length() - 1) return new Token(TokenType.DIV, currentCharacterPosition.copy());
        char nextCharacter = text.charAt(currentCharacterPosition.copy().index() + 1);

        if (nextCharacter == '/') {
            advance();
            skipLine();
            return new Token(TokenType.NEWLINE, currentCharacterPosition.copy());
        }
        return new Token(TokenType.DIV, currentCharacterPosition.copy());
    }

    private void skipLine() {
        while (currentCharacter != null && currentCharacter != '\n' && currentCharacter != ';') advance();
    }

    private void setException(Exception exception) {
        if (this.exception == null) this.exception = exception;
    }
}
