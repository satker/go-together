import {READ_NOTIFICATIONS, USER_NOTIFICATIONS} from "./constants";
import {NOTIFICATION_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

export const getUserNotifications = (userId) => (dispatch) => {
    dispatch({
        type: USER_NOTIFICATIONS,
        url: NOTIFICATION_SERVICE_URL + '/notifications/receivers/' + userId
    });
};

export const readNotifications = (userId) => (dispatch) => {
    dispatch({
        type: READ_NOTIFICATIONS,
        url: NOTIFICATION_SERVICE_URL + '/notifications/receivers/' + userId,
        method: POST
    });
};