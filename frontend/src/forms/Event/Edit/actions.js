import {EVENT_SERVICE_URL} from "../../utils/constants";
import {EDIT_EVENT_EVENT, EDIT_EVENT_NEW_EVENT, EDIT_EVENT_UPDATED_EVENT} from "./constants";
import {POST, PUT} from "../../../App/utils/api/constants";

export const getEvent = () => (id) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_EVENT,
        url: EVENT_SERVICE_URL + '/events/' + id
    });
};

export const postUpdatedEvent = () => (saveObj) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_UPDATED_EVENT,
        url: EVENT_SERVICE_URL + '/events',
        method: POST,
        data: saveObj
    });
};

export const putNewEvent = () => (saveObj) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_NEW_EVENT,
        url: EVENT_SERVICE_URL + '/events',
        method: PUT,
        data: saveObj
    });
};