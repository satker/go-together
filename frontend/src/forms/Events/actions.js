import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "../utils/constants";
import {events} from "./reducers";

export const postFindEvents = () => (filterObject) => (dispatch) => {
    dispatch(EVENTS_URL + '/find', filterObject)(events.findEvents);
};

export const getParameters = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/parameters')(events.parameters)
};

export const getLanguages = () => () => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/languages')(events.languages)
};

export const getApartmentTypes = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/types')(events.apartmentTypes)
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