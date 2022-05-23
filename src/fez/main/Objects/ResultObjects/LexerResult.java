package fez.main.Objects.ResultObjects;

import java.util.ArrayList;
import fez.main.Exceptions.Exception;
import fez.main.Objects.Token;

public class LexerResult extends BaseResult {

    private final ArrayList<Token> tokens;

    public LexerResult(ArrayList<Token> tokens, Exception exception) {
        super(exception);
        this.tokens = tokens;
    }

    public LexerResult(Exception exception) {
        super(exception);
        this.tokens = null;
    }

    public ArrayList<Token> tokens() {
        return tokens;
    }

}
