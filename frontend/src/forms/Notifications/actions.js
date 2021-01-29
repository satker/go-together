import {NOTIFICATION_PAGE, READ_NOTIFICATIONS, UNREAD_USER_NOTIFICATIONS, USER_NOTIFICATIONS} from "./constants";
import {NOTIFICATION_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

export const getUserNotifications = (userId, page = 0) => (dispatch) => {
    dispatch({
        type: USER_NOTIFICATIONS,
        method: POST,
        url: NOTIFICATION_SERVICE_URL + '/find',
        data: {
            mainIdField: "notificationReceiverMessages",
            filters: {
                "receiver.id": {
                    values: [{
                        id: {
                            filterType: 'EQUAL',
                            value: userId
                        }
                    }]
                }
            },
            page: {...NOTIFICATION_PAGE, page}
        }
    });
};

export const getUnreadUserNotifications = (userId) => (dispatch) => {
    dispatch({
        type: UNREAD_USER_NOTIFICATIONS,
        method: POST,
        url: NOTIFICATION_SERVICE_URL + '/find',
        data: {
            mainIdField: "notificationReceiverMessages",
            filters: {
                "receiver.id": {
                    values: [{
                        id: {
                            filterType: 'EQUAL',
                            value: userId
                        }
                    }]
                },
                isRead: {
                    values: [{
                        isRead: {
                            filterType: 'EQUAL',
                            value: false
                        }
                    }]
                }
            }
        }
    });
};

export const readNotifications = (userId) => (dispatch) => {
    dispatch({
        type: READ_NOTIFICATIONS,
        url: NOTIFICATION_SERVICE_URL + '/notifications/receivers/' + userId,
        method: POST
    });
};