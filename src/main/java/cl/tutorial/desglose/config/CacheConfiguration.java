package cl.tutorial.desglose.config;

import java.time.Duration;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.Concepto;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteBasico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteJuridico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteNatural;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteSinRut;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.*;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUserGroupRole;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(cl.tutorial.desglose.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.Desglose.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.Periodo.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.ValPeriodoDesglose.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.ValPeriodoDesgImpr.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.EmpresaConsolidada.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.ConceptoImpresion.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.PeriodoDesgConsol.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.PeriodoDesgImpresion.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseImpresion.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseRevision.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseFirma.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseGrupo.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseReemplazo.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseMensaje.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseAuditoria.class.getName(), jcacheConfiguration);
            cm.createCache(ClienteSinRut.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseChequeoLog.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseChequeoInfo.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DesgloseRevisionLog.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.Cuadre.class.getName(), jcacheConfiguration);
            cm.createCache(Concepto.class.getName(), jcacheConfiguration);
            cm.createCache(ClienteBasico.class.getName(), jcacheConfiguration);
            cm.createCache(ClienteNatural.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.IdentificacionCliente.class.getName(), jcacheConfiguration);
            cm.createCache(ClienteJuridico.class.getName(), jcacheConfiguration);
            cm.createCache(TurbineRole.class.getName(), jcacheConfiguration);
            cm.createCache(TurbineGroup.class.getName(), jcacheConfiguration);
            cm.createCache(TurbinePermission.class.getName(), jcacheConfiguration);
            cm.createCache(TurbineUser.class.getName(), jcacheConfiguration);
            cm.createCache(TurbineRolePermission.class.getName(), jcacheConfiguration);
            cm.createCache(TurbineUserGroupRole.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.DescripcionCodigo.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.RelacionTabla.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.TablaValor.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.TablaServicioMail.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.TablaServMailDest.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.SfStfOperator.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.UniqueSessionControl.class.getName(), jcacheConfiguration);
            cm.createCache(cl.tutorial.desglose.domain.BlackListJwt.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
