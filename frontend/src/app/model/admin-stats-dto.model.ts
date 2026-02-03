export interface AdminStatsDTO {
  totalUsers: number;
  activeUsers: number;
  inactiveUsers: number;

  adminUsers: number;
  employeeUsers: number;
  clientUsers: number;

  totalEvents: number;
  approvedEvents: number;
  pendingEvents: number;
  cancelledEvents: number;
  completedEvents: number;
}
