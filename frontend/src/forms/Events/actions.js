import {EVENT_SERVICE_URL, FORM_DTO, USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";
import {PAGE} from "App/Context/constants";

import {EVENTS_FIND_EVENTS, EVENTS_INTERESTS, EVENTS_LANGUAGES, FILTER} from "./constants";
import {updateFormDto} from "../utils/utils";

export const postFindEvents = (filterObject) => (dispatch) => {
    dispatch({
        type: EVENTS_FIND_EVENTS,
        url: EVENT_SERVICE_URL + '/find',
        method: POST,
        data: filterObject
    });
};

export const getInterests = () => (dispatch) => {
    dispatch({
        type: EVENTS_INTERESTS,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "interest"
        }
    });
};

export const getLanguages = () => (dispatch) => {
    dispatch({
        type: EVENTS_LANGUAGES,
        method: POST,
        url: USER_SERVICE_URL + '/find',
        data: {
            mainIdField: "language"
        }
    });
};

export const setPage = (page) => (dispatch) => {
    dispatch({
        type: PAGE,
        value: page
    });
};

export const cleanFilter = () => (dispatch) => {
    dispatch({
        type: FILTER,
        value: FORM_DTO("event")
    });
};

export const setFilter = (filterOperation, values, searchField, havingCount) => (dispatch, state) => {
    const currentFilter = state.components.forms.events.filter.response;

    const updatedFilterObject = updateFormDto(currentFilter, filterOperation, values, searchField, havingCount);

    dispatch({
        type: FILTER,
        value: updatedFilterObject
    });
};