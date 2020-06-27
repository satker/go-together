import {createEmptyResponse} from "App/utils/utils";

import {READ_NOTIFICATIONS, USER_NOTIFICATIONS} from "./constants";

export const notifications = {
    userNotifications: createEmptyResponse(USER_NOTIFICATIONS, []),
    readNotifications: createEmptyResponse(READ_NOTIFICATIONS)
};