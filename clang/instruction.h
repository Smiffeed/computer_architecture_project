#ifndef INSTRUCTION_H
#define INSTRUCTION_H

#include "register.h"

typedef struct {
    int step;
    char opcode[4];
    Register* register_ptr;
    int clock_cycles;
    char operand[10];
    int value;
} Instruction;

void process_instruction(char* input, Register* registers, 
                        Instruction* instructions, int* step);

void instruction_init(Instruction* inst, int step, const char* opcode, 
                     Register* reg, int clock_cycles, const char* operand);
char* five_bit_opcode(const char* opcode);
void instruction_to_string(Instruction* inst, char* buffer);

#endif 