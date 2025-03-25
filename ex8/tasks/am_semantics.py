class state:
    prog_counter : int
    data_stack : list
    procedure_stack : list

    def __str__(self) -> str:
        return "("+str(self.prog_counter)+", "+str(self.data_stack)+", "+str(self.procedure_stack)+")"


def sem_add(s : state):
    z1 = s.data_stack.pop()
    z2 = s.data_stack.pop()
    s.data_stack.append(z1 + z2)
    s.prog_counter += 1


def sem_not(s : state):
    b = s.data_stack.pop()
    s.data_stack.append(~b)
    s.prog_counter += 1


def sem_and(s : state):
    b1 = s.data_stack.pop()
    b2 = s.data_stack.pop()
    s.data_stack.append(b1 & b2)
    s.prog_counter += 1


def sem_or(s : state):
    b1 = s.data_stack.pop()
    b2 = s.data_stack.pop()
    s.data_stack.append(b1 | b2)
    s.prog_counter += 1


def sem_lt(s : state):
    z1 = s.data_stack.pop()
    z2 = s.data_stack.pop()
    if z1 < z2:
        s.data_stack.append(1)
    else:
        s.data_stack.append(0)
    s.prog_counter += 1


def sem_jmp(counter_address: int, s : state):
    s.prog_counter = counter_address


def sem_jfalse(counter_address: int, s : state):
    b = s.data_stack.pop()
    if b:
        s.prog_counter += 1
    else:
        s.prog_counter = counter_address

def base(procedure_stack : list, diff : int):
    if diff == 0:
        return 1
    else:
        frame_location = base(procedure_stack, diff-1)
        offset = procedure_stack[len(procedure_stack) - frame_location]
        return frame_location + offset


def sem_call(counter_address : int, diff : int, loc : int, s : state):
    sl = base(s.procedure_stack, diff) + loc + 2
    for i in range(loc):
        s.procedure_stack.append(0)
    s.procedure_stack.append(s.prog_counter+1)
    s.procedure_stack.append(loc + 2)
    s.procedure_stack.append(sl)
    s.prog_counter = counter_address

def sem_ret(s : state):
    sl = s.procedure_stack.pop()
    dl = s.procedure_stack.pop()
    ra = s.procedure_stack[-1]
    for i in range(dl-1):
        s.procedure_stack.pop()
    s.prog_counter = ra

def sem_load(dif : int, off : int, s: state):
    frame_location = base(s.procedure_stack, dif) + off + 2
    data = s.procedure_stack[len(s.procedure_stack) - frame_location]
    s.data_stack.pop(data)
    s.prog_counter += 1

def sem_store(dif : int, off : int, s : state):
    value = s.data_stack.pop()
    frame_location = base(s.procedure_stack, dif) + off + 2
    s.procedure_stack[len(s.procedure_stack) - frame_location] = value
    s.prog_counter += 1

def sem_lit(z : int, s : state):
    s.data_stack.append(z)
    s.prog_counter += 1

def sem_am(program : list, s : state):
    while s.prog_counter in range(1,len(program)+1):
        print(s)
        (cmd, a1, a2, a3) = program[s.prog_counter]
        if cmd == "add":
            sem_add(s)
        elif cmd == "not":
            sem_not(s)
        elif cmd == "and":
            sem_and(s)
        elif cmd == "or":
            sem_or(s)
        elif cmd == "lt":
            sem_lt(s)
        elif cmd == "jmp":
            sem_jmp(a1, s)
        elif cmd == "jfalse":
            sem_jfalse(a1, s)
        elif cmd == "call":
            sem_call(a1, a2, a3, s)
        elif cmd == "ret":
            sem_ret(s)
        elif cmd == "load":
            sem_load(a1, a2, s)
        elif cmd == "store":
            sem_store(a1, a2, s)
        elif cmd == "lit":
            sem_lit(a1, s)

#              0              1                  2                  3                 4                   5                 6
ex_prog = [("empty",0,0,0),("lit", 5, 0, 0), ("store", 0, 1, 0), ("jmp", 7, 0, 0), ("call", 9, 1, 0), ("lit", 0, 0, 0), ("lit", 1, 0, 0),
#               7                8                  9                   10                  11                    12
           ("call", 10, 0, 1), ("jmp", 0, 0, 0), ("call", 8, 0, 1), ("lit", -1, 0, 0), ("store", 0, 1, 0), ("jmp", 4, 0, 0)]

s = state()
s.prog_counter = 1
s.data_stack = []
s.procedure_stack = [0, 0, 0, 0]

sem_am(ex_prog,s)

print(s)
