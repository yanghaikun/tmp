package net.rlair.flight.entity;

import net.rlair.flight.common.RecordStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Yang Haikun
 */
@Entity
@Table(name = "t_flight")
public class Flight implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 时间
     */
    @Column
    private Date date;

    @Column
    private int dayOfWeek;

    /**
     * 航班号
     */
    @Column
    private String flightNumber;

    /**
     * 机型
     */
    @Column
    private String airplane;

    /**
     * 起飞城市
     */
    @Column
    private String originCity;

    /**
     * 落地城市
     */
    @Column
    private String destinationCity;

    /**
     * 起飞时间
     */
    @Column
    private String departureTime;

    /**
     * 到达时间
     */
    @Column
    private String arrivalTime;

    /**
     * 来自于那条航线计划
     */
    @Column
    private long planId;

    /**
     *是否已经删除
     */
    @Column
    private boolean canceled;

    @Transient
    private RecordStatus status = RecordStatus.NONE;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    /**
     * 根据航线计划设置航班信息
     * @param fp
     */
    public void copyFormPlan(FlightPlan fp){
        this.setFlightNumber(fp.getFlightNumber());
        this.setAirplane(fp.getAirplane());
        this.setDepartureTime(fp.getDepartureTime());
        this.setArrivalTime(fp.getArrivalTime());
        this.setOriginCity(fp.getOriginCity());
        this.setDestinationCity(fp.getDestinationCity());
        this.setPlanId(fp.getId());
    }

    /**
     * 判断航班是否匹配航线计划
     * @param fp
     * @return
     */
    public boolean matchPlan(FlightPlan fp){
        //判断航班号
        if(!(this.getFlightNumber().equals(fp.getFlightNumber()))){
            return false;
        }
        //判断出发城市
        if(!(this.getOriginCity().equals(fp.getOriginCity()))){
            return false;
        }
        //判断到达城市
        if(!(this.getDestinationCity().equals(fp.getDestinationCity()))){
            return false;
        }
        //排期不满足
        if(!(fp.getScheduleList().contains(this.getDayOfWeek()))){
            return false;
        }

        //时间不满足
        if(!((this.getDate().getTime() >= fp.getStartDate().getTime()) && (this.getDate().getTime() <= fp.getEndDate().getTime()))){
            return false;
        }

        return true;
    }

    public String SQLUpdate(){
        StringBuilder sb = new StringBuilder()
                .append("UPDATE t_flight SET departure_time = '").append(this.departureTime).append("',")
                .append(" arrival_time = '").append(this.arrivalTime).append("',")
                .append(" airplane = '").append(this.airplane).append("',")
                .append(" canceled = ").append(this.canceled ? 1 : 0).append(" ")
                .append(" WHERE id = ").append(this.id).append(";");

        return sb.toString();
    }
}
