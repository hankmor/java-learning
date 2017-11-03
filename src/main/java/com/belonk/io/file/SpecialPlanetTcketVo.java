package com.belonk.io.file;

import java.util.Date;

public class SpecialPlanetTcketVo {
    private String id;

    private String departure_city;

    private String arrival_city;

    private String airline_company;

    private String shipping_code;

    private Date travel_date_start;

    private Date travel_date_cutoff;

    private Date start_date_of_sale;

    private Date closing_date;

    private String face_value;

    private String fare_basis;

    private String earliest_advance_ticket;

    private String latest_ticket;

    private String time_limit;

    private String flight_availability;

    private String not_applicable_flight;

    private String applicable_time_range;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeparture_city() {
        return departure_city;
    }

    public void setDeparture_city(String departure_city) {
        this.departure_city = departure_city == null ? null : departure_city.trim();
    }

    public String getArrival_city() {
        return arrival_city;
    }

    public void setArrival_city(String arrival_city) {
        this.arrival_city = arrival_city == null ? null : arrival_city.trim();
    }

    public String getAirline_company() {
        return airline_company;
    }

    public void setAirline_company(String airline_company) {
        this.airline_company = airline_company == null ? null : airline_company.trim();
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code == null ? null : shipping_code.trim();
    }

    public Date getTravel_date_start() {
        return travel_date_start;
    }

    public void setTravel_date_start(Date travel_date_start) {
        this.travel_date_start = travel_date_start;
    }

    public Date getTravel_date_cutoff() {
        return travel_date_cutoff;
    }

    public void setTravel_date_cutoff(Date travel_date_cutoff) {
        this.travel_date_cutoff = travel_date_cutoff;
    }

    public Date getStart_date_of_sale() {
        return start_date_of_sale;
    }

    public void setStart_date_of_sale(Date start_date_of_sale) {
        this.start_date_of_sale = start_date_of_sale;
    }

    public Date getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(Date closing_date) {
        this.closing_date = closing_date;
    }

    public String getFace_value() {
        return face_value;
    }

    public void setFace_value(String face_value) {
        this.face_value = face_value == null ? null : face_value.trim();
    }

    public String getFare_basis() {
        return fare_basis;
    }

    public void setFare_basis(String fare_basis) {
        this.fare_basis = fare_basis == null ? null : fare_basis.trim();
    }

    public String getEarliest_advance_ticket() {
        return earliest_advance_ticket;
    }

    public void setEarliest_advance_ticket(String earliest_advance_ticket) {
        this.earliest_advance_ticket = earliest_advance_ticket == null ? null : earliest_advance_ticket.trim();
    }

    public String getLatest_ticket() {
        return latest_ticket;
    }

    public void setLatest_ticket(String latest_ticket) {
        this.latest_ticket = latest_ticket == null ? null : latest_ticket.trim();
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit == null ? null : time_limit.trim();
    }

    public String getFlight_availability() {
        return flight_availability;
    }

    public void setFlight_availability(String flight_availability) {
        this.flight_availability = flight_availability == null ? null : flight_availability.trim();
    }

    public String getNot_applicable_flight() {
        return not_applicable_flight;
    }

    public void setNot_applicable_flight(String not_applicable_flight) {
        this.not_applicable_flight = not_applicable_flight == null ? null : not_applicable_flight.trim();
    }

    public String getApplicable_time_range() {
        return applicable_time_range;
    }

    public void setApplicable_time_range(String applicable_time_range) {
        this.applicable_time_range = applicable_time_range == null ? null : applicable_time_range.trim();
    }
}