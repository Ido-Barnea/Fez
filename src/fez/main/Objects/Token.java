package fez.main.Objects;

public class Token {
    private final TokenType type;
    private Object value;
    private final Position position;

    public Token(TokenType type, Object value, Position position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public Token(TokenType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public TokenType type() {
        return type;
    }

    public Object value() {
        return value;
    }
    
    public Position copyPosition() {
        return position.copy();
    }

    public boolean matches(TokenType tokenType, Object value) {
        return this.type.equals(tokenType) && this.value.equals(value);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", value, type);
    }
}
