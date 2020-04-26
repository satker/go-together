import {MESSAGE_SERVICE_URL, USER_SERVICE_URL} from "../../../../utils/constants";
import {messages} from "../reducers";

export const getMessages = () => (eventId) => (dispatch) => {
    dispatch(MESSAGE_SERVICE_URL + '/events/' + eventId + '/messages/')(messages.messages);
};

export const postUsersInfo = () => (notFoundUserIds) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/simple', notFoundUserIds)(messages.usersInfo);
};