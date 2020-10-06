import {createEmptyResponse} from "App/utils/utils";

import {READ_NOTIFICATIONS, UNREAD_USER_NOTIFICATIONS, USER_NOTIFICATIONS} from "./constants";

export const notifications = {
    userNotifications: createEmptyResponse(USER_NOTIFICATIONS, []),
    unreadNotifications: createEmptyResponse(UNREAD_USER_NOTIFICATIONS, []),
    readNotifications: createEmptyResponse(READ_NOTIFICATIONS)
};