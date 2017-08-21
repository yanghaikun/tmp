package net.rlair.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author Yang Haikun
 */
@Entity
@Table(name = "t_flight_plan")
public class FlightPlan implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String flightNumber;

    @Column
    private String airplane;

    @Column
    private String originCity;

    @Column
    private String destinationCity;

    @Column
    private String departureTime;

    @Column
    private String arrivalTime;

    @Column
    private Date beginTime;

    @Column
    private Date endTime;

}
