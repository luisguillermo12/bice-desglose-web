package cl.tutorial.desglose.config;

import cl.tutorial.desglose.domain.DescripcionCodigo;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.DescripcionCodigoService;
import cl.tutorial.desglose.service.dto.DescripcionCodigoDTO;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.xml.crypto.dsig.spec.XPathType;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class LogConfig {

    final DescripcionCodigoService descripcionCodigoService;

    public LogConfig(DescripcionCodigoService descripcionCodigoService){
        this.descripcionCodigoService = descripcionCodigoService;
    }
    @PostConstruct
    public void onStartUp() {

            List<DescripcionCodigoDTO> descripcionCodigoList =  this.descripcionCodigoService.getAllDesCodByCodTablasInAndCodGenericoLike("DGLCONFG","DGLLOGER_",0,"codGenerico,ASC");
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();
            final Appender appender = ctx.getConfiguration().getAppender("RollingFile");
            final LoggerConfig loggerConfig =  new LoggerConfig();
            loggerConfig.isAdditive();
            loggerConfig.setLevel(Level.DEBUG);
            ThresholdFilter filterDebug = ThresholdFilter.createFilter(Level.DEBUG, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
            ThresholdFilter filterError = ThresholdFilter.createFilter(Level.ERROR, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
            ThresholdFilter filterInfo = ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
            ThresholdFilter filterWarn = ThresholdFilter.createFilter(Level.WARN, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
            Filter compositeFilter1 = CompositeFilter.createFilters(new Filter[]{filterDebug, filterError,filterInfo,filterWarn} );;
            loggerConfig.addAppender(appender, Level.DEBUG, compositeFilter1);
            for (DescripcionCodigoDTO item:descripcionCodigoList) {
                if(item.getGlsGenericacorta().equals("0")) { // service
                    config.addLogger("cl.tutorial.desglose.service", loggerConfig);
                }
                if(item.getGlsGenericacorta().equals("1")) { // reposotory
                    config.addLogger("cl.tutorial.desglose.repository", loggerConfig);
                    config.addLogger("cl.tutorial.desglose.conexiones.bctdesglose.repository", loggerConfig);
                    config.addLogger("cl.tutorial.desglose.conexiones.cifdesglose.repository", loggerConfig);
                    config.addLogger("cl.tutorial.desglose.conexiones.sfdesglose.repository", loggerConfig);
                    config.addLogger("cl.tutorial.desglose.conexiones.sqldesglose.repository", loggerConfig);
                }
                if(item.getGlsGenericacorta().equals("2")) { // rest
                    config.addLogger("cl.tutorial.desglose.web.rest", loggerConfig);
                }
            }
            config.addLogger("cl.tutorial.desglose.aop.logging",loggerConfig);
            ctx.updateLoggers(config);
    }
}
