import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {getUsers} from "../actions";

export const postUserStatus = () => (eventId, approvedUser) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/users', () => {
        getUsers()(eventId)(fetch);
    }, null, approvedUser);
};