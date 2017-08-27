package net.rlair.flight.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.rlair.flight.common.RecordStatus;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

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

    //班期
    @Column
    private String schedule;

    //航班号
    @Column
    private String flightNumber;

    //运力
    @Column
    private String airplane;

    //起飞站
    @Column
    private String originCity;

    //落地站
    @Column
    private String destinationCity;

    //起飞时间
    @Column
    private String departureTime;

    //到达时间
    @Column
    private String arrivalTime;

    //计划开始时间
    @Column
    private Date startDate;

    //计划结束时间
    @Column
    private Date endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    @Column
    private String description;

    @Transient
    private boolean canceled = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirplane() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        this.airplane = airplane;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public List<Integer> getScheduleList() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = null;
        try {
            list = objectMapper.readValue(this.schedule, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public FlightPlan copy(){
        FlightPlan fp = new FlightPlan();
        fp.setAirplane(this.airplane);
        fp.setFlightNumber(this.flightNumber);
        fp.setOriginCity(this.originCity);
        fp.setDestinationCity(this.destinationCity);
        fp.setDepartureTime(this.departureTime);
        fp.setArrivalTime(this.arrivalTime);
        fp.setStartDate(this.startDate);
        fp.setEndDate(this.endDate);
        fp.setSchedule(this.schedule);

        return fp;
    }

    public String SQLWhere(){
        String scheduleIn = "(";
        List<Integer> sList = this.getScheduleList();
        for (int i = 0; i < sList.size(); i++){
            scheduleIn += sList.get(i);
            if(i < sList.size() - 1){
                scheduleIn += ",";
            }
        }
        scheduleIn += ")";

        StringBuilder sb = new StringBuilder()
        .append(" WHERE flight_number = '").append(this.flightNumber).append("'")
        .append(" AND origin_city = '").append(this.originCity).append("'")
        .append(" AND destination_city = '").append(this.destinationCity).append("'")
        .append(" AND date >= '").append(this.startDate).append("'")
        .append(" AND date <= '").append(this.endDate).append("'")
        .append(" AND day_of_week IN ").append(scheduleIn);

        return sb.toString();
    }

    public String SQLUpdate(){
        StringBuilder sb = new StringBuilder()
        .append("UPDATE t_flight SET departure_time = '").append(this.departureTime).append("',")
                .append(" arrival_time = '").append(this.arrivalTime).append("',")
                .append(" airplane = '").append(this.airplane).append("',")
                .append(" plan_id = ").append(this.id).append(",")
                .append(" canceled = ").append(this.canceled ? 1 : 0).append(" ")
                .append(SQLWhere());

        return sb.toString();
    }

    public String SQLDelete(){
        StringBuilder sb = new StringBuilder()
                .append("DELETE FROM t_flight ")
                .append(SQLWhere());

        return sb.toString();
    }
}
