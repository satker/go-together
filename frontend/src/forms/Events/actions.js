import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "../utils/constants";
import {EVENTS_APARTMENT_TYPES, EVENTS_FIND_EVENTS, EVENTS_LANGUAGES, EVENTS_PARAMETERS} from "./constants";
import {POST} from "../../App/utils/api/constants";

export const postFindEvents = () => (filterObject) => (dispatch) => {
    dispatch(EVENTS_URL + '/find', POST, filterObject)(EVENTS_FIND_EVENTS);
};

export const getParameters = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/parameters')(EVENTS_PARAMETERS)
};

export const getLanguages = () => () => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/languages')(EVENTS_LANGUAGES)
};

export const getApartmentTypes = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/types')(EVENTS_APARTMENT_TYPES)
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