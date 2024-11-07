#ifndef REGISTER_H
#define REGISTER_H

typedef struct {
    char address[3];
    int value;
} Register;

void register_init(Register* reg, int value, const char* address);
char* to_16bit_value(int value, char* buffer);
char* to_3bit_address(const char* address);

#endif 