import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "../utils/constants";

export const postFindEvents = () => (filterObject) => (fetch) => {
    fetch(EVENTS_URL + '/find', filterObject);
};

export const getParameters = () => () => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/parameters')
};

export const getLanguages = () => () => (fetch) => {
    fetch(USER_SERVICE_URL + '/languages')
};

export const getApartmentTypes = () => () => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/types')
};

export const setEventId = (state, setState) => (eventId) => () => {
    setState('eventId', eventId);
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