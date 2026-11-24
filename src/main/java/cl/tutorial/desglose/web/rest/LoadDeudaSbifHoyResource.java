package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.LoadDeudaSbifHoyService;
import cl.tutorial.desglose.service.dto.DatosConsultaSbifRequestDTO;
import cl.tutorial.desglose.service.dto.DatosSbifDTO;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.FORMATTER_DATE_SHORT;


/**
 * REST controller for managing ClienteBasico.
 */
@RestController
@RequestMapping("/api")
public class LoadDeudaSbifHoyResource {

    private final Logger log = LogManager.getLogger(LoadDeudaSbifHoyResource.class);

    private final LoadDeudaSbifHoyService loadDeudaSbifHoyService;

    public LoadDeudaSbifHoyResource(LoadDeudaSbifHoyService loadDeudaSbifHoyService) {
        this.loadDeudaSbifHoyService = loadDeudaSbifHoyService;
    }

    /**
     * GET  /load-deuda-sbif-hoy/:rut/:fecConsultaString : get the "rut" and get the "fecConsultaString" List DatosSbifDTO.
     *
     * @param rut and @param fecConsultaString
     * 		the rut of the List DatosSbifDTO to retrieve
     * 	    the fecConsultaString of the List DatosSbifDTO to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the datosSbifDTO, or with status 404 (Not Found)
     */
/*
    @GetMapping("/load-deuda-sbif-hoy/{rut}/{fecConsultaString}")
    @Timed
    public ResponseEntity<List<DatosSbifDTO>> getLoadDeudaSbifHoy(@PathVariable String rut, @PathVariable String fecConsultaString) {
        log.debug(String.format("REST request to getLoadDeudaSbifHoy : {rut: %s, fecConsultaString: %s}", rut, fecConsultaString));

        LocalDate fechaConsultaDate = LocalDate.parse(fecConsultaString, FORMATTER_DATE_SHORT);
        List<DatosSbifDTO> listDatosSbifDTO = loadDeudaSbifHoyService.getLoadDeudaSbifHoy_By_Rut_FecConsulta(rut, fechaConsultaDate);
        return new ResponseEntity<>(listDatosSbifDTO, HttpStatus.OK);
    }
*/

    /**
     * POST  /load-deuda-sbif-obtener : recovers all the DatosSbifDTO data.
     *
     * @param datos
     * 		objeto con los valores de la consulta
     *
     * @return the ResponseEntity with status 200 (OK) and with body the datosSbifDTO, or with status 404 (Not Found)
     */
    // todo: este servicio no se esta usando
    @PostMapping("/load-deuda-sbif-obtener")
    @Timed
    public ResponseEntity<List<DatosSbifDTO>> getLoadDeudaSbifHoy(@RequestBody DatosConsultaSbifRequestDTO datos
    ) {
        List<DatosSbifDTO> listDatosSbifDTO = new ArrayList<DatosSbifDTO>();
        String rut = datos.getRut();
        LocalDate fecConsulta = datos.getFecConsulta();
        List<LocalDate> arrPERIODOS = datos.getArrPERIODOS();
        List<Integer> activePeriodsIndexes = datos.getActivePeriodsIndexes();
        if (rut !=null && arrPERIODOS != null && fecConsulta != null) {
            log.info(String.format("REST request to getLoadDeudaSbifHoy : {rut: %s, fecConsulta: %s}", rut, fecConsulta.format(FORMATTER_DATE_SHORT)));
            for (int i = 0; i < (arrPERIODOS.size() - 1); i++) {
                try {
                    if (arrPERIODOS.get(i) != null) {
                        String fecha = FORMATTER_DATE_SHORT.format(arrPERIODOS.get(i));
                        log.debug(String.format("arrPERIODOS[%s]: %s", Integer.toString(i), fecha));
                    }
                } catch (DateTimeParseException e) {
                    log.error(String.format("Error al convertir fecha de arrPERIODOS[%s]"));
                }
            }
            listDatosSbifDTO = loadDeudaSbifHoyService.getLoadDeudaSbifHoy_By_Rut_FecConsulta(rut, fecConsulta, arrPERIODOS, activePeriodsIndexes);
            return new ResponseEntity<>(listDatosSbifDTO, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(listDatosSbifDTO, HttpStatus.BAD_REQUEST);
        }
    }

}
