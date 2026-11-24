package cl.tutorial.desglose.conexiones.bctdesglose.repository;

import cl.tutorial.desglose.conexiones.sqldesglose.repository.SQLRepository;
import cl.tutorial.desglose.service.util.Param;
import cl.tutorial.desglose.web.rest.errors.StorageProcedureExecutionException;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureOutputs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional("partnerbctdesgloseTransactionManager")
public class SQLBCTRepository {

    private final Logger log = LogManager.getLogger(SQLBCTRepository.class);

    @PersistenceContext(unitName = "partnerbctdesgloseEntityManagerFactory")
    private EntityManager partnerEntityManagerFactory;

    @Transactional(readOnly = true)
    public List<?> executeNativeSelectQueryByName(String queryName) {
        Session session = partnerEntityManagerFactory.unwrap(Session.class);
        Query query = session.getNamedQuery(queryName);
        return (List<?>) query.getResultList();
    }

    /**
     * Ejecuta una consulta SQL sin parámetros.
     *
     * @param sqlString
     * 		Consulta SQL.
     *
     * @return Lista de elementos resultantes de la ejecusión de la consulta¿ sql.
     *
     * @author Antonio Marrero Palomino
     */
    @Transactional(readOnly = true)
    public List<Object[]> executeNativeSelectQuery(String sqlString) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sqlString);
        return (List<Object[]>) q.getResultList();
    }

    /**
     * Ejecuta una consulta SQL con parámetros del modo ?1, ?2, ?X según la cantidad de elementos del parámetro "params"
     *
     * @param sqlString
     * 		Consulta SQL.
     * @param params
     * 		Parámetros de la consulta.
     *
     * @return Lista de elementos resultantes de la ejecusión de la consulta¿ sql.
     *
     * @author Antonio Marrero Palomino
     */
    @Transactional(readOnly = true)
    public List<Object[]> executeNativeSelectQuery(String sqlString, List<?> params) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sqlString);
        int index = 1;
        for (Object item : params) {
            q.setParameter(index, item);
            index++;
        }
        final List<Object[]> resultList = q.getResultList();
        return resultList;
    }

    public List<Object[]> execute(String sqlString, Map<String, Object> listaParametros) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sqlString);
        for (Map.Entry<String, Object> item : listaParametros.entrySet()) {
            q.setParameter(item.getKey(), item.getValue());
        }
        return (List<Object[]>) q.getResultList();
    }

    public void executeQuery(String sqlString, Map<String, Object> listaParametros) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sqlString);
        for (Map.Entry<String, Object> item : listaParametros.entrySet()) {
            q.setParameter(item.getKey(), item.getValue());
        }
        q.executeUpdate();
    }

    public BigDecimal countSql(String sql, Map<String, Object> listaParametros) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sql);
        for (Map.Entry<String, Object> item : listaParametros.entrySet()) {
            q.setParameter(item.getKey(), item.getValue());
        }
        return (BigDecimal) q.getSingleResult();

    }

    /**
     * Ejecuta una procedimiento almacenado en la base de datos que no devuelve un SELECT en su contenido, solo parámetros de salida.
     *
     * @param spName
     * 		Nombre del procedimiento almacenado.
     * @param inParams
     * 		parámetros de entrada.
     *
     * @return Lista de elementos seleccionados.
     *
     * @author Antonio Marrero Palomino
     */
    public Map<String, Object> executeNativeStoredProcedureOutputNoSelect(String spName, List<Param<?>> inParams) throws StorageProcedureExecutionException {
        try {
            StoredProcedureQuery sp = partnerEntityManagerFactory.createStoredProcedureQuery(spName);
            List<Param<?>> outParams = new ArrayList<>();
            if (inParams != null && !inParams.isEmpty()) {
                for (Param param : inParams) {
                    if (param.parameterMode.equals(ParameterMode.IN) || param.parameterMode.equals(ParameterMode.INOUT)) {
                        sp.registerStoredProcedureParameter(param.name, param.type, param.parameterMode).setParameter(param.name, param.value);
                        if (param.parameterMode.equals(ParameterMode.INOUT)) {
                            outParams.add(param);
                        }
                    } else if (param.parameterMode.equals(ParameterMode.OUT)) {
                        sp.registerStoredProcedureParameter(param.name, param.type, param.parameterMode);
                        outParams.add(param);
                    }
                }
            }
            sp.execute();
            Map<String, Object> resultList = new HashMap<>();
            for (Param param : outParams) {
                resultList.put(param.name, sp.getOutputParameterValue(param.name));
            }
            sp.unwrap(ProcedureOutputs.class).release(); // OJO -> Antonio -> Para resolver "ORA-01000: maximum open cursors exceeded"
            return resultList;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StorageProcedureExecutionException();
        }
    }

    /**
     * Ejecuta una procedimiento almacenado en la base de datos que devuelve un SELECT en su contenido y no parámetros de salida.
     *
     * @param spName
     * 		Nombre del procedimiento almacenado.
     * @param inParams
     * 		parámetros de entrada.
     *
     * @return Datos del select.
     *
     * @author Antonio Marrero Palomino
     */
    public List<Object[]> executeNativeStoredProcedureOutputSelect(String spName, List<Param<?>> inParams) {
        StoredProcedureQuery sp = partnerEntityManagerFactory.createStoredProcedureQuery(spName);
        Integer index = 1;
        List<Param<?>> outParams = new ArrayList<>();
        SQLRepository.execStoredProcedure(inParams, sp, index, outParams);
        sp.execute();

        final List<Object[]> resultList = sp.getResultList();
        return resultList;
    }

    /**
     * Ejecuta una consulta SQL con parámetros del modo parámetros por nombre de JPA 2
     *
     * @param sqlString
     * 		Consulta SQL.
     * @param params
     * 		Parámetros de la consulta.
     *
     * @return Lista de elementos resultantes de la ejecusión de la consulta sql.
     *
     * @author Antonio Marrero Palomino
     */
    @Transactional(readOnly = true)
    public List<Object[]> executeNativeSelectQuery(String sqlString, Map<String, Object> params) {
        Query q = partnerEntityManagerFactory.createNativeQuery(sqlString);
        for (Map.Entry<String, Object> item : params.entrySet()) {
            q.setParameter(item.getKey(), item.getValue());
        }
        return (List<Object[]>) q.getResultList();
    }
}
