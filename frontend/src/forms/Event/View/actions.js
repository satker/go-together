import {EVENT_SERVICE_URL, EVENTS_URL} from "../../utils/constants";

export const getEvent = () => (id) => (fetch) => {
    fetch(EVENTS_URL + "/" + id);
};

export const getStatuses = () => (id) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/' + id + '/users/statuses');
};

export const getUsers = () => (eventId) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/' + eventId + '/users');
};
