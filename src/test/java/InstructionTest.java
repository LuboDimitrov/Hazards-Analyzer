import mainPackage.Instruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstructionTest {

    ArrayList<String> operands;
    Instruction instruction;

    @BeforeEach
    void setup(){
        operands = new ArrayList<>(Arrays.asList("r1","r2","r3"));
        instruction = new Instruction("ADD", operands,1);
    }

    @Test
    public void OpcodeTest(){
        assertEquals("ADD",instruction.getOpcode());
    }

    @Test
    public void FirstOperandTest(){
        assertEquals("r1",instruction.getOperands().get(0));
    }

    @Test
    public void SecondOperandTest(){
        assertEquals("r2",instruction.getOperands().get(1));
    }

    @Test
    public void ThirdOperandTest(){
        assertEquals("r3",instruction.getOperands().get(2));
    }

    @Test
    public void positionTest(){
        assertEquals(1, instruction.getPosition());
    }
}
