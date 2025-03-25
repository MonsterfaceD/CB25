import os
import subprocess
import re
import argparse
import shutil

timeout_cmd = "gtimeout"

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


# Unzip archive
def unzip_one(path, name, remove = True):
    print("Unzipping: {}".format(name))
    new_name = name[:-4]
    ret = subprocess.call(["unzip", "-qq", os.path.join(path, name), "-d", os.path.join(path, new_name)])
    found = True
    if not "i2Compiler" in os.listdir(os.path.join(path, new_name)):
        print("WARNING: Directory i2Compiler not found!")
        found = False
    if remove:
        subprocess.call(["rm", os.path.join(path, name)])
    # Return success
    return ret == 0 and found

def unzip_all(path, remove = True):
    no_all = 0
    no_fail = 0
    for file in os.listdir(path):
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
    for name in os.listdir(path):
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
    # Clear bin dir
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


# Check exercise solution
def check_ex1(bin_path, name):
    correct = 0
    test_path = os.path.join(exercise_path, "i2CompilerSolution/tests")
    for (test_file, resWhile, resComment) in tests_ex1:
        args = ['java', "-cp",  bin_path, "Main", os.path.join(test_path, "{}.txt".format(test_file))]
        try:
            outputstr = subprocess.check_output(args, stderr=subprocess.STDOUT, shell=False, universal_newlines=True)
            #print("Output: {}".format(outputstr))
            matchWhile = re.search(r'WHILE: (true|false)', outputstr).group(1)
            matchComment = re.search(r'COMMENT: (true|false)', outputstr).group(1)
            if matchWhile == resWhile:
                if matchComment == resComment:
                    correct += 1
                else:
                    print("Incorrect result for COMMENT on {}: {} but expected {}".format(test_file, matchComment, resComment))
            else:
                print("Incorrect result for WHILE on {}: {} but expected {}".format(test_file, matchWhile, resWhile))
        except:
            print("Error happened while checking")
    print("{} of {} correct".format(correct, len(tests_ex1)))
    return correct == len(tests_ex1)

def check_one(path, name, number):
    bin_path = os.path.join(path, name, "i2Compiler/bin")
    if number == 1:
        print("Checking exercise 1 for {}".format(name))
        return check_ex1(bin_path, name)
    else:
        print("Invalid exercise number {}".format(number))
        return False

def check_all(path, number):
    no_all = 0
    no_fail = 0
    for name in sorted(os.listdir(path)):
        if not os.path.isdir(os.path.join(path, name)) or name.startswith('.'):
            continue
        no_all += 1
        success = check_one(path, name, number)
        no_fail += 0 if success else 1
    return no_all, no_fail

def check(path, name, number):
    if name:
        return 1, 0 if check_one(path, name, number) else 1
    else:
        return check_all(path, number)


# Main method
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Check compiler construction implementations of students.')

    parser.add_argument('--path', help='the path with all solutions', required=True)
    parser.add_argument('--onedir', help='move solutions into one dir', action="store_true")
    parser.add_argument('--unzip', help='unzip solutions', action="store_true")
    parser.add_argument('--rename', help='rename projects', action="store_true")
    parser.add_argument('--compile', help='compile solutions', action="store_true")
    parser.add_argument('--check', help='check solution for given exercise', type=int, default=0)
    parser.add_argument('--solution', help='the name of a specific solution to consider instead of all', default="")

    args = parser.parse_args()

    solution = ""
    if not args.solution == "":
        for file in os.listdir(args.path):
            if str(file).startswith(args.solution):
                solution = str(file)
                break
        if solution == "":
            print("Solution '{}' not found.".format(args.solution))

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
        no_all, no_fail = check(args.path, solution, args.check)
        task_done = True
        print("-"*30)
        print("Checking {}: {}/{} failed.".format(args.check, no_fail, no_all))

    if not task_done:
        print("No task chosen")
