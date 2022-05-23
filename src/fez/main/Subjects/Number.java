package fez.main.Subjects;

import fez.main.Objects.Token;
import fez.main.Objects.TokenType;

public abstract class Number extends Subject {
    
    public static Number createNumber(Token token) {
        if (token.type() == TokenType.INT) return new Int((int) token.value());
        else if (token.type() == TokenType.FLOAT) return new Float((float) token.value());
        return null;
    }

}
