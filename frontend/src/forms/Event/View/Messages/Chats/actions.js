import {MESSAGE_SERVICE_URL, USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {MESSAGES_CHATS, MESSAGES_USERS_INFO} from "../constants";

export const getChats = (eventId) => (dispatch) => {
    dispatch({
        type: MESSAGES_CHATS,
        url: MESSAGE_SERVICE_URL + '/events/' + eventId + '/messages/'
    });
};

export const postUsersInfo = (notFoundUserIds) => (dispatch) => {
    dispatch({
        type: MESSAGES_USERS_INFO,
        url: USER_SERVICE_URL + '/users/simple',
        method: POST,
        data: notFoundUserIds
    });
};