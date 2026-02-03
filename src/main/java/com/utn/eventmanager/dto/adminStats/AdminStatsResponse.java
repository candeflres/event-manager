package com.utn.eventmanager.dto.adminStats;

public class AdminStatsResponse {
    private long totalEvents;
    private long pendingEvents;
    private long approvedEvents;
    private long cancelledEvents;
    private long completedEvents;

    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long clientUsers;
    private long employeeUsers;
    private long adminUsers;

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(long pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    public long getApprovedEvents() {
        return approvedEvents;
    }

    public void setApprovedEvents(long approvedEvents) {
        this.approvedEvents = approvedEvents;
    }

    public long getCancelledEvents() {
        return cancelledEvents;
    }

    public void setCancelledEvents(long cancelledEvents) {
        this.cancelledEvents = cancelledEvents;
    }

    public long getCompletedEvents() {
        return completedEvents;
    }

    public void setCompletedEvents(long completedEvents) {
        this.completedEvents = completedEvents;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public long getClientUsers() {
        return clientUsers;
    }

    public void setClientUsers(long clientUsers) {
        this.clientUsers = clientUsers;
    }

    public long getEmployeeUsers() {
        return employeeUsers;
    }

    public void setEmployeeUsers(long employeeUsers) {
        this.employeeUsers = employeeUsers;
    }

    public long getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(long adminUsers) {
        this.adminUsers = adminUsers;
    }
}
