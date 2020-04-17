import {EVENT_SERVICE_URL} from "../../../utils/constants";

export const postUserStatus = () => (approvedUser) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/users', approvedUser);
};