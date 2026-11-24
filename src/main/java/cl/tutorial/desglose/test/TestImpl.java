package cl.tutorial.desglose.test;

public class TestImpl extends TestAbs implements Test {

    @Override
    public String getNameValue() {
        return nombre;
    }

    @Override
    public String getNameChi() {
        return "getNameChi";
    }

    @Override
    public String getNameCha() {
        return "getNameCha Padre";
    }
}
