import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";
import {ARRIVAL_DATE, DEPARTURE_DATE, PAGE} from "App/Context/constants";

import {EVENTS_APARTMENT_TYPES, EVENTS_FIND_EVENTS, EVENTS_LANGUAGES, EVENTS_PARAMETERS} from "./constants";

export const postFindEvents = (filterObject) => (dispatch) => {
    console.log(filterObject.page.page);
    dispatch({
        type: EVENTS_FIND_EVENTS,
        url: EVENTS_URL + '/find',
        method: POST,
        data: filterObject
    });
};

export const getParameters = () => (dispatch) => {
    dispatch({
        type: EVENTS_PARAMETERS,
        url: EVENT_SERVICE_URL + '/parameters'
    });
};

export const getLanguages = () => (dispatch) => {
    dispatch({
        type: EVENTS_LANGUAGES,
        url: USER_SERVICE_URL + '/languages'
    });
};

export const getApartmentTypes = () => (dispatch) => {
    dispatch({
        type: EVENTS_APARTMENT_TYPES,
        url: EVENT_SERVICE_URL + '/types'
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