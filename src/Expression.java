
/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

import java.util.List;

//Base abstract class for Expression
abstract class Expression {
  interface Visitor<R> {
    R visitBooleanExpression(BooleanExpression expression);
    R visitArithmeticOperator(ArithmeticOperator expression);
    R visitLiteral(Literal expression);
    R visitIdentifier(Identifier expression);
  }

  //Class for an expression of type boolean
  static class BooleanExpression extends Expression {
    BooleanExpression(Expression leftArithmeticExpression, Token relativeOperator, Expression rightArithmeticExpression) {
      this.leftArithmeticExpression = leftArithmeticExpression;
      this.relativeOperator = relativeOperator;
      this.rightArithmeticExpression = rightArithmeticExpression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBooleanExpression(this);
    }

    final Expression leftArithmeticExpression;
    final Token relativeOperator;
    final Expression rightArithmeticExpression;
  }

  //Class for an expression of type arithmetic operator
  static class ArithmeticOperator extends Expression {
    ArithmeticOperator(Token operator) {
      this.operator = operator;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitArithmeticOperator(this);
    }

    final Token operator;
  }

  //Class for an expression of type literal
  static class Literal extends Expression {
    Literal(Object value) {
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteral(this);
    }

    final Object value;
  }

  //Class for an expression of type identifier
  static class Identifier extends Expression {
    Identifier(Token variableName) {
      this.variableName = variableName;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitIdentifier(this);
    }

    final Token variableName;
  }

  abstract <R> R accept(Visitor<R> visitor);
}
