from register import Register
from instruction import Instruction

def main():
    registers = [Register(0, f"r{i}") for i in range(8)]
    steps = []
    step = 0

    while True:
        try:
            instruction = input("Input instructions: ")
            if instruction == "end 0 0":
                break

            opcode, operand1, operand2 = instruction.split()
            
            # Find destination register
            dest_reg = next((reg for reg in registers if reg.address == operand1), None)
            if not dest_reg:
                continue

            # Try to process as immediate value first
            try:
                value = int(operand2)
                if opcode == "mov":
                    dest_reg.value = value
                    steps.append(Instruction(step, opcode, dest_reg, 1, operand2))
                elif opcode == "add":
                    dest_reg.value += value
                    steps.append(Instruction(step, opcode, dest_reg, 2, operand2))
                elif opcode == "sub":
                    dest_reg.value -= value
                    steps.append(Instruction(step, opcode, dest_reg, 3, operand2))
                elif opcode == "mul":
                    result = dest_reg.value * value
                    registers[7].value = result >> 32  # High bits to r7
                    dest_reg.value = result & 0xFFFFFFFF  # Low bits to destination
                    steps.append(Instruction(step, opcode, dest_reg, 4, operand2))
                elif opcode == "div":
                    if value != 0:
                        registers[7].value = dest_reg.value % value  # Remainder to r7
                        dest_reg.value //= value
                        steps.append(Instruction(step, opcode, dest_reg, 4, operand2))

            # If not a number, try register-to-register operation
            except ValueError:
                src_reg = next((reg for reg in registers if reg.address == operand2), None)
                if src_reg:
                    if opcode == "mov":
                        dest_reg.value = src_reg.value
                        steps.append(Instruction(step, opcode, dest_reg, 1, operand2))
                    elif opcode == "add":
                        dest_reg.value += src_reg.value
                        steps.append(Instruction(step, opcode, dest_reg, 2, operand2))
                    elif opcode == "sub":
                        dest_reg.value -= src_reg.value
                        steps.append(Instruction(step, opcode, dest_reg, 3, operand2))
                    elif opcode == "mul":
                        dest_reg.value *= src_reg.value
                        steps.append(Instruction(step, opcode, dest_reg, 4, operand2))
                    elif opcode == "div":
                        if src_reg.value != 0:
                            dest_reg.value //= src_reg.value
                            steps.append(Instruction(step, opcode, dest_reg, 4, operand2))

            step += 1

        except (ValueError, IndexError):
            print("Invalid instruction format")
            continue

    # Print results
    print(f"{'Decoded Form':<19}{'Encoded Form':<24}Clock Cycles")
    for instruction in steps:
        print(instruction)

    print("-" * 47)
    print("Register States:")
    for instruction in steps:
        print(f"{instruction.register.to_3bit_address()} = {instruction.value}")

    print("-" * 47)
    print("Values of registers after executed")
    for reg in registers:
        print(f"{reg.address}   {reg.value}")

    print("-" * 47)
    total_cycles = sum(instruction.clock_cycles for instruction in steps)
    cpi = total_cycles / step if step > 0 else 0
    print(f"The program's CPI: {cpi:.2f}")
    
    print("-" * 47)
    print("Pipelined execution of the program:")
    print(" " * 17, end="")
    
    cycles_with_pipeline = len(steps) + 3
    for i in range(1, cycles_with_pipeline + 1):
        print(f"{i:>5}", end="")
    print()

    for s in steps:
        print(f"{s.step + 1}. {s.opcode:<5}{s.register.address:<4}{s.operand:<2} :", end="")
        print(" " * (5 * (s.step + 1)), end="")
        print("IF | ID | EX | WB")

    print(f"\nPipelined ICP ex {cycles_with_pipeline} clock cycles to complete the program execution")

if __name__ == "__main__":
    main() 