import {EVENT_SERVICE_URL, EVENTS_URL} from "../../utils/constants";
import {EVENT_VIEW_EVENT, EVENT_VIEW_STATUSES, EVENT_VIEW_USERS} from "./constants";

export const getEvent = () => (id) => (dispatch) => {
    dispatch(EVENTS_URL + "/" + id)(EVENT_VIEW_EVENT);
};

export const getStatuses = () => (id) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + id + '/users/statuses')(EVENT_VIEW_STATUSES);
};

export const getUsers = () => (eventId) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + eventId + '/users')(EVENT_VIEW_USERS);
};
