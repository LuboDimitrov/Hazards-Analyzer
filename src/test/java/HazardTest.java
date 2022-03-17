import mainPackage.Hazard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static mainPackage.HazardType.RAW;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazardTest {

    Hazard h1;

    @BeforeEach
    public void setUp() {
        h1 = new Hazard(RAW);
    }
    @Test
    public void testRAW(){
        assertEquals(RAW,h1.getHazardtype());
    }
}
