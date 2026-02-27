# Changes to Workmate SDK - Android

## 1.0.3 - 27 Feb, 2026

### Added
- Added Task Check-In / Check-Out functionality. Previously, the system supported only Client Check-In / Check-Out.
- Introduced separate functions for Check-In and Check-Out actions.
Previously, a single function handled both operations.
- Introduced new task-related functionalities:
    - Get Task List
    - Get Assigned Tasks
- Added new client-related functionality:
    - Get Client List
- Improved error handling across the system. All errors are now returned in structured error objects, including:
    - Error Code
    - Error Message
- User-assigned plans linked to locations are now automatically arranged using an optimized route to improve efficiency.