class Instruction:
    def __init__(self, step, opcode, register, clock_cycles, operand=""):
        self.step = step
        self.opcode = opcode
        self.register = register
        self.clock_cycles = clock_cycles
        self.value = register.value
        self.operand = operand

    def five_bit_opcode(self):
        opcode_map = {
            'mov': '00001',
            'add': '00010',
            'mul': '00100',
            'sub': '00011',
            'div': '00101'
        }
        return opcode_map.get(self.opcode, 'end 0 0')

    def __str__(self):
        operand_value = str(self.value) if self.operand == "" else self.operand
        return f"[{self.step}] {self.opcode:<5}{self.register.address:<4} {self.operand:<5}{self.register.to_16bit_value():<22}{self.clock_cycles}" 