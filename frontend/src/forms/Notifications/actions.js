import {NOTIFICATION_PAGE, READ_NOTIFICATIONS, UNREAD_USER_NOTIFICATIONS, USER_NOTIFICATIONS} from "./constants";
import {NOTIFICATION_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

export const getUserNotifications = (userId) => (dispatch) => {
    dispatch({
        type: USER_NOTIFICATIONS,
        method: POST,
        url: NOTIFICATION_SERVICE_URL + '/find',
        data: {
            mainIdField: "notificationReceiverMessage",
            filters: {
                "receiver.id": {
                    filterType: 'EQUAL',
                    values: [{
                        id: userId
                    }]
                }
            },
            page: {...NOTIFICATION_PAGE}
        }
    });
};

export const getUnreadUserNotifications = (userId) => (dispatch) => {
    dispatch({
        type: UNREAD_USER_NOTIFICATIONS,
        method: POST,
        url: NOTIFICATION_SERVICE_URL + '/find',
        data: {
            mainIdField: "notificationReceiverMessage",
            filters: {
                "receiver.id": {
                    filterType: 'EQUAL',
                    values: [{
                        id: userId
                    }]
                },
                isRead: {
                    filterType: 'EQUAL',
                    values: [{
                        isRead: false
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