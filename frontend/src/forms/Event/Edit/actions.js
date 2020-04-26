import {EVENT_SERVICE_URL} from "../../utils/constants";
import {eventEdit} from "./reducer";

export const getEvent = () => (id) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/' + id)(eventEdit.event);
};

export const postUpdatedEvent = () => (saveObj) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events', saveObj)(eventEdit.updatedEvent);
};

export const putNewEvent = () => (saveObj) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events', saveObj)(eventEdit.newEvent);
};