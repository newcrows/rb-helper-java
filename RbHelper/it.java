/*
Hi there!
This is gonna teach you the basics of JAVA, an easy programming language

1) Syntax is important:
    -for a program to work, syntax must be correct
    
Example:
    -after every instruction, you need ";"
    -comments you do with "// comment text"
    -upper/lowercase is important
    -curly braces mark area of specific code "{..}"
*/
    
String hello = "hello"; //a string variable (i am a comment)

/*
2) Variables / Constants:
    -if you have a value that changes if you do something, it is called a variable
    -if you have a value that never changes, it is called a constant
    -Variables / Constants have a type
        -type "int" means a number with no decimals, e.g. "156"
        -type "float" means a number with decimals, e.g. "12.5"
        -type "String" means a text, e.g. "Hello"
        -type "boolean" means a lever that is either "true" or "false"
    
    Example:
*/

int myInt = 13; //i am a number

float myFloat = 12.5F; //i am a float

String myString = "I am text"; //i am text

boolean myBoolean = true; //i am a boolean

/*
3) Control Flow:
    -you need to decide what you want to do based on variable
        -"if" statement is for deciding what to run
    -operators to do something
        -operators are "=",+","-","*","/",">>",">>>","<<","++","--"
    -loops
        -to run forever or until you done with work
        -"for","while","do..while" are all loop formats
        
    Examples:
*/

if (myInt > 100) {
    //if myInt is bigger than 100, code here is run    
} else {
    //if myInt is smaller or equal to 100, code here is run
}

myInt = 102; //myInt is now set to 102
myInt = myInt * 2; //myInt is now set to 204 (102 * 2)
    
myInt++; //myInt is now 205 (++ means add 1 and set)

for(int index = 0; index < 100; index++) {
    //code in here is run 100 times
    //index++ means add 1 to index every round of loop
    //index < 100 means run loop until index is 100 or bigger
    //int index = 0 means create variable to use in loop and set it to 0
}

/*
4) That is the basic of every program
    -there exist much more variable types
    -and much more complex stuff that you need, i will write them in other tutorial
*/

