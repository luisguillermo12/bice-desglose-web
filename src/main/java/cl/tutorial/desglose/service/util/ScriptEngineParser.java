package cl.tutorial.desglose.service.util;

import cl.tutorial.desglose.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.List;

public class ScriptEngineParser {

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName(Constants.SCRIPT_ENGINE_BY_NAME);

    private static final Logger log = LoggerFactory.getLogger(ScriptEngineParser.class);

    private static void logAvailableEngines() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            System.out.println("ScriptEngineFactory Info");
            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();
            System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);
            List<String> engNames = factory.getNames();
            for (String name : engNames) {
                log.info("\tEngine Alias: {}\n", name);
            }
            log.info("\tLanguage: {} ({})\n", langName, langVersion);
        }
    }

    public static Boolean evaluateConceptValidation(String validationExpression) {
        final ScriptContext ctx = new SimpleScriptContext();
        ctx.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
        try {
            // ctx.setAttribute("paramName", param, ScriptContext.ENGINE_SCOPE);
            Object res = engine.eval(validationExpression, ctx);
            log.debug(res != null ? res.toString() : "NULL");
            return (Boolean) res;
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ScriptEngineParser.logAvailableEngines();
    }
}
