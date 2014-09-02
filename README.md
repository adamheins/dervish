# Expression Tree

### Design
The expression tree is a data structure for evaluating mathematical expressions. There are a few aspects of the technique I find especially attractive:

1. The expression gets evaluated directly from infix notation, so no conversion to postfix is required.
2. There is no fundamental difference in the tree between binary operators (two children), unary operators (one child), and numbers (zero children). No checks need to be made against the maximum number of children; for well-formed input, the correct tree forms naturally.
3. The tree is highly scalable for adding additional operators due to it's polymorphic nature. Additional types of node could be added even without access to the original source code.

### Motivation
I designed the expression tree structure as a nice complement to the stack-based postfix evaluation technique used in my [calculator](https://github.com/adamheins/calculator). I wanted to create a very object-oriented and extensible system that made very heavy use of polymorphism.
