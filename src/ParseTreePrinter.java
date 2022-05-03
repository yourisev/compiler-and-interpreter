
/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

// Useful class for printing the parse tree of each statement or expression
public class ParseTreePrinter implements Expression.Visitor<String>, Statement.Visitor<String> {


    String print(Expression expression) {
        return expression.accept(this);
    }

    String print(Statement statement) {
        return statement.accept(this);
    }

    // Method for printing expressions correctly
    private String parenthesize(String name, Expression... expressions) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        if (expressions != null) {
            for (Expression expression : expressions) {
                builder.append(" ");
                builder.append(expression.accept(this));
            }
        }

        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitBooleanExpression(Expression.BooleanExpression booleanExpression) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(booleanExpression.leftArithmeticExpression.accept(this)).append(
                booleanExpression.relativeOperator.getLexeme()).append(
                booleanExpression.rightArithmeticExpression.accept(this));
        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitArithmeticOperator(Expression.ArithmeticOperator arithmeticOperatorExpression) {
        return parenthesize(arithmeticOperatorExpression.operator.getLexeme(), null);
    }

    @Override
    public String visitLiteral(Expression.Literal literalExpression) {
        if (literalExpression.value == null) return "nil";
        return literalExpression.value.toString();
    }

    @Override
    public String visitIdentifier(Expression.Identifier identifierExpression) {
        return parenthesize(identifierExpression.variableName.getLexeme(), null);
    }

    @Override
    public String visitIFStatement(Statement.IFStatement ifStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("if");
        builder.append(ifStatement.booleanExpression.accept(this));
        if (ifStatement.thenBranch != null) {
            for (Statement statement : ifStatement.thenBranch
            ) {
                builder.append(statement.accept(this));
            }
        }
        if (ifStatement.elseBranch != null) {
            builder.append("(else");
            for (Statement statement : ifStatement.elseBranch
            ) {
                builder.append(statement.accept(this));
            }
            builder.append(")");
        }

        builder.append("end");

        return builder.toString();
    }

    @Override
    public String visitWhileStatement(Statement.WhileStatement whileStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("while");
        builder.append(whileStatement.booleanExpression.accept(this));
        if (whileStatement.body != null) {
            for (Statement statement : whileStatement.body
            ) {
                builder.append(statement.accept(this));
            }
        }

        builder.append("end");

        return builder.toString();
    }

    @Override
    public String visitAssignmentStatement(Statement.AssignmentStatement assignmentStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(assignmentStatement.variableName.getLexeme()).append(
                assignmentStatement.assignmentOperator.getLexeme());
        builder.append(assignmentStatement.arithmeticExpression.accept(this));
        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitPrintStatement(Statement.PrintStatement printStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("(print ").append(printStatement.arithmeticExpression.accept(this));
        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitRepeatStatement(Statement.RepeatStatement repeatStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("(repeat").append(" until");
        builder.append(repeatStatement.booleanExpression.accept(this));
        builder.append(")");

        return builder.toString();
    }
}
