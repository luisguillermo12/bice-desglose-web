package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.service.ActividadEconomicaService;
import cl.tutorial.desglose.service.SectorService;
import cl.tutorial.desglose.service.SubSectorService;
import cl.tutorial.desglose.service.dto.ActividadEconomicaDTO;
import cl.tutorial.desglose.service.dto.SectorDTO;
import cl.tutorial.desglose.service.dto.SubSectorDTO;
import com.codahale.metrics.annotation.Timed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for custom sql queries.
 */
@RestController
@RequestMapping("/api")
public class CustomResource {

    private final Logger log = LogManager.getLogger(CustomResource.class);

    private final ActividadEconomicaService actividadEconomicaService;
    private final SectorService             sectorService;
    private final SubSectorService          subSectorService;

    public CustomResource(ActividadEconomicaService actividadEconomicaService, SectorService sectorService, SubSectorService subSectorService) {
        this.actividadEconomicaService = actividadEconomicaService;
        this.sectorService = sectorService;
        this.subSectorService = subSectorService;
    }

    /**
     * GET  /descripcion-codigos/get-all-economic-activities : get all the ActividadEconomica.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ActividadEconomica in body
     */
    @GetMapping("/custom-sql/get-all-economic-activities")
    @Timed
    public ResponseEntity<List<ActividadEconomicaDTO>> getAllEconomicActivities() {
        log.info("REST request to get all ActividadEconomicaDTO");
        List<ActividadEconomicaDTO> list = actividadEconomicaService.getAllEconomicActivities();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/get-all-sectors : get all the Sector.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of Sector in body
     */
    @GetMapping("/custom-sql/get-all-sectors")
    @Timed
    public ResponseEntity<List<SectorDTO>> getAllSectors() {
        log.info("REST request to get all SectorDTO");
        List<SectorDTO> list = sectorService.getAllSectors();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /descripcion-codigos/get-all-subsectors : get all the SubSector.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of SubSector in body
     */
    @GetMapping("/custom-sql/get-all-subsectors")
    @Timed
    public ResponseEntity<List<SubSectorDTO>> getAllSubSectors() {
        log.info("REST request to get all SubSectorDTO");
        List<SubSectorDTO> list = subSectorService.getAllSubSectors();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /custom-sql/get-subsectors-by-sector : get SUBSECTORS BY SECTOR.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of SubSector in body
     */
    @GetMapping("/custom-sql/get-subsectors-by-sector")
    @Timed
    public ResponseEntity<List<SubSectorDTO>> getSubSectorsBySector(@RequestParam(name = "codSector") String codSector) {
        log.info("REST request to get all SubSectorDTO");
        List<SubSectorDTO> list = subSectorService.getSubSectorsBySector(codSector);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /custom-sql/get-economic-activities-by-sector : get all the ActividadEconomica by sector code.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ActividadEconomica in body
     */
    @GetMapping("/custom-sql/get-economic-activities-by-sector")
    @Timed
    public ResponseEntity<List<ActividadEconomicaDTO>> getEconomicActivitiesBySector(@RequestParam(name = "codSector") String codSector) {
        log.info("REST request to get all ActividadEconomicaDTO");
        List<ActividadEconomicaDTO> list = actividadEconomicaService.getEconomicActivitiesBySector(codSector);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
