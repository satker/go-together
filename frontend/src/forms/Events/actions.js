import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "../utils/constants";
import {EVENTS_APARTMENT_TYPES, EVENTS_FIND_EVENTS, EVENTS_LANGUAGES, EVENTS_PARAMETERS} from "./constants";
import {POST} from "../../App/utils/api/constants";

export const postFindEvents = () => (filterObject) => (dispatch) => {
    dispatch({
        type: EVENTS_FIND_EVENTS,
        url: EVENTS_URL + '/find',
        method: POST,
        data: filterObject
    });
};

export const getParameters = () => () => (dispatch) => {
    dispatch({
        type: EVENTS_PARAMETERS,
        url: EVENT_SERVICE_URL + '/parameters'
    });
};

export const getLanguages = () => () => (dispatch) => {
    dispatch({
        type: EVENTS_LANGUAGES,
        url: USER_SERVICE_URL + '/languages'
    });
};

export const getApartmentTypes = () => () => (dispatch) => {
    dispatch({
        type: EVENTS_APARTMENT_TYPES,
        url: EVENT_SERVICE_URL + '/types'
    });
};

export const setPage = (state, setState) => (page) => () => {
    setState('page', page);
};

export const setArrivalDate = (state, setState) => (arrivalDate) => () => {
    setState('arrivalDate', arrivalDate);
};

export const setDepartureDate = (state, setState) => (departureDate) => () => {
    setState('departureDate', departureDate);
};