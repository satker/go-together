import {EVENT_SERVICE_URL} from "../../utils/constants";
import {EDIT_EVENT_EVENT, EDIT_EVENT_NEW_EVENT, EDIT_EVENT_UPDATED_EVENT} from "./constants";

export const getEvent = () => (id) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + id)(EDIT_EVENT_EVENT);
};

export const postUpdatedEvent = () => (saveObj) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events', saveObj)(EDIT_EVENT_UPDATED_EVENT);
};

export const putNewEvent = () => (saveObj) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events', saveObj)(EDIT_EVENT_NEW_EVENT);
};