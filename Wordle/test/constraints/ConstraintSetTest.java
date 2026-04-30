package constraints;

//import org.junit.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintSetTest {

    @Test
    void constraintSetCanBeConstructed() {
        ConstraintSet cs = new ConstraintSet();
        assertNotNull(cs);
    }

    @Test
    void allowsAlwaysReturnsTrueInitially() {
        ConstraintSet cs = new ConstraintSet();
        assertTrue(cs.allows(new Object()));
    }

    @Test
    void updatedByReturnsSameInstanceForNow() {
        ConstraintSet cs = new ConstraintSet();
        assertSame(cs, cs.updatedBy(new Object(), new Object()));
    }
}
