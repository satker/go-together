import moment from "moment";

import {EVENT_SERVICE_URL} from "forms/utils/constants";
import {POST, PUT} from "App/utils/api/constants";
import {onChange} from "forms/utils/utils";

import {EDIT_EVENT_EVENT, EDIT_EVENT_NEW_EVENT, EDIT_EVENT_UPDATED_EVENT} from "./constants";

export const getEvent = (id) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_EVENT,
        url: EVENT_SERVICE_URL + '/events/' + id
    });
};

export const postUpdatedEvent = (saveObj) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_UPDATED_EVENT,
        url: EVENT_SERVICE_URL + '/events',
        method: POST,
        data: saveObj
    });
};

export const putNewEvent = (saveObj) => (dispatch) => {
    dispatch({
        type: EDIT_EVENT_NEW_EVENT,
        url: EVENT_SERVICE_URL + '/events',
        method: PUT,
        data: saveObj
    });
};

export const updateEvent = (path, value) => (dispatch, state) => {
    let updatedEvent = state.components.forms.event.eventEdit.event.response;
    if (!updatedEvent) {
        updatedEvent = {...value};
        updatedEvent.startDate = updatedEvent.startDate ? moment(updatedEvent.startDate) : null;
        updatedEvent.endDate = updatedEvent.endDate ? moment(updatedEvent.endDate) : null
    }
    if (path) {
        onChange(updatedEvent, result => updatedEvent = result)(path, value);
    }

    dispatch({
        type: EDIT_EVENT_EVENT,
        value: updatedEvent
    })
};