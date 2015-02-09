# Diff
A simple command line tool that allows for evaluation and differentiation of mathematical functions. The tool can also interpret a text file.

## Installation
To just get the application, download and run the jar file located at `build/jar/Diff.jar`.

Alternatively, you can compile and run the code yourself. This project uses Apache Ant for building, which can be be downloaded [here](http://ant.apache.org/bindownload.cgi). Clone this repo and use `ant clean-build` to package the project into a jar.

The jar can then be run using `java -jar build/jar/Diff.jar`.

## Commands
<pre>
use &lt;variable(s)&gt;               Declare what variables are being used.
forget &lt;variable(s)&gt;            Remove variables from `use` list.

set &lt;variable> &lt;expression&gt;     Set a variable equal to a function.
clear &lt;variable(s)&gt;             Clear a variable`s value.
show &lt;variable(s)&gt;              Lists all variables and their values, if they
                                have one.

sub &lt;expression&gt; &lt;variable(s)&gt;  Substitutes specified variables and evaluates
                                the expression.
eval &lt;expression&gt;               Evaluates the expression without substitution,
                                which is the same as calling `sub` with no
                                variable arguments.
diff &lt;expression&gt; &lt;variable&gt;    Calculates the derivative of the expression
                                with respect to given variable.

help                            Lists descriptions of all commands.
exit                            Exit the program.
</pre>

## Guide
All arguments and commands are separated by spaces. This also means that you cannot put any spaces in a function.

To see all commands and a description of what they each do, simply type `help`. To exit the program, type `exit`.

If you are going to be using any variables, you need to tell the program about them before you use them. Use the `use` command followed by the names of the variables you want to use.

    > use x y z

You can stop the program from recognizing variables with the `forget` command.

    > forget x y z

To assign a value to a variable, use the `set` command.

    > set x 4

Any function can be assigned as the value of a variable, as long as it doesn`t cause as cycle.

    > set x y+2
    > set y x-4

The above is not permitted because the each variable contains the other in its definition, causing a cycle.

To get rid of a variable`s value, use the `clear` command. The `clear` takes any number of variables to clear.

    > clear x y

Alternatively, specify `all` to clear the value of every variable.

    > clear all

The program automatically remembers the result of your last function operation and stores it as the value of the `$` variable. The user cannot modify the `$` variable manually.

    > eval 1+2
    3
    > show $
    3

You can show all variables that are currently defined and their values, if applicable, with the `show` command.

    > use x y z
    > set x 15
    > show
    $
    y
    z
    x = 15

Simple evaluation of functions is done using the `eval` command.

    > eval 3+8
    11
    > use x
    > eval 5+x
    5+x

Using the `eval` command does not substitute in values for variables. Instead, use the `sub` command. In addition to a function, the `sub` command takes any number of variables to have substituted in. Passing `all` or nothing substitutes in all variables.

    > use x
    > set x 4
    > set y 3
    > sub x+y x
    4+y
    > sub x+y all
    7

Functions can be differentiated using the `diff` command. The `diff` command takes a function followed by the name of the variable to differentiate with respect to.

    > use x
    > diff x^2 x
    x*2

The value of a variable can be set as the result of a evaluation, substitution, or differentiation.

    > use x y
    > set y diff x^3 x
    > show y
    x^2*3
