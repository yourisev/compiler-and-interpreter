import java.util.List;

/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

//Base abstract class for Statements
abstract class Statement {
  interface Visitor<R> {
    R visitIFStatement(IFStatement statement);
    R visitWhileStatement(WhileStatement statement);
    R visitAssignmentStatement(AssignmentStatement statement);
    R visitPrintStatement(PrintStatement statement);
    R visitRepeatStatement(RepeatStatement statement);
  }
  static class IFStatement extends Statement {
    IFStatement(Expression booleanExpression, List<Statement> thenBranch, List<Statement> elseBranch) {
      this.booleanExpression = booleanExpression;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitIFStatement(this);
    }

    final Expression booleanExpression;
    final List<Statement> thenBranch;
    final List<Statement> elseBranch;
  }

  //Class for a while statement
  static class WhileStatement extends Statement {
    WhileStatement(Expression booleanExpression, List<Statement> body) {
      this.booleanExpression = booleanExpression;
      this.body = body;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitWhileStatement(this);
    }

    final Expression booleanExpression;
    final List<Statement> body;
  }

  //Class for an assignment statement
  static class AssignmentStatement extends Statement {
    AssignmentStatement(Token variableName, Token assignmentOperator, Expression arithmeticExpression) {
      this.variableName = variableName;
      this.assignmentOperator = assignmentOperator;
      this.arithmeticExpression = arithmeticExpression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitAssignmentStatement(this);
    }

    final Token variableName;
    final Token assignmentOperator;
    final Expression arithmeticExpression;
  }

  //Class for a print statement
  static class PrintStatement extends Statement {
    PrintStatement(Expression arithmeticExpression) {
      this.arithmeticExpression = arithmeticExpression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStatement(this);
    }

    final Expression arithmeticExpression;
  }

  //Class for a repeat statement
  static class RepeatStatement extends Statement {
    RepeatStatement(List<Statement> statements, Expression booleanExpression) {
      this.statements = statements;
      this.booleanExpression = booleanExpression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitRepeatStatement(this);
    }

    final List<Statement> statements;
    final Expression booleanExpression;
  }

  abstract <R> R accept(Visitor<R> visitor);
}
