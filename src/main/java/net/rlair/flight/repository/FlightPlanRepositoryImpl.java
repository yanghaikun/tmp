package net.rlair.flight.repository;

import net.rlair.flight.support.log.Log;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Yang Haikun
 */
public class FlightPlanRepositoryImpl implements FlightPlanCustome {
    //获取当前线程的EntityManager实例
    @PersistenceContext
    private EntityManager em;


    @Override
    public void executeSQL(String sql) {
        Log.FLIGHT.debug("执行原生sql={}", sql);
        getSession().createSQLQuery(sql).executeUpdate();
        em.flush();
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    /**
     * 获取Session
     * @return
     */
    public Session getSession() {
        return (Session) em.getDelegate();
    }
}
