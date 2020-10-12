export const FORM_ID = 'Notifications';

export const USER_NOTIFICATIONS = 'USER_NOTIFICATIONS';
export const UNREAD_USER_NOTIFICATIONS = 'UNREAD_USER_NOTIFICATIONS';
export const READ_NOTIFICATIONS = 'READ_NOTIFICATIONS';

export const NOTIFICATION_PAGE = {
    page: 0,
    size: 20,
    totalSize: 0,
    sort: [{
        field: 'message.date',
        direction: 'DESC'
    }]
}