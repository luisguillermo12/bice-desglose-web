package cl.tutorial.desglose.service.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScriptEngineParserTest {

    private static final String EXPRESSION_1  = "2 == 2 && 2 > 1";
    private static final String EXPRESSION_2  = "2 == 2 && 2 > 4";
    private static final String EXPRESSION_3  = " (2 + 2) == 4 && (2 * 5) > 4";
    private static final String EXPRESSION_4  = "var dddd = 'sk1';";
    private static final String EXPRESSION_5  = "2 - 1 == 1";
    private static final String EXPRESSION_6  = "2 - 2 == 2";
    private static final String EXPRESSION_7  = "2 == 1 || 2 <= 4";
    private static final String EXPRESSION_8  = "4 >= 4";
    private static final String EXPRESSION_9  = "Math.abs(-2 + 1) == 1";
    private static final String EXPRESSION_10 = "MATH.ABS(-2 + 1) == 1";
    private static final String EXPRESSION_11 = "Math.abs(-2960.074--2960.074) != 0";
    private static final String EXPRESSION_12 = "Math.abs(-2960.074-(-2960.074)) != 0";
    private static final String EXPRESSION_13 = "Math.abs((-2960.074)-(-2960.074)) != 0";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void evaluateConceptValidation1() {
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_1));
    }

    @Test
    public void evaluateConceptValidation2() {
        Assert.assertEquals(false, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_2));
    }

    @Test
    public void evaluateConceptValidation3() {
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_3));
    }

    @Test
    public void evaluateConceptValidation4() {
        Assert.assertEquals(null, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_4));
    }

    @Test
    public void evaluateConceptValidation5() {
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_5));
    }

    @Test
    public void evaluateConceptValidation6() {
        Assert.assertEquals(false, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_6));
    }

    @Test
    public void evaluateConceptValidation7() {
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_7));
    }

    @Test
    public void evaluateConceptValidation8() {
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_8));
    }

    @Test
    public void evaluateConceptValidation9() {
        System.out.println(EXPRESSION_9);
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_9));
    }

    @Test
    public void evaluateConceptValidation10() {
        System.out.println(EXPRESSION_10);
        Assert.assertEquals(true, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_10));
    }

    @Test
    public void evaluateConceptValidation11() {
        System.out.println(EXPRESSION_11);
        Assert.assertEquals(false, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_11));
    }

    @Test
    public void evaluateConceptValidation12() {
        System.out.println(EXPRESSION_12);
        Assert.assertEquals(false, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_12));
    }

    @Test
    public void evaluateConceptValidation13() {
        System.out.println(EXPRESSION_13);
        Assert.assertEquals(false, ScriptEngineParser.evaluateConceptValidation(EXPRESSION_13));
    }
}
