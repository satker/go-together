import {MESSAGE_SERVICE_URL, USER_SERVICE_URL} from "../../../../utils/constants";
import {MESSAGES_CHATS, MESSAGES_USERS_INFO} from "../constants";
import {POST} from "../../../../../App/utils/api/constants";

export const getChats = () => (eventId) => (dispatch) => {
    dispatch(MESSAGE_SERVICE_URL + '/events/' + eventId + '/messages/')(MESSAGES_CHATS);
};

export const postUsersInfo = () => (notFoundUserIds) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/simple', POST, notFoundUserIds)(MESSAGES_USERS_INFO);
};