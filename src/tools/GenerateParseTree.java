package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Name:       NGUIAGAING SEUGA MARCEL YOURI
 * Instructor:   Sharon Perry
 * Project:     Deliverable 2 Parser
 **/

// Simple script for creating the different classes for expressions and statements
public class GenerateParseTree {
    public static void main(String[] args) throws IOException {
        String outputDirectory = "src\\";

        defineParseTree(outputDirectory, "Expression", Arrays.asList(
                "BooleanExpression   : Expression leftArithmeticExpression, Token relativeOperator, Expression rightArithmeticExpression",
                "ArithmeticOperator  : Token operator",
                "Literal             : Object value",
                "Identifier          : Token variableName"
        ));

        defineParseTree(outputDirectory, "Statement", Arrays.asList(
                "IFStatement         : Expression booleanExpression, List<Statement> thenBranch, List<Statement> elseBranch",
                "WhileStatement      : Expression booleanExpression, List<Statement> body",
                "AssignmentStatement : Token variableName, Token assignmentOperator, Expression arithmeticExpression",
                "PrintStatement      : Expression arithmeticExpression",
                "RepeatStatement     : List<Statement> statements, Expression booleanExpression"
        ));
    }

    // Method used to automatically create necessary classes for expressions and statements
    private static void defineParseTree(
            String outputDirectory, String baseClassName, List<String> expressionTypes)
            throws IOException {
        String classFilePath = outputDirectory + "/" + baseClassName + ".java";
        PrintWriter writer = new PrintWriter(classFilePath, "UTF-8");

        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseClassName + " {");

        defineVisitorClass(writer, baseClassName, expressionTypes);

        // The Parse Tree classes.
        for (String expressionType : expressionTypes) {
            String className = expressionType.split(":")[0].trim();
            String classFields = expressionType.split(":")[1].trim();
            defineType(writer, baseClassName, className, classFields);
        }

        // The base accept() method.
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    // Define the inner classes for the different types of expressions or statements
    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println("  static class " + className + " extends " +
                baseName + " {");

        // Generate the class Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store constructor parameters in corresponding class fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // The Visitor pattern.
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className /*+ baseName*/ + "(this);");
        writer.println("    }");

        // Generate class Fields.
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }

    // Define the visitor interface
    private static void defineVisitorClass(
            PrintWriter writer, String baseClassName, List<String> expressionTypes) {
        writer.println("  interface Visitor<R> {");

        //Define the methods unique to each expression or statement type in the visitor interface
        for (String expressionType : expressionTypes) {
            String expressionTypeName = expressionType.split(":")[0].trim();
            writer.println("    R visit" + expressionTypeName /*+ baseClassName*/ + "(" +
                    expressionTypeName + " " + baseClassName.toLowerCase() + ");");
        }

        writer.println("  }");
    }

}
