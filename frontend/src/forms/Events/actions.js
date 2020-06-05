import {EVENT_SERVICE_URL, USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";
import {ARRIVAL_DATE, DEPARTURE_DATE, PAGE} from "App/Context/constants";

import {EVENTS_FIND_EVENTS, EVENTS_INTERESTS, EVENTS_LANGUAGES} from "./constants";

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
        url: USER_SERVICE_URL + '/interests'
    });
};

export const getLanguages = () => (dispatch) => {
    dispatch({
        type: EVENTS_LANGUAGES,
        url: USER_SERVICE_URL + '/languages'
    });
};

export const setPage = (page) => (dispatch) => {
    dispatch({
        type: PAGE,
        value: page
    });
};

export const setArrivalDate = (arrivalDate) => (dispatch) => {
    dispatch({
        type: ARRIVAL_DATE,
        value: arrivalDate
    });
};

export const setDepartureDate = (departureDate) => (dispatch) => {
    dispatch({
        type: DEPARTURE_DATE,
        value: departureDate
    });
};