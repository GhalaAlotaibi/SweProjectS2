package com.example.sweprojects2;
public class bookO {
    private int appointmentID;
    private int clientID;
    private int staffID;
    private int serviceID;

    private String servicename;
    private String date;
    private String time;
    private String staffName;

    // Constructor
    public bookO(int appointmentID, int clientID, int staffID, String date, String time, String staffName, String serviceName) {
        this.appointmentID = appointmentID;
        this.clientID = clientID;
        this.staffID = staffID;
        this.servicename = serviceName;
        this.date = date;
        this.time = time;
        this.staffName = staffName;
    }

    // Getters
    public int getAppointmentID() {
        return appointmentID;
    }


    public int getClientID() {
        return clientID;
    }


    public int getStaffID() {
        return staffID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStaffName() {
        return staffName;
    }
    public String getServiceName() {
        return servicename;
    }

    // toString Method
    @Override
    public String toString() {
        return "appointmentID=" + appointmentID +
                ", staffID=" + staffID +
                ", serviceID=" + serviceID +
                ", date=" + (date != null ? date : "null") +
                ", time=" + (time != null ? time : "null") +
                ", serviceName=" + (servicename != null ? servicename : "null") +
                '}';
    }
}
