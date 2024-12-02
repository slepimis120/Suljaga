# Šuljaga

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

## Introduction
Šuljaga is a custom interpreter for an imaginary programming language that supports variable assignments, scoped operations, and basic control flow. The language features an interactive prompt, basic arithmetic, block structures, and error handling.

The project is based on the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom, with the catch that the whole project was written in Kotlin, instead of Java.

## Features
- Variable Declarations and Assignments
- Arithmetic Operations (+, -, *, /)
- Unary and Grouping Operations
- Print Statements
- Block Statements
- Command-line Interface (CLI)
- Error Handling

## Requirements

- Java Development Kit (JDK) 8 or higher
- Gradle

## Building the Project

To build the project, run the following command:

```bash
gradle wrapper
gradlew build
```

## Running the Project
To run the project, use the following command:

```bash
java -jar build/libs/Suljaga-1.0.0-all.jar src/main/resources/example.sulj
```

## Commands

### Variable Declaration and Assignment
```
x = 99
```
Declaration and Assignment are done in a single step, and it's allowed to re-assign existing variables (you can write x = 5, and then x = 7).

### Arithmetic Operations
```
x = (99 + 1) * (10 - 5)
y = (100 * 2 / (4 + 1))
```
You can perform basic arithmetic operations on variables. The operations supported are addition, subtraction, multiplication, and division, along with brackets.

### Variable Assignment from Another Variable
```
x = 99
y = x
```

You can assign a variable's value from another existing variable. If x has been previously assigned a value, y will take that value. If x is undefined, the value of y will be null.

### Scopes
```
scope {
    x = 99
    y = x
}
```
Scopes allow you to group variable assignments and operations. Once you exit a scope, all variables declared or reassigned inside it will be discarded. In the example above, the values of x and y inside the inner scope won't persist after exiting the scope. You can also nest scopes inside each other.

### Print Statements
```
print x
```
The print command prints the value of the variable. If the variable hasn't been assigned any value in the current scope or its parent scopes, null will be printed instead.

### Example Program
```
x = 1
print x       # Output: 1
scope {
  x = 2
  print x     # Output: 2
  scope {
    x = 3
    y = x
    print x   # Output: 3
    print y   # Output: 3
  }
  print x     # Output: 2
  print y     # Output: null
}
print x       # Output: 1
```

## License
This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.