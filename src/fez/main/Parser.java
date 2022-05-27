package fez.main;

import java.util.*;
import java.util.function.Supplier;
import fez.main.Exceptions.Exception;
import fez.main.Exceptions.InvalidSyntaxException;
import fez.main.Nodes.BinaryOperationNode;
import fez.main.Nodes.ForNode;
import fez.main.Nodes.FunctionCallNode;
import fez.main.Nodes.FunctionDefinitionNode;
import fez.main.Nodes.IfNode;
import fez.main.Nodes.ListNode;
import fez.main.Nodes.Node;
import fez.main.Nodes.NumberNode;
import fez.main.Nodes.StringNode;
import fez.main.Nodes.UnaryOperationNode;
import fez.main.Nodes.VariableAssignNode;
import fez.main.Nodes.VariableReassignNode;
import fez.main.Nodes.VariableReferenceNode;
import fez.main.Nodes.WhileNode;
import fez.main.Objects.ModifierType;
import fez.main.Objects.Position;
import fez.main.Objects.Token;
import fez.main.Objects.TokenType;
import fez.main.Objects.ResultObjects.ParserResult;
import fez.main.Subjects.Int;
import fez.main.Subjects.Number;

public class Parser {
    private final ArrayList<Token> tokens;
    private int tokenIndex;
    private Token currentToken;
    private Exception exception;

    private final TokenType[] numberAcceptedTokenTypes;
    private final TokenType[] factorAcceptedTokenTypes;
    private final TokenType[] termAcceptedTokenTypes;
    private final TokenType[] mathematicalExpressionAcceptedTokenTypes;
    private final TokenType[] comparisonExpressionAcceptedTokenTypes;
    private final TokenType[] expressionAcceptedTokenTypes;
    private final TokenType[] mathematicalAndExpressionAcceptedTokenTypes;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        tokenIndex = -1;

        numberAcceptedTokenTypes = new TokenType[]{ TokenType.INT, TokenType.FLOAT };
        factorAcceptedTokenTypes = new TokenType[]{ TokenType.PLUS, TokenType.MINUS };
        termAcceptedTokenTypes = new TokenType[]{ TokenType.MUL, TokenType.DIV, TokenType.MODULO };
        mathematicalExpressionAcceptedTokenTypes = new TokenType[]{ TokenType.PLUS, TokenType.MINUS, TokenType.MUL, TokenType.DIV, TokenType.MODULO, TokenType.POW };
        comparisonExpressionAcceptedTokenTypes = new TokenType[]{ TokenType.EQUAL_EQUAL, TokenType.NOT_EQUAL, TokenType.GREATER_THAN, TokenType.LESS_THAN, TokenType.GREATER_OR_EQUAL, TokenType.LESS_OR_EQUAL, TokenType.NOT };
        expressionAcceptedTokenTypes = new TokenType[]{ TokenType.AND, TokenType.OR };
        mathematicalAndExpressionAcceptedTokenTypes = new TokenType[]{ TokenType.PLUS, TokenType.MINUS, TokenType.MUL, TokenType.DIV, TokenType.MODULO, TokenType.POW, TokenType.AND, TokenType.OR };

        if (tokens != null) advance();
    }

    private void advance() {
        tokenIndex++;
        if (tokenIndex < tokens.size()) currentToken = tokens.get(tokenIndex);
        else {
            tokenIndex = tokens.size();
            currentToken = null;
        }
    }

    private void retreat() {
        tokenIndex--;
        currentToken = tokens.get(tokenIndex);
    }

    public ParserResult parse() {
        if (tokens == null) return new ParserResult(exception);
        return new ParserResult(statements(), exception);
    }

    private ListNode statements() {
        ArrayList<Node> statements = new ArrayList<>();
        Position statementsPosition = currentToken.copyPosition();

        // Skip new lines at the start
        while (currentToken != null && currentToken.type() == TokenType.NEWLINE) {
            statementsPosition = currentToken.copyPosition();
            advance();
        }
        
        // Add first statement to statements arraylist
        Node firstExpression = expression();
        if (firstExpression != null) statements.add(firstExpression);
        
        while (true) {
            int newLinesCount = 0;

            // Skip new lines
            while (currentToken != null && currentToken.type() == TokenType.NEWLINE) {
                statementsPosition = currentToken.copyPosition();
                advance();
                newLinesCount++;
            }
            
            if (newLinesCount == 0) break;
            if (currentToken != null && currentToken.type() != TokenType.RCURLY) statements.add(expression());
        }

        return new ListNode(statements, statementsPosition);
    }

    private Node expression() {
        if (currentToken == null) return null;
        else if (currentToken.matches(TokenType.KEYWORD, "var")) return assignVariable(new ArrayList<>());
        else if (currentToken.matches(TokenType.KEYWORD, "const")) return assignVariable(new ArrayList<>(List.of(ModifierType.IMMUTABLE)));
        else if (currentToken.type().equals(TokenType.IDENTIFIER)) {
            Token variableName = currentToken;
            advance(); // Skip identifier
    
            if (currentToken != null) {
                // Continue to reassign a variable
                if (currentToken.type() == TokenType.EQUAL) return reassignVariable(variableName, TokenType.EQUAL);
                else if (currentToken.type() == TokenType.PLUS_EQUAL) return reassignVariable(variableName, TokenType.PLUS_EQUAL);
                else if (currentToken.type() == TokenType.MINUS_EQUAL) return reassignVariable(variableName, TokenType.MINUS_EQUAL); 
            }
            retreat(); // Identifier is used to access a variable
        }
        else if (currentToken.matches(TokenType.KEYWORD, "while")) return whileStatement();
        else if (currentToken.matches(TokenType.KEYWORD, "for")) return forStatement();

        Node expression = binaryOperation(this::comparisonExpression, expressionAcceptedTokenTypes);
        if (expression == null) return null; // term has error

        if (currentToken != null) {
            // If BinaryOperationNode has more nodes
            if (expression instanceof BinaryOperationNode && isAcceptedTokenType(currentToken, mathematicalAndExpressionAcceptedTokenTypes))
                expression = binaryOperation(expression);
            else expression = retrieveFromList(expression); // If trying to retrieve an item from ListNode
        }

        return expression;
    }

    private Node retrieveFromList(Node expression) {
        if (currentToken != null && currentToken.type() == TokenType.LSQUARE) {
            ListNode itemsToRetrieve = listStatement();
            if (itemsToRetrieve != null) expression = retrieveFromList(new BinaryOperationNode(expression, TokenType.RETRIEVE, itemsToRetrieve));
        }

        return expression;
    }

    private Node comparisonExpression() {
        if (currentToken.type() == TokenType.NOT) {
            advance();
            Node comparisonExpression = comparisonExpression();
            return new UnaryOperationNode(TokenType.NOT, comparisonExpression);
        }

        return binaryOperation(this::mathematicalExpression, comparisonExpressionAcceptedTokenTypes);
    }

    private Node mathematicalExpression() {
        return binaryOperation(this::term, mathematicalExpressionAcceptedTokenTypes);
    }

    private Node term() {
        return binaryOperation(this::factor, termAcceptedTokenTypes);
    }

    private Node factor() {
        Token token = currentToken;

        if (token != null) {
            if (isAcceptedTokenType(token, factorAcceptedTokenTypes)) { // If unary operation
                advance();
                return new UnaryOperationNode(token.type(), Objects.requireNonNull(factor()));
            }
            else return power();
        }
        
        retreat();
        setException(new InvalidSyntaxException(currentToken.copyPosition(), "Expected value"));
        return null;
    }

    private Node power() {
        Token token = currentToken;

        if (token != null) {
            advance();
            if (currentToken != null && currentToken.type() == TokenType.POW) {
                retreat();
                Node firstNumber = call();
                advance();
                return new BinaryOperationNode(firstNumber, TokenType.POW, Objects.requireNonNull(call()));
            }
            else {
                retreat();
                return call();
            }
        }
        
        retreat();
        setException(new InvalidSyntaxException(currentToken.copyPosition(), "Expected value"));
        return null;
    }

    private Node call() {
        Position callPosition = currentToken.copyPosition();

        Node atom = atom();
        if (currentToken != null && currentToken.type() == TokenType.LPAREN) {
            ArrayList<Node> arguments = new ArrayList<>();
            advance();

            if (currentToken == null) {
                setException(new InvalidSyntaxException(callPosition, "Expected argument or ')'"));
                return null;
            }
            else if (currentToken.type() == TokenType.RPAREN) advance();
            else {
                Node firstArgument = expression();
                if (firstArgument == null) {
                    setException(new InvalidSyntaxException(callPosition, "Expected argument"));
                    return null;
                }

                arguments.add(firstArgument);

                while (currentToken != null && currentToken.type() == TokenType.COMMA) {
                    advance();
                    arguments.add(expression());
                }

                if (currentToken == null || currentToken.type() != TokenType.RPAREN) {
                    setException(new InvalidSyntaxException(callPosition, "Expected ')'"));
                    return null;
                }

                advance();
            }

            return new FunctionCallNode(atom, arguments, callPosition);
        }

        return atom;
    }

    private Node atom() {
        Token token = currentToken;
        
        if (token != null) {
            if (token.type() == TokenType.IDENTIFIER) {
                advance();
                return new VariableReferenceNode(token);
            }
            else if (isAcceptedTokenType(token, numberAcceptedTokenTypes)) {
                advance();
                return new NumberNode(Number.createNumber(token), token.copyPosition());
            }
            else if (token.type() == TokenType.STRING) {
                advance();
                return new StringNode((String) token.value(), token.copyPosition());
            }
            else if (token.matches(TokenType.KEYWORD, "if")) return ifStatement();
            else if (token.matches(TokenType.KEYWORD, "fun")) return functionDefinition();
            else if (token.type() == TokenType.LSQUARE && currentToken != null) return listStatement();
            else if (token.type() == TokenType.LPAREN && currentToken != null) {
                advance();
                Node expression = expression();
                if (currentToken == null) { // No RPAREN found
                    setException(new InvalidSyntaxException(token.copyPosition(), "Expected ')'"));
                    return null;
                } else if (currentToken.type() == TokenType.RPAREN) {
                    advance();
                    return expression;
                }
            }
        }

        assert currentToken != null;
        setException(new InvalidSyntaxException(currentToken.copyPosition(), "Expected value"));
        return null;
    }

    private Node binaryOperation(Supplier<Node> supplier, TokenType[] acceptedTokenTypes) {
        Node left = supplier.get();
        if (left == null) return null; // expression or term have an error

        if (isAcceptedTokenType(currentToken, acceptedTokenTypes)) {
            TokenType operation = currentToken.type();
            advance();
            Node right = supplier.get();
            if (right == null) {
                setException(new InvalidSyntaxException(left.copyPosition(), "Expected value"));
                return null;
            }

            return new BinaryOperationNode(left, operation, right);
        }

        return left;
    }

    private Node binaryOperation(Node left) {
        BinaryOperationNode binExpression = new BinaryOperationNode(left);
        TokenType operator = currentToken.type() == TokenType.MINUS ? TokenType.PLUS : currentToken.type();
        if (isAcceptedTokenType(currentToken, expressionAcceptedTokenTypes)) advance();

        left = expression();
        if (left instanceof UnaryOperationNode) binExpression.setRight(left);
        else binExpression.setRight(left);

        binExpression.setOperator(operator);

        return binExpression;
    }

    private VariableAssignNode assignVariable(ArrayList<ModifierType> modifiers) {
        Position variableAssignmentPosition = currentToken.copyPosition();
        advance(); // Skip var or val

        if (currentToken == null || currentToken.type() != TokenType.IDENTIFIER) {
            setException(new InvalidSyntaxException(variableAssignmentPosition, "Expected identifier"));
            return null;
        }

        variableAssignmentPosition = currentToken.copyPosition();

        Token variableName = currentToken;
        advance();

        if (currentToken == null || currentToken.type() != TokenType.EQUAL) {
            setException(new InvalidSyntaxException(variableAssignmentPosition, "Expected '='"));
            return null;
        }
        
        variableAssignmentPosition = currentToken.copyPosition();
        advance();

        if (currentToken == null) {
            setException(new InvalidSyntaxException(variableAssignmentPosition, "Expected expression"));
            return null;
        }

        Node expression = expression();
        if (expression == null) return null;
        variableAssignmentPosition = expression.copyPosition();
        if (expression instanceof ForNode) {
            setException(new InvalidSyntaxException(variableAssignmentPosition, "For loop is forbidden as a variable value"));
            return null;
        } else if (expression instanceof WhileNode) {
            setException(new InvalidSyntaxException(variableAssignmentPosition, "While loop is forbidden as a variable value"));
            return null;
        }

        return new VariableAssignNode(variableName, expression, modifiers);
    }

    private VariableReassignNode reassignVariable(Token identifierToken, TokenType type) {
        Position reassignVariablePosition = currentToken.copyPosition();
        advance(); // Skip '=', '+=', '-='

        if (currentToken != null) {
            reassignVariablePosition = currentToken.copyPosition();
            Node expression = expression();

            if (expression instanceof ForNode) {
                setException(new InvalidSyntaxException(reassignVariablePosition, "For loop is forbidden as a variable value"));
                return null;
            } else if (expression instanceof WhileNode) {
                setException(new InvalidSyntaxException(reassignVariablePosition, "While loop is forbidden as a variable value"));
                return null;
            }

            if (expression == null) return null;
            BinaryOperationNode binaryOperation = new BinaryOperationNode(new VariableReferenceNode(identifierToken), TokenType.PLUS, expression);
            switch (type) {
                case EQUAL: return new VariableReassignNode(identifierToken, expression);
                case PLUS_EQUAL: return new VariableReassignNode(identifierToken, binaryOperation);
                case MINUS_EQUAL:
                    binaryOperation.setOperator(TokenType.MINUS);
                    return new VariableReassignNode(identifierToken, binaryOperation);
                default: return null;
            }
        } else {
            setException(new InvalidSyntaxException(reassignVariablePosition, "Expected expression"));
            return null;
        }
    }

    private FunctionDefinitionNode functionDefinition() {
        Position functionPosition = currentToken.copyPosition();
        advance();

        if (currentToken == null || currentToken.type() != TokenType.IDENTIFIER) {
            setException(new InvalidSyntaxException(functionPosition, "Expected identifier"));
            return null;
        }

        functionPosition = currentToken.copyPosition();
        String name = (String) currentToken.value();
        advance();

        if (currentToken == null || currentToken.type() != TokenType.LPAREN) {
            setException(new InvalidSyntaxException(functionPosition, "Expected '('"));
            return null;
        }

        functionPosition = currentToken.copyPosition();
        advance();

        if (currentToken == null) {
            setException(new InvalidSyntaxException(functionPosition, "Expected identifier or ')'"));
            return null;
        }

        functionPosition = currentToken.copyPosition();
        ArrayList<String> parameters = new ArrayList<>();
        if (currentToken.type() == TokenType.IDENTIFIER) {
            parameters.add((String) currentToken.value());
            advance();
        }

        while (currentToken.type() == TokenType.COMMA) {
            advance();

            if (currentToken == null || currentToken.type() != TokenType.IDENTIFIER) {
                setException(new InvalidSyntaxException(functionPosition, "Expected identifier"));
                return null;
            }

            functionPosition = currentToken.copyPosition();
            parameters.add((String) currentToken.value());
            advance();
        }

        if (currentToken == null || currentToken.type() != TokenType.RPAREN) {
            setException(new InvalidSyntaxException(functionPosition, "Expected ',' or ')'"));
            return null;
        }

        functionPosition = currentToken.copyPosition();
        advance();

        if (currentToken != null && currentToken.type() == TokenType.ARROW) {
            functionPosition = currentToken.copyPosition();
            advance();
        } else {
            if (currentToken == null || currentToken.type() != TokenType.LCURLY) {
                setException(new InvalidSyntaxException(functionPosition, "Expected '->' or '{'"));
                return null;
            } else functionPosition = currentToken.copyPosition();
        }

        Node statements;
        if (currentToken != null) {
            if (currentToken.type() == TokenType.LCURLY) {
                statements = getStatements(functionPosition);
                advance();
            }
            else statements = expression();
        } else {
            setException(new InvalidSyntaxException(functionPosition, "Expected '{' or expression"));
            return null;
        }

        if (statements == null) return null; // Exception is already checked in getStatements()

        return new FunctionDefinitionNode(name, parameters, statements, functionPosition);
    }

    private ListNode listStatement() {
        ArrayList<Node> nodes = new ArrayList<>();
        Position listPosition = currentToken.copyPosition();

        if (currentToken == null || currentToken.type() != TokenType.LSQUARE) {
            setException(new InvalidSyntaxException(listPosition, "Expected '['"));
            return null;
        }

        advance();

        if (currentToken == null) {
            setException(new InvalidSyntaxException(listPosition, "Expected ']'"));
            return null;
        }

        if (currentToken.type() == TokenType.RSQUARE) advance();
        else {
            Node firstItem = expression();
            if (firstItem == null) {
                setException(new InvalidSyntaxException(listPosition, "Expected argument"));
                return null;
            }

            listPosition = currentToken.copyPosition();
            nodes.add(firstItem);

            while (currentToken != null && currentToken.type() == TokenType.COMMA) {
                advance();
                listPosition = currentToken.copyPosition();
                if (currentToken.type() != TokenType.RSQUARE) {
                    nodes.add(expression());
                } else {
                    setException(new InvalidSyntaxException(listPosition, "Expected value"));
                    return null;
                }
            }

            if (currentToken == null || currentToken.type() != TokenType.RSQUARE) {
                setException(new InvalidSyntaxException(listPosition, "Expected ']'"));
                return null;
            }

            advance();
        }

        return new ListNode(nodes, listPosition);
    }

    private WhileNode whileStatement() {
        Position whilePosition = currentToken.copyPosition();
        advance();
        
        if (currentToken == null || currentToken.type() != TokenType.LPAREN) {
            setException(new InvalidSyntaxException(whilePosition, "Expected '('"));
            return null;
        }

        whilePosition = currentToken.copyPosition();
        advance();
        Node condition = expression();
        
        if (currentToken == null || condition == null) {
            setException(new InvalidSyntaxException(whilePosition, "Expected condition"));
            return null;
        }

        whilePosition = currentToken.copyPosition();

        if (currentToken == null || currentToken.type() != TokenType.RPAREN) {
            setException(new InvalidSyntaxException(whilePosition, "Expected ')'"));
            return null;
        }

        whilePosition = currentToken.copyPosition();
        advance();

        Node statements;
        if (currentToken != null) {
            if (currentToken.type() == TokenType.LCURLY) {
                statements = getStatements(whilePosition);
                advance();
            }
            else statements = expression();
        } else {
            setException(new InvalidSyntaxException(whilePosition, "Expected '{' or expression"));
            return null;
        }

        if (statements == null) return null; // Exception is already checked in getStatements()
        return new WhileNode(condition, statements);
    }

    private ForNode forStatement() {
        Position forPosition = currentToken.copyPosition();
        advance();
        if (currentToken == null || currentToken.type() != TokenType.LPAREN) {
            setException(new InvalidSyntaxException(forPosition, "Expected '('"));
            return null;
        }

        forPosition = currentToken.copyPosition();
        advance();

        if (currentToken == null || currentToken.type() != TokenType.IDENTIFIER) {
            setException(new InvalidSyntaxException(forPosition, "Expected identifier"));
            return null;
        }

        forPosition = currentToken.copyPosition();
        String variableName = (String) currentToken.value();
        advance();

        if (currentToken == null || !currentToken.matches(TokenType.KEYWORD, "in")) {
            setException(new InvalidSyntaxException(forPosition, "Expected \"in\""));
            return null;
        }

        forPosition = currentToken.copyPosition();
        advance();

        if (currentToken == null) {
            setException(new InvalidSyntaxException(forPosition, "Expected start value"));
            return null;
        }

        Node startValue = expression();
        if (startValue == null) {
            setException(new InvalidSyntaxException(forPosition, "Expected start value"));
            return null;
        }

        if (currentToken == null) {
            setException(new InvalidSyntaxException(forPosition, "Expected ':' or ')'"));
            return null;
        }

        Node endValue;
        if (currentToken.type() == TokenType.COLON) {
            forPosition = currentToken.copyPosition();
            advance();
            if (currentToken == null) {
                setException(new InvalidSyntaxException(forPosition, "Expected end value"));
                return null;
            }

            forPosition = currentToken.copyPosition();
            endValue = expression();

            if (currentToken == null) {
                setException(new InvalidSyntaxException(forPosition, "Expected ')'"));
                return null;
            } else if (endValue == null) {
                setException(new InvalidSyntaxException(forPosition, "Expected end value"));
                return null;
            }
        } else {
            endValue = startValue;
            startValue = new NumberNode(new Int(0), startValue.copyPosition());
        }

        forPosition = currentToken.copyPosition();
        advance();

        Node statements;
        if (currentToken != null) {
            if (currentToken.type() == TokenType.LCURLY) {
                statements = getStatements(forPosition);
                advance();
            }
            else statements = expression();
        } else {
            setException(new InvalidSyntaxException(forPosition, "Expected '{' or expression"));
            return null;
        }

        if (statements == null) return null; // Exception is already set in getStatements()
        return new ForNode(variableName, startValue, endValue, statements);
    }

    private IfNode ifStatement() {
        Position ifPosition = currentToken.copyPosition();
        Node elseCase = null;
        advance();

        if (currentToken == null || currentToken.type() != TokenType.LPAREN) {
            setException(new InvalidSyntaxException(ifPosition, "Expected '('"));
            return null;
        }

        advance();
        
        if (currentToken == null) {
            setException(new InvalidSyntaxException(ifPosition, "Expected condition"));
            return null;
        }

        ifPosition = currentToken.copyPosition();
        Node condition = expression();
        if (condition == null) return null; // No need to set exception since it's being set atom

        if (currentToken == null || currentToken.type() != TokenType.RPAREN) {
            setException(new InvalidSyntaxException(ifPosition, "Expected ')'"));
            return null;
        }

        ifPosition = currentToken.copyPosition();
        advance();

        Node statements;
        if (currentToken != null) {
            if (currentToken.type() == TokenType.LCURLY) {
                statements = getStatements(ifPosition);
                advance();
            }
            else statements = expression();
        } else {
            setException(new InvalidSyntaxException(ifPosition, "Expected '{' or expression"));
            return null;
        }

        // Give possibility for else keyword to be with one line space
        if (currentToken != null && currentToken.type() == TokenType.NEWLINE) advance();
        if (currentToken != null && currentToken.matches(TokenType.KEYWORD, "else")) {
            ifPosition = currentToken.copyPosition();
            advance(); // Skip else keyword

            if (currentToken != null) {
                if (currentToken.type() == TokenType.LCURLY) {
                    elseCase = getStatements(ifPosition);
                    advance();
                }
                else elseCase = expression();
            } else {
                setException(new InvalidSyntaxException(ifPosition, "Expected '{' or expression"));
                return null;
            }
        } else retreat();

        return new IfNode(condition, statements, elseCase);
    }

    private Node getStatements(Position position) {
        Node statements;
        if (currentToken != null && currentToken.type() == TokenType.LCURLY) {
            advance(); // Skip '{'

            if (currentToken == null) {
                setException(new InvalidSyntaxException(position, "Expected statement"));
                return null;
            }

            statements = statements();
            position = currentToken.copyPosition();
            if (currentToken.type() != TokenType.RCURLY) {
                setException(new InvalidSyntaxException(position, "Expected '}'"));
                return null;
            }
        } else {
            statements = expression();
            if (statements == null) {
                setException(new InvalidSyntaxException(position, "Expected expression"));
                return null;
            }
        }

        return statements;
    }

    private Boolean isAcceptedTokenType(Token token, TokenType[] acceptedTokens) {
        if (token == null) return false;
        return Arrays.asList(acceptedTokens).contains(token.type());
    }

    private void setException(Exception exception) {
        if (this.exception == null) this.exception = exception;
    }

}
