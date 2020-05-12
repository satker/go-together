import {EVENT_SERVICE_URL, EVENTS_URL} from "../../utils/constants";
import {EVENT_VIEW_EVENT, EVENT_VIEW_STATUSES, EVENT_VIEW_USERS} from "./constants";

export const getEvent = (id) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_EVENT,
        url: EVENTS_URL + "/" + id
    });
};

export const getStatuses = (id) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_STATUSES,
        url: EVENT_SERVICE_URL + '/events/' + id + '/users/statuses'
    });
};

export const getUsers = (eventId) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_USERS,
        url: EVENT_SERVICE_URL + '/events/' + eventId + '/users'
    });
};
