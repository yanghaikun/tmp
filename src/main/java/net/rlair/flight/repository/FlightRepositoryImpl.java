package net.rlair.flight.repository;

import net.rlair.flight.entity.Flight;
import net.rlair.flight.support.log.Log;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Kevin on 2017/8/27.
 */
public class FlightRepositoryImpl implements FlightCustome {
    //获取当前线程的EntityManager实例
    @PersistenceContext
    private EntityManager em;
    private int batchSize = 10000;

    @Override
    public void batchUpdate(List<Flight> flightList) {
        StringBuilder sb = new StringBuilder();
        int count = flightList.size();
        for (int i = 0; i < count; ++i) {
            if (i > 0 && i % batchSize == 0) {
                getSession().createQuery(sb.toString()).executeUpdate();
                em.flush();
                em.clear();
                sb = new StringBuilder();
            }
            sb.append(flightList.get(i).SQLUpdate());
        }

        getSession().createQuery(sb.toString()).executeUpdate();
        em.flush();
        em.clear();
    }

    @Override
    public void batchInsert(List<Flight> flightList) {
        int count = flightList.size();
        for (int i = 0; i < count; ++i) {
            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
            em.persist(flightList.get(i));
        }
    }

    /**
     * 获取Session
     * @return
     */
    public Session getSession() {
        return (Session) em.getDelegate();
    }
}
