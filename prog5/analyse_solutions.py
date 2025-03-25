#!/usr/bin/python

import os
import subprocess
import sys
import re
import argparse
import shutil

# Set timeout command for OS
if sys.platform == "darwin":
    # MacOS
    timeout_cmd = "gtimeout"
elif sys.platform.startswith("linux"):
    # Linux
    timeout_cmd = "timeout"
else:
    print("ERROR: Timeout command not defined for platform {}".format(sys.platform))
    sys.exit(1)
timeout = 5
EXITCODE_TO = 124
exercise_path = ""

tests_ex1 = [
    ("lexer/valid/test1", "true", "false"),
    ("lexer/invalid/test2", "false", "false"),
    ("lexer/valid/test3", "false", "true"),
    ("lexer/invalid/test4", "false", "false"),
    ("lexer/valid/test5", "false", "true"),
    ("lexer/invalid/test6", "false", "false"),
    ("lexer/invalid/test7", "false", "false"),
    ("lexer/valid/test8", "false", "true"),
    ("lexer/valid/test9", "false", "true"),
    ("lexer/valid/test10", "false", "true"),
    ("lexer/invalid/test11", "false", "false"),
    ("lexer/valid/test12", "false", "true"),
    ("lexer/invalid/fo", "false", "false"),
    ("lexer/invalid/whil", "false", "false"),
    ("lexer/valid/test13", "false", "true"),
    ("lexer/invalid/test14", "false", "false"),
    ("lexer/invalid/test15", "false", "false"),
    ("lexer/valid/test16", "false", "true"),
    ("lexer/valid/test17", "false", "true"),
    ("lexer/valid/test18", "false", "true"),
]

tests_ex2_valid = [
    ("lexer/valid/broken_while", "(ID, whi), (ID, le), (LPAR, (), (ID, x), (NEQ, !=), (ID, y), (RPAR, )), (LBRACE, {), (EOF, $)"),
    ("lexer/valid/fizzbuzz", "(INT, int), (ID, upperBound), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, i), (ASSIGN, =), (NUMBER, 0), (SEMICOLON, ;), (INT, int), (ID, printNumber), (ASSIGN, =), (NUMBER, 1), (SEMICOLON, ;), (WHILE, while), (LPAR, (), (ID, i), (LEQ, <=), (ID, upperBound), (RPAR, )), (LBRACE, {), (ID, printNumber), (ASSIGN, =), (NUMBER, 1), (SEMICOLON, ;), (IF, if), (LPAR, (), (ID, i), (MOD, %), (NUMBER, 3), (EQ, ==), (NUMBER, 0), (RPAR, )), (LBRACE, {), (WRITE, write), (LPAR, (), (STRING, \"Fizz\"), (RPAR, )), (SEMICOLON, ;), (ID, printNumber), (ASSIGN, =), (NUMBER, 0), (SEMICOLON, ;), (RBRACE, }), (IF, if), (LPAR, (), (ID, i), (MOD, %), (NUMBER, 5), (EQ, ==), (NUMBER, 0), (RPAR, )), (LBRACE, {), (WRITE, write), (LPAR, (), (STRING, \"Buzz\"), (RPAR, )), (SEMICOLON, ;), (ID, printNumber), (ASSIGN, =), (NUMBER, 0), (SEMICOLON, ;), (RBRACE, }), (IF, if), (LPAR, (), (ID, printNumber), (EQ, ==), (NUMBER, 1), (RPAR, )), (LBRACE, {), (WRITE, write), (LPAR, (), (ID, i), (RPAR, )), (SEMICOLON, ;), (RBRACE, }), (ID, i), (INC, ++), (SEMICOLON, ;), (RBRACE, }), (EOF, $)"),
    ("lexer/valid/fahrenheit", "(WRITE, write), (LPAR, (), (STRING, \"Please enter a temperature in Celsius without comma/point: (example: 32.15 should be entered as 3215)\"), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, celsius), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, fahrenheit), (ASSIGN, =), (NUMBER, 180), (TIMES, *), (ID, celsius), (PLUS, +), (NUMBER, 3200), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (STRING, \"Temperature in Fahrenheit is: \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, fahrenheit), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/keyword_in_keyword", "(WRITE, write), (LPAR, (), (STRING, \"write\"), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/keyword_variable", "(INT, int), (ID, write2), (ASSIGN, =), (NUMBER, 10), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/long_comment", "(EOF, $)"),
    ("lexer/valid/long_comment_stars", "(EOF, $)"),
    ("lexer/valid/mickey", "(ID, Mickey), (ID, Maus), (NOT, !), (EOF, $)"),
    ("lexer/valid/notneq", "(WHILE, while), (LPAR, (), (ID, x), (NOT, !), (NEQ, !=), (ID, y), (RPAR, )), (LBRACE, {), (EOF, $)"),
    ("lexer/valid/old_source_code", "(INT, int), (ID, x), (SEMICOLON, ;), (INT, int), (ID, y), (SEMICOLON, ;), (ID, x), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (ID, y), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (WHILE, while), (LPAR, (), (ID, x), (NEQ, !=), (ID, y), (RPAR, )), (LBRACE, {), (IF, if), (LPAR, (), (ID, y), (LEQ, <=), (ID, x), (RPAR, )), (LBRACE, {), (ID, y), (ASSIGN, =), (ID, y), (MINUS, -), (ID, x), (SEMICOLON, ;), (RBRACE, }), (ELSE, else), (LBRACE, {), (ID, x), (ASSIGN, =), (ID, x), (MINUS, -), (ID, y), (SEMICOLON, ;), (RBRACE, }), (RBRACE, }), (WRITE, write), (LPAR, (), (STRING, \"GCD: \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, x), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/one_symbol", "(LBRACE, {), (EOF, $)"),
    ("lexer/valid/quotient_and_remainder", "(WRITE, write), (LPAR, (), (STRING, \"Enter dividend: \"), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, dividend), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (STRING, \"Enter divisor: \"), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, divisor), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (INT, int), (ID, quotient), (ASSIGN, =), (ID, dividend), (DIV, /), (ID, divisor), (SEMICOLON, ;), (INT, int), (ID, remainder), (ASSIGN, =), (ID, dividend), (MOD, %), (ID, divisor), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (STRING, \"Quotient = \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, quotient), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (STRING, \" and Remainder = \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, remainder), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/random_walk", "(INT, int), (ID, x), (ASSIGN, =), (NUMBER, 10), (SEMICOLON, ;), (INT, int), (ID, s), (ASSIGN, =), (NUMBER, 0), (SEMICOLON, ;), (WHILE, while), (LPAR, (), (ID, x), (GT, >), (NUMBER, 0), (RPAR, )), (LBRACE, {), (INT, int), (ID, b), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (MOD, %), (NUMBER, 2), (SEMICOLON, ;), (IF, if), (LPAR, (), (ID, b), (EQ, ==), (NUMBER, 1), (RPAR, )), (LBRACE, {), (ID, x), (ASSIGN, =), (ID, x), (PLUS, +), (NUMBER, 1), (SEMICOLON, ;), (RBRACE, }), (ELSE, else), (LBRACE, {), (ID, x), (ASSIGN, =), (ID, x), (MINUS, -), (NUMBER, 1), (SEMICOLON, ;), (RBRACE, }), (ID, s), (INC, ++), (SEMICOLON, ;), (RBRACE, }), (WRITE, write), (LPAR, (), (STRING, \"I stopped walking after: \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, s), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (STRING, \" steps\"), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/spaces", "(INT, int), (ID, x), (SEMICOLON, ;), (INT, int), (ID, y), (SEMICOLON, ;), (ID, x), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (ID, y), (ASSIGN, =), (READ, read), (LPAR, (), (RPAR, )), (SEMICOLON, ;), (WHILE, while), (LPAR, (), (ID, x), (NEQ, !=), (ID, y), (RPAR, )), (LBRACE, {), (IF, if), (LPAR, (), (ID, y), (LEQ, <=), (ID, x), (RPAR, )), (LBRACE, {), (ID, y), (ASSIGN, =), (ID, y), (MINUS, -), (ID, x), (SEMICOLON, ;), (RBRACE, }), (ELSE, else), (LBRACE, {), (ID, x), (ASSIGN, =), (ID, x), (MINUS, -), (ID, y), (SEMICOLON, ;), (RBRACE, }), (RBRACE, }), (WRITE, write), (LPAR, (), (STRING, \"GCD: \"), (RPAR, )), (SEMICOLON, ;), (WRITE, write), (LPAR, (), (ID, x), (RPAR, )), (SEMICOLON, ;), (EOF, $)"),
    ("lexer/valid/test0", "(INT, int), (ID, a), (ASSIGN, =), (NUMBER, 0), (SEMICOLON, ;), (READ, read), (LPAR, (), (ID, a), (RPAR, )), (SEMICOLON, ;), (WHILE, while), (LPAR, (), (ID, a), (LT, <), (NUMBER, 17), (RPAR, )), (LBRACE, {), (WRITE, write), (LPAR, (), (ID, a), (RPAR, )), (SEMICOLON, ;), (ID, a), (ASSIGN, =), (ID, a), (PLUS, +), (NUMBER, 1), (SEMICOLON, ;), (RBRACE, }), (EOF, $)"),
    ("lexer/valid/two_symbols", "(LBRACE, {), (LBRACE, {), (EOF, $)"),
]
tests_ex2_invalid = [
    ("lexer/invalid/colon", "62"),
    ("lexer/invalid/underscore", "38"),
    ("lexer/invalid/half_and", "50"),
    ("lexer/invalid/half_or", "50"),
]

tests_ex4 = [
    # (Tokens, grammar, input, in LR0?, valid input?)
    ("lr0", "lr0", "lr0", True, True),
    ("slr1", "slr1", "slr1", False, True),
    (None, None, "while_1", False, True),
    (None, None, "while_2", False, True),
    (None, None, "while_3", False, True),
    (None, None, "while_4", False, True),
    (None, None, "while_5", False, True),
    (None, None, "while_6", False, False),
    (None, None, "while_7", False, False),
    (None, None, "while_8", False, False),
    (None, None, "while_9", False, False),
    (None, None, "while_10", False, False),
    ("simple", "simple", "simple_1", True, True),
    ("simple", "simple", "simple_2", True, True),
    ("simple", "simple", "simple_3", True, True),
    ("simple", "simple", "simple_4", True, True),
    ("simple", "simple", "simple_5", True, True),
    ("simple", "simple", "simple_6", True, False),
    ("simple", "simple", "simple_7", True, False),
    ("simple", "simple", "simple_8", True, False),
    ("simple", "simple", "simple_9", True, False),
    ("simple", "simple", "simple_10", True, False),
    ("simple", "grammar_2", "grammar2_1", False, True),
    ("simple", "grammar_2", "grammar2_2", False, False),
    ("simple", "grammar_3", "grammar3_1", False, True),
    ("simple", "grammar_4", "grammar4_1", True, True),
    ("simple", "grammar_4", "grammar4_2", True, False),
    ("simple", "grammar_5", "grammar5_1", True, True),
    ("simple", "grammar_5", "grammar5_2", True, False),
    ("simple", "grammar_6", "grammar6_1", True, True),
    ("tokens_7", "grammar_7", "grammar7_1", False, True),
    ("tokens_8", "grammar_8", "grammar8_1", True, True),
    ("simple", "grammar_9", "grammar9_1", False, False),
]

tests_ex5 = [
    ("checker/valid/checkerTest1", True),
    ("checker/valid/checkerTest2", True),
    ("checker/valid/checkerTest3", True),
    ("checker/valid/checkerTest4", True),
    ("checker/valid/checkerTest5", True),
    ("checker/valid/checkerTest6", True),
    ("checker/invalid/checkerTest1", False),
    ("checker/invalid/checkerTest2", False),
    ("checker/invalid/checkerTest3", False),
    ("checker/invalid/checkerTest4", False),
    ("checker/invalid/checkerTest5", False),
    ("checker/invalid/checkerTest6", False),
    ("checker/invalid/checkerTest7", False),
    ("checker/invalid/checkerTest8", False),
    ("checker/invalid/checkerTest9", False),
]

# Unzip archive
def unzip_one(path, name, remove = True):
    print("Unzipping: {}".format(name))
    new_name = name[:-4]
    subPath = os.path.join(path, new_name)
    ret = subprocess.call(["unzip", "-qq", os.path.join(path, name), "-d", subPath])
    found = True
    if not "i2Compiler" in os.listdir(subPath):
        if "src" in os.listdir(subPath):
            print("WARNING: Directory src found, moving to i2Compiler - HALF POINTS!")
            os.mkdir(os.path.join(subPath, "i2Compiler"))
            for file in os.listdir(subPath):
                if file.startswith('i2Compiler'):
                    continue
                os.rename(os.path.join(subPath, file), os.path.join(os.path.join(subPath, "i2Compiler"), file))
        else:
            print("WARNING: Directory i2Compiler not found!")
            found = False
    if remove:
        subprocess.call(["rm", os.path.join(path, name)])
    # Return success
    return ret == 0 and found

def unzip_all(path, remove = True):
    no_all = 0
    no_fail = 0
    for file in sorted(os.listdir(path)):
        if file.endswith(".zip"):
            no_all += 1
            success = unzip_one(path, file, remove)
            no_fail += 0 if success else 1
    return no_all, no_fail

def unzip(path, name, remove = True):
    if name:
        return 1, 0 if unzip_one(path, name, remove) else 1
    else:
        return unzip_all(path, remove)


# Rename project
def rename_one(path, name):
    print("Renaming: {}".format(name))
    project_path = os.path.join(path, name, "i2Compiler/.project")
    replace = "s/<name>i2Compiler.*<\/name>/<name>i2Compiler_{}<\/name>/g".format(name)
    ret = subprocess.call(["sed", "-i", "", replace, project_path])
    return ret == 0

def rename_all(path):
    no_all = 0
    no_fail = 0
    for name in sorted(os.listdir(path)):
        if name.startswith('.'):
            continue
        no_all += 1
        success = rename_one(path, name)
        no_fail += 0 if success else 1
    return no_all, no_fail

def rename(path, name):
    if name:
        return 1, 0 if rename_one(path, name) else 1
    else:
        return rename_all(path)


# Compile source code
def compile_one(path, name):
    print("Compiling: {}".format(name))
    project_path = os.path.join(path, name, "i2Compiler")
    bin_path = os.path.join(project_path, "bin")
    # Clear bin dir and create anew
    if os.path.exists(bin_path):
        shutil.rmtree(bin_path)
    os.makedirs(bin_path)

    ret = subprocess.call(["javac", "-d", bin_path, "-sourcepath", os.path.join(project_path, "src"), os.path.join(project_path, "src/Main.java")])
    return ret == 0

def compile_all(path):
    no_all = 0
    no_fail = 0
    for name in sorted(os.listdir(path)):
        if not os.path.isdir(os.path.join(path, name)) or name.startswith('.'):
            continue
        no_all += 1
        success = compile_one(path, name)
        no_fail += 0 if success else 1
    return no_all, no_fail

def compile(path, name):
    if name:
        return 1, 0 if compile_one(path, name) else 1
    else:
        return compile_all(path)


def call_process(args, test_file):
    """
    Call (Java) process with timeout.
    :param args: Arguments for process.
    :test_file: Name of test for better error message.
    :return: Output or None if there was an error.
    """
    try:
        outputstr = subprocess.check_output([timeout_cmd, str(timeout)] + args, stderr=subprocess.STDOUT, shell=False, universal_newlines=True)
    except subprocess.CalledProcessError as e:
        if e.returncode == EXITCODE_TO:
            print("Timeout on {}".format(test_file))
        else:
            print("Error on {}: {}".format(test_file, e))
        return None
    return outputstr

# Check exercise solution
def check_ex1(bin_path, name):
    correct = 0
    test_path = os.path.join(exercise_path, "i2CompilerSolution/tests")
    for (test_file, resWhile, resComment) in tests_ex1:
        args = ['java', "-cp",  bin_path, "Main", os.path.join(test_path, "{}.txt".format(test_file))]
        output = call_process(args, test_file)
        if output is None:
            continue
        matchWhile = re.search(r'WHILE: (true|false)', output).group(1)
        matchComment = re.search(r'COMMENT: (true|false)', output).group(1)
        if matchWhile == resWhile:
            if matchComment == resComment:
                correct += 1
            else:
                print("Incorrect result for COMMENT on {}: {} but expected {}".format(test_file, matchComment, resComment))
        else:
            print("Incorrect result for WHILE on {}: {} but expected {}".format(test_file, matchWhile, resWhile))
    print("{} of {} correct".format(correct, len(tests_ex1)))
    return correct == len(tests_ex1)

def check_ex2(bin_path, name):
    correct = 0
    test_path = os.path.join(exercise_path, "i2CompilerSolution/tests")
    for (test_file, resSymbol) in tests_ex2_valid:
        args = ['java', "-cp",  bin_path, "Main", os.path.join(test_path, "{}.txt".format(test_file))]
        output = call_process(args, test_file)
        if output is None:
            continue
        match = re.search(r'Symbol stream: \[(.*)\]', output)
        if match:
            matchSymbol = match.group(1)
            if matchSymbol == resSymbol:
                correct += 1
            else:
                print("Incorrect result on {}: {} but expected {}".format(test_file, matchSymbol, resSymbol))
        else:
            print("Could not verify result on {}: {}".format(test_file, output))
    for (test_file, resSymbol) in tests_ex2_invalid:
        args = ['java', "-cp",  bin_path, "Main", os.path.join(test_path, "{}.txt".format(test_file))]
        output = call_process(args, test_file)
        if output is None:
            continue
        match = re.match(r'LexErr$', output, re.MULTILINE)
        if match:
            correct += 1
        else:
            print("Incorrect result on {}: No LexErr found".format(test_file))
    print("{} of {} correct".format(correct, len(tests_ex2_valid) + len(tests_ex2_invalid)))
    return correct == (len(tests_ex2_valid) + len(tests_ex2_invalid))

def check_ex4(bin_path, name, quiet):
    project_path = os.path.join(bin_path, "..")
    path_reference = os.path.join(exercise_path, "i2CompilerSolution")
    unittest_path = os.path.join(project_path, "src/unittests")
    unittest_path_reference = os.path.join(path_reference, "src/unittests")
    test_path = os.path.join(path_reference, "tests")
    tokens_path = os.path.join(test_path, "tokens")
    grammar_path = os.path.join(test_path, "grammar")
    input_path = os.path.join(test_path, "input")

    # Copy files
    if not quiet:
        print("Copying files...")
    # Main
    shutil.copyfile(os.path.join(path_reference, "src/MainCheck.java"), os.path.join(project_path, "src/MainCheck.java"))
    # Parser reference
    if not os.path.exists(unittest_path):
        os.makedirs(unittest_path)
    shutil.copyfile(os.path.join(unittest_path_reference, "LR0ParserReference.java"), os.path.join(unittest_path, "LR0ParserReference.java"))
    shutil.copyfile(os.path.join(unittest_path_reference, "LR0SetGeneratorReference.java"), os.path.join(unittest_path, "LR0SetGeneratorReference.java"))
    shutil.copyfile(os.path.join(unittest_path_reference, "LookAheadGeneratorReference.java"), os.path.join(unittest_path, "LookAheadGeneratorReference.java"))
    shutil.copyfile(os.path.join(unittest_path_reference, "ParserChecker.java"), os.path.join(unittest_path, "ParserChecker.java"))
    shutil.copyfile(os.path.join(unittest_path_reference, "SLR1ParserReference.java"), os.path.join(unittest_path, "SLR1ParserReference.java"))

    # Recompile
    if not quiet:
        print("Recompiling...")
    # Clear bin dir and create anew
    if os.path.exists(bin_path):
        shutil.rmtree(bin_path)
    os.makedirs(bin_path)
    ret = subprocess.call(["javac", "-d", bin_path, "-sourcepath", os.path.join(project_path, "src"), os.path.join(project_path, "src/MainCheck.java")])
    if ret != 0:
        print("Recompiling not successful. Aborting.")
        return False


    # Run tests (are performed internally in Java)
    if not quiet:
        print("Testing...")
    correctLR0Sets = 0
    correctLR0Conflicts = 0
    correctLR0Parsing = 0
    correctFirstFollow = 0
    correctSLR1Parsing = 0
    noTotal = len(tests_ex4)
    for (tokens_file, grammar_file, input_file, lr0_valid, input_valid) in tests_ex4:
        args = [timeout_cmd, '10', 'java', "-cp",  bin_path, "MainCheck"]
        args.append(os.path.join(input_path, "{}.txt".format(input_file)))
        if tokens_file is not None:
            assert grammar_file is not None
            args.extend(["--tokens", os.path.join(tokens_path, "{}.txt".format(tokens_file)),
                         "--grammar", os.path.join(grammar_path, "{}.txt".format(grammar_file))])
        args.extend([str(lr0_valid), str(input_valid)])

        outputstr = ""
        try:
            outputstr = subprocess.check_output(args, stderr=subprocess.STDOUT, shell=False, universal_newlines=True)
        except subprocess.CalledProcessError as e:
            if not quiet:
                print("Error on {}: {}\nOutput:\n-----\n{}-----\n".format(input_file, e, outputstr))
            continue
        if not quiet:
            print("Results on {}:\n-----\n{}-----\n".format(input_file, outputstr))
        if 'LR0 sets correct.' in outputstr:
            correctLR0Sets += 1
        if 'LR0 conflicts correct.' in outputstr:
            correctLR0Conflicts += 1
        if 'First and follow sets correct.' in outputstr:
            correctFirstFollow += 1
        if 'LR0 parsing correct.' in outputstr:
            correctLR0Parsing += 1
        if 'SLR1 parsing correct.' in outputstr:
            correctSLR1Parsing += 1

    print("===== Results =====")
    print("{}/{} correct LR0 sets".format(correctLR0Sets, noTotal))
    print("{}/{} correct LR0 conflicts".format(correctLR0Conflicts, noTotal))
    print("{}/{} correct LR0 parsing".format(correctLR0Parsing, noTotal))
    print("{}/{} correct first/follow sets".format(correctFirstFollow, noTotal))
    print("{}/{} correct SLR1 parsing".format(correctSLR1Parsing, noTotal))
    return correctLR0Sets == noTotal and correctLR0Conflicts == noTotal and correctLR0Parsing == noTotal and correctFirstFollow == noTotal and correctSLR1Parsing == noTotal

def check_ex5(bin_path, name):
    correct = 0
    test_path = os.path.join(exercise_path, "i2CompilerSolution/tests")
    for (test_file, resDeclared) in tests_ex5:
        args = [timeout_cmd, '10', 'java', "-cp",  bin_path, "Main"]
        args.append(os.path.join(test_path, "{}.txt".format(test_file)))
        output = call_process(args, test_file)
        if output is None:
            continue
        match = re.search(r'Every variable was declared before use: (.*)', output, re.MULTILINE)
        if match:
            matchDeclared = match.group(1)
            if matchDeclared == ("true" if resDeclared else "false"):
                correct += 1
            else:
                print("Incorrect result on {}: {} but expected {}".format(test_file, matchDeclared, resDeclared))
        else:
            print("Error on {} {}".format(test_file, output))
    print("{} of {} correct".format(correct, len(tests_ex5)))


def check_one(path, name, number, quiet):
    bin_path = os.path.join(path, name, "i2Compiler/bin")
    if number == 1:
        print("Checking exercise 1 for {}".format(name))
        return check_ex1(bin_path, name)
    elif number == 2:
        print("Checking exercise 2 for {}".format(name))
        return check_ex2(bin_path, name)
    elif number == 4:
        print("Checking exercise 4 for {}".format(name))
        return check_ex4(bin_path, name, quiet)
    elif number == 5:
        print("Checking exercise 5 for {}".format(name))
        return check_ex5(bin_path, name)
    else:
        print("Invalid exercise number {}".format(number))
        return False

def check_all(path, number, quiet):
    no_all = 0
    no_fail = 0
    for name in sorted(os.listdir(path)):
        if not os.path.isdir(os.path.join(path, name)) or name.startswith('.'):
            continue
        no_all += 1
        success = check_one(path, name, number, quiet)
        no_fail += 0 if success else 1
    return no_all, no_fail

def check(path, name, number, quiet):
    if name:
        return 1, 0 if check_one(path, name, number, quiet) else 1
    else:
        return check_all(path, number, quiet)


# Main method
if __name__ == "__main__":
    help_str = "The workflow is as follows:\n\t--onedir\t Move solutions zip files into one directory\n\t--unzip\t\t Unzip student solutions\n\t--compile\t Compile solutions\n\t--check no\t Check given exercise"
    parser = argparse.ArgumentParser(description='Check compiler construction implementations of students.\n' + help_str, formatter_class=argparse.RawTextHelpFormatter)

    parser.add_argument('--path', help='the path with all solutions', required=True)
    parser.add_argument('--onedir', help='move solutions into one dir', action="store_true")
    parser.add_argument('--unzip', help='unzip solutions', action="store_true")
    parser.add_argument('--rename', help='rename projects', action="store_true")
    parser.add_argument('--compile', help='compile solutions', action="store_true")
    parser.add_argument('--check', help='check solution for given exercise', type=int, default=0)
    parser.add_argument('--solution', help='the name of a specific solution to consider instead of all', default="")
    parser.add_argument('--quiet', help='reduce output to minimum', action="store_true")

    args = parser.parse_args()

    solution = ""
    if not args.solution == "":
        for file in os.listdir(args.path):
            if str(file).startswith(args.solution):
                solution = str(file)
                break
        if solution == "":
            print("Solution '{}' not found.".format(args.solution))
            exit(1)

    task_done = False
    if args.onedir:
        for name in os.listdir(args.path):
            for file in os.listdir(os.path.join(args.path, name)):
                os.rename(os.path.join(args.path, name, file), os.path.join(args.path, "{}-{}".format(name, file)))
            os.rmdir(os.path.join(args.path, name))
        task_done = True
    if args.unzip:
        remove = True
        no_all, no_fail = unzip(args.path, solution, remove=remove)
        task_done = True
        print("-"*30)
        print("Unzipping: {}/{} failed.".format(no_fail, no_all))
    if args.rename:
        no_all, no_fail = rename(args.path, solution)
        task_done = True
        print("-"*30)
        print("Renaming: {}/{} failed.".format(no_fail, no_all))
    if args.compile:
        no_all, no_fail = compile(args.path, solution)
        task_done = True
        print("-"*30)
        print("Compiling: {}/{} failed.".format(no_fail, no_all))
    if args.check > 0:
        no_all, no_fail = check(args.path, solution, args.check, args.quiet)
        task_done = True
        print("-"*30)
        print("Checking {}: {}/{} failed.".format(args.check, no_fail, no_all))

    if not task_done:
        print("No task chosen")
