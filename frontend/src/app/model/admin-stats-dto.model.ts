export interface AdminStatsdto {
  totalEvents: number;
  pendingEvents: number;
  approvedEvents: number;
  cancelledEvents: number;
  completedEvents: number;

  totalUsers: number;
  activeUsers: number;
  inactiveUsers: number;
  clientUsers: number;
  employeeUsers: number;
  adminUsers: number;
}
