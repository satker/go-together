import {MESSAGE_SERVICE_URL, USER_SERVICE_URL} from "../../../../utils/constants";

export const getMessages = () => (eventId) => (fetch) => {
    fetch(MESSAGE_SERVICE_URL + '/events/' + eventId + '/messages/');
};

export const postUsersInfo = () => (notFoundUserIds) => (fetch) => {
    fetch(USER_SERVICE_URL + '/users/simple', notFoundUserIds);
};