public class Instruction {
    private int step;
    private String opcode;
    private Register register;
    private int clockCycles;
    private String operand;
    private int value;

    public Instruction(int step, String opcode, Register register, int clockCycles) {
        this(step, opcode, register, clockCycles, "");
    }

    public Instruction(int step, String opcode, Register register, int clockCycles, String operand) {
        this.step = step;
        this.opcode = opcode;
        this.register = register;
        this.clockCycles = clockCycles;
        this.value = register.getValue();
        this.operand = operand;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public int getClockCycles() {
        return clockCycles;
    }

    public void setClockCycles(int clockCycles) {
        this.clockCycles = clockCycles;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String fiveBitOpcode() {
        switch (opcode) {
            case "mov":
                return "00001";
            case "add":
                return "00010";
            case "mul":
                return "00100";
            case "sub":
                return "00011";
            case "div":
                return "00101";
            default:
                return "end 0 0";
        }
    }

    public String to16BitValue() {
        String binaryValue = Integer.toBinaryString(value);
        if (value >= 0) {
            return String.format("%16s", binaryValue).replace(' ', '0');
        } else {
            return binaryValue.substring(Math.max(0, binaryValue.length() - 16));
        }
    }

    @Override
    public String toString() {
        String operandValue = operand.equals("") ? String.valueOf(value) : operand;
        return String.format("[%d] %-5s%-4s %-5s%-22s%d",
                step, opcode, register.getAddress(), operand,
                to16BitValue(), clockCycles);
    }
}

