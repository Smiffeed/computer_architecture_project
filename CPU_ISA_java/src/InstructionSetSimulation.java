import java.util.ArrayList;
import java.util.Scanner;

public class InstructionSetSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        String opcode = "";
        String operand = "";
        String operand2 = "";
        Register[] registers = new Register[8];
        for (int i = 0; i < 8; i++) {
            registers[i] = new Register(0, "r" + i);
        }
        ArrayList<Instruction> steps = new ArrayList<>();
        int step = 0;
        while (!(opcode.equals("end")) && !(operand.equals("0")) && !(operand2.equals("0"))) {
            System.out.print("Input instructions:");
            input = scanner.nextLine();
            if (input.equals("end 0 0")) {
                break;
            } else {
                String[] parts = input.split(" ");
                opcode = parts[0];
                operand = parts[1];
                operand2 = parts[2];
                Register destinationRegister = null;
                for (int i = 0; i < registers.length; i++) {
                    if ((registers[i].getAddress().equals(operand))) {
                        destinationRegister = registers[i];
                        break;
                    }
                }
                try {
                    int value = Integer.parseInt(operand2);
                    switch (opcode) {
                        case "mov":
                            destinationRegister.setValue(value);
                            steps.add(new Instruction(step, opcode, destinationRegister, 1, operand2));
                            break;
                        case "add":
                            destinationRegister.setValue(destinationRegister.getValue() + value);
                            steps.add(new Instruction(step, opcode, destinationRegister, 2, operand2));
                            break;
                        case "sub":
                            destinationRegister.setValue(destinationRegister.getValue() - value);
                            steps.add(new Instruction(step, opcode, destinationRegister, 3, operand2));
                            break;
                        case "mul":
                            long result = (long)destinationRegister.getValue() * value;
                            registers[7].setValue((int)(result >> 32)); // High bits to r7
                            destinationRegister.setValue((int)result); // Low bits to destination
                            steps.add(new Instruction(step, opcode, destinationRegister, 4, operand2));
                            break;
                        case "div":
                            int quotient = destinationRegister.getValue() / value;
                            int remainder = destinationRegister.getValue() % value;
                            registers[7].setValue(remainder); // Remainder to r7
                            destinationRegister.setValue(quotient);
                            steps.add(new Instruction(step, opcode, destinationRegister, 4, operand2));
                            break;
                    }
                } catch (NumberFormatException e) {
                    Register sourceRegister = null;
                    for (int i = 0; i < registers.length; i++) {
                        if (registers[i].getAddress().equals(operand2)) {
                            sourceRegister = registers[i];
                            break;
                        }
                    }
                    switch (opcode) {
                        case "mov":
                            destinationRegister.setValue(sourceRegister.getValue());
                            steps.add(new Instruction(step, opcode, destinationRegister, 1, operand2));
                            break;
                        case "add":
                            destinationRegister.setValue(destinationRegister.getValue() + sourceRegister.getValue());
                            steps.add(new Instruction(step, opcode, destinationRegister, 2, operand2));
                            break;
                        case "sub":
                            destinationRegister.setValue(destinationRegister.getValue() - sourceRegister.getValue());
                            steps.add(new Instruction(step, opcode, destinationRegister, 3, operand2));
                            break;
                        case "mul":
                            destinationRegister.setValue(destinationRegister.getValue() * sourceRegister.getValue());
                            steps.add(new Instruction(step, opcode, destinationRegister, 4, operand2));
                            break;
                        case "div":
                            destinationRegister.setValue(destinationRegister.getValue() / sourceRegister.getValue());
                            steps.add(new Instruction(step, opcode, destinationRegister, 4, operand2));
                    }
                }
            }
            step++;
        }
        scanner.close();
        System.out.format("%-19s%-24s%s\n","Decoded Form", "Encoded Form", "Clock Cycles");
        for (Instruction s : steps) {
            System.out.println(s.toString());
        }
        System.out.println("--------------------------------------");
        System.out.println("Register States:");
        for (Instruction s : steps) {
            System.out.format("%s = %d   \n", s.getRegister().to3BitAddress(), s.getValue(), s.getRegister().to16BitValue());
        }
        System.out.println("----------------------------------------------");
        System.out.println("Values of registers after executed");
        for (int i = 0; i < registers.length; i++) {
            System.out.format("%s   %d  \n", registers[i].getAddress(), registers[i].getValue(), registers[i].to16BitValue());
        }
        System.out.println("-----------------------------------------------");
        int totalClockCycles = 0;
        for (Instruction s : steps) {
            totalClockCycles += s.getClockCycles();
        }
        double cpi = totalClockCycles / step;
        System.out.println("the program's CPI: " + cpi);
        System.out.println("-----------------------------------------------");
        int clockCyclesWithPipeline = steps.size() + 3;
        System.out.println("Pipelined execution of the program:");
        System.out.printf("%17s", "");
        for (int i = 1; i <= clockCyclesWithPipeline; i++) {
            System.out.printf("%5d", i);
        }
        for (Instruction s : steps) {
            if (s.getOperand().equals("")) {
                System.out.printf("%n%d. %-5s%-4s%-2d :", s.getStep() + 1, s.getOpcode(), s.getRegister().getAddress(), s.getValue());
            } else {
                System.out.printf("%n%d. %-5s%-4s%-2s :", s.getStep() + 1, s.getOpcode(), s.getRegister().getAddress(), s.getOperand());
            }
            for (int i = 0; i < s.getStep() + 1; i++) {
                System.out.printf("%5s", "");
            }
            System.out.printf("IF | ID | EX | WB");//Instruction Fetch,Instruction Decoding,Execution,WriteBack
        }
        System.out.println("\n\nPipelined ICP ex " + clockCyclesWithPipeline + " clock cycles to complete the program execution");
    }
}
