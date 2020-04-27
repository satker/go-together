import {EVENT_SERVICE_URL, EVENTS_URL} from "../../utils/constants";
import {eventView} from "./reducers";

export const getEvent = () => (id) => (dispatch) => {
    dispatch(EVENTS_URL + "/" + id)(eventView.event);
};

export const getStatuses = () => (id) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + id + '/users/statuses')(eventView.statuses);
};

export const getUsers = () => (eventId) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + eventId + '/users')(eventView.users);
};
