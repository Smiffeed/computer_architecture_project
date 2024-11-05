class CPU:
    def __init__(self):
        # Initialize 8 32-bit general purpose registers (r0-r7)
        self.registers = [0] * 8
        # Clock cycles counter
        self.clock_cycles = 0
        # CPI dictionary for different operations
        self.cpi = {
            'mov': 1,
            'add': 2,
            'sub': 2,
            'mul': 3,
            'div': 3,
            'end': 1
        }

    def execute_instruction(self, instruction):
        """Execute a single instruction and update clock cycles"""
        parts = instruction.lower().split()
        opcode = parts[0]
        
        # Update clock cycles
        self.clock_cycles += self.cpi.get(opcode, 1)
        
        if opcode == 'mov':
            dest = int(parts[1][1])  # Extract register number
            if parts[2].startswith('r'):
                # Register to register
                src = int(parts[2][1])
                self.registers[dest] = self.registers[src]
            else:
                # Immediate to register
                self.registers[dest] = int(parts[2])
                
        elif opcode == 'add':
            dest = int(parts[1][1])
            if parts[2].startswith('r'):
                # Register to register
                src = int(parts[2][1])
                self.registers[dest] += self.registers[src]
            else:
                # Immediate value
                self.registers[dest] += int(parts[2])
                
        elif opcode == 'mul':
            dest = int(parts[1][1])
            value = int(parts[2])
            result = self.registers[dest] * value
            # Store high bits in r7 for multiplication
            self.registers[7] = (result >> 32) & 0xFFFFFFFF
            self.registers[dest] = result & 0xFFFFFFFF
            
        elif opcode == 'div':
            dest = int(parts[1][1])
            divisor = int(parts[2])
            quotient = self.registers[dest] // divisor
            remainder = self.registers[dest] % divisor
            self.registers[7] = remainder  # Store remainder in r7
            self.registers[dest] = quotient
            
        elif opcode == 'end':
            return False
            
        return True

    def get_register_value(self, reg_num):
        """Get the value of a register in binary format"""
        value = self.registers[reg_num]
        return format(value & 0xFFFFFFFF, '032b')

    def print_registers(self):
        """Print the current state of all registers"""
        for i in range(8):
            print(f"r{i} = {self.registers[i]} [{self.get_register_value(i)}]")

def main():
    cpu = CPU()
    
    # Example program
    program = [
        "mov r1 3",
        "add r1 3",
        "mov r2 r1",
        "mul r2 -1",
        "mov r3 r2",
        "div r3 2",
        "end 0"
    ]
    
    # Execute program
    for instruction in program:
        if not cpu.execute_instruction(instruction):
            break
    
    # Print final register states and total clock cycles
    print("\nFinal register states:")
    cpu.print_registers()
    print(f"\nTotal clock cycles: {cpu.clock_cycles}")
    print(f"Average CPI: {cpu.clock_cycles / len(program):.2f}")

if __name__ == "__main__":
    main()
