# Šuljaga

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

## Introduction
Šuljaga is a custom interpreter for an imaginary programming language that allows variable assignments and scoped operations. The language itself supports:

- Command-line interface for running scripts and interactive prompt.
- Basic error handling and runtime error reporting.
- Custom interpreter with support for:
    - Variable declarations and assignments.
    - Basic arithmetic operations.
    - Grouping and unary operations.
    - Print statements.
    - Block statements.

## Requirements

- Java Development Kit (JDK) 8 or higher
- Gradle

## Building the Project

To build the project, run the following command:

```bash
./gradlew build
```

## Running the Project
To run the project, use the following command:

```bash
java -jar build/libs/Suljaga-1.0.0-all.jar path/to/your/script.sulj
```

## Notes
- The project is based on the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom. The book is available for free online and provides a comprehensive guide on building interpreters and compilers.
- The script file must have a .sulj extension to be executed.

## License
This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.