package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.ValPeriodoProyeccionDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.ValPeriodoDesgloseService;
import cl.tutorial.desglose.service.ValPeriodoProyeccionDesgloseService;
import cl.tutorial.desglose.service.dto.DesgloseDTO;
import cl.tutorial.desglose.service.dto.ValPeriodoDesgloseDTO;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 */
@RestController
@RequestMapping("/api")
public class ValPeriodoProyeccionDesgloseResource {
    private final Logger log = LogManager.getLogger(ValPeriodoProyeccionDesgloseResource.class);

    private static final String ENTITY_NAME = "valPeriodoDesglose";

    private final ValPeriodoProyeccionDesgloseService valPeriodoProyeccionDesgloseService;

    public ValPeriodoProyeccionDesgloseResource(ValPeriodoProyeccionDesgloseService valPeriodoProyeccionDesgloseService) {

        this.valPeriodoProyeccionDesgloseService = valPeriodoProyeccionDesgloseService;
    }
}
