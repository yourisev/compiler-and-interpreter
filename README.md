# 🧠 Mini Julia Parser – Lexical and Syntax Analyzer in Java

This project implements a **Lexical Analyzer (Scanner)**, a **Recursive Descent Parser**, and a **Parse Tree Generator** for a **simplified version of the Julia programming language**. The system analyzes `.jl` source files, identifies syntax structures, and generates a visual parse tree using the **Visitor Pattern**.

---

## 🚀 Why This Project is Cool

- ✅ Parses custom Julia-like syntax
- 🧠 Built from scratch using core Java (no external parsing libraries)
- 🌲 Generates structured **parse trees** for visual understanding
- ❌ Detects and reports both **lexical and syntax errors**
- 🛠️ Modular design with Expression/Statement trees and the Visitor pattern
- 🔁 Includes a test suite with sample `.jl` programs (correct and buggy)

---

## 🧩 What This Project Does

1. **Reads Julia-style `.jl` files**
2. **Performs lexical analysis** to produce a list of tokens (e.g., `IF`, `IDENTIFIER`, `+`)
3. **Performs syntax analysis** to build an abstract syntax tree (AST) using recursive grammar rules
4. **Prints parse tree** for each statement using the Visitor pattern
5. **Displays a token table** including lexemes and opcodes

---

## 📐 Design

![image](https://github.com/user-attachments/assets/d6f16fe1-4881-4e43-bbab-436606a53b9b)

---

## 📦 Project Structure

| File/Class | Purpose |
|------------|---------|
| `Main.java` | Main runner – loads source file, invokes lexer, parser, and parse tree printer |
| `LexicalAnalyzer.java` | Scans characters and produces a list of tokens |
| `SyntaxAnalyzer.java` | Parses tokens into structured statements and expressions using recursive descent |
| `Token.java`, `TokenType.java` | Token representation and types (e.g., `IF`, `INTEGER_LITERAL`) |
| `Statement.java`, `Expression.java` | AST node base classes with subclasses for `IfStatement`, `Literal`, etc. |
| `ParseTreePrinter.java` | Prints formatted parse trees using the Visitor pattern |
| `GenerateParseTree.java` | Codegen utility to automatically generate AST classes |
| `Matcher.java`, `WordsMatcher.java` | Utility/test classes (not core to the parser) |
| `*.jl` | Julia-like test files to validate parsing and tree generation (some contain syntax errors for testing)

---

## 🔍 Example Input and Output

### ✅ Valid Source (`Test3Fix.jl`)
```julia
function a()
    x = 1
    if x != 1
        print(0)
    else
        print(1)
    end
end
```

### 🖨️ Output
```bash
Lexeme                    Token                          OpCode                        
----------------------------------------------------------------
function                 FUNCTION                       20
a                        IDENTIFIER                     2
(                        LEFT_PARANTHESIS               1
)                        RIGHT_PARANTHESIS              0
...

Parse Tree
function a()
(if(x!=1(print 0))(else(print 1))end)
end
```

## 💡 Concepts Used

|Concept	| Description|
|---------|------------|
|Lexical Analysis	| Identifies lexemes, symbols, keywords, literals |
|Recursive Descent Parsing |	Implements grammar rules via method calls |
|AST (Abstract Syntax Tree) |	Expression and statement classes form a tree |
|Visitor Pattern |	Cleanly traverses and prints tree nodes |
|Error Handling	| Detects unexpected tokens and character mismatches|

## ⚙️ How to Run

🖥️ Requirements
- Java JDK 8 or later

🛠️ Compile
```bash
javac *.java
```

▶️ Run
```bash
java Main
```

---

## 🙏 Acknowledgments

This project was heavily inspired by **Robert Nystrom's** excellent book,  
[**Crafting Interpreters**](https://craftinginterpreters.com/), which provided the foundational concepts for building a scanner, parser, and abstract syntax trees using the Visitor pattern.

**_Edit Main.java to specify which .jl test file to run._**
