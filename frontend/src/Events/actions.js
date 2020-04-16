import {EVENT_SERVICE_URL, EVENTS_URL, USER_SERVICE_URL} from "../utils/constants";

export const postFindEvents = () => (filterObject) => (fetch) => {
    fetch(EVENTS_URL + '/find', () => null, 'POST', filterObject);
};

export const deleteEvent = () => (id, setEvents, events) => (fetch) => {
    fetch(EVENTS_URL + '/delete/' + id, () =>
        setEvents(events.events.filter(apartment => apartment.id !== id)), 'DELETE')
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

export const getParameters = () => (setParameters) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/parameters', setParameters)
};

export const getLanguages = () => (setLanguages) => (fetch) => {
    fetch(USER_SERVICE_URL + '/languages', setLanguages)
};

export const getApartmentTypes = () => (setApartmentTypes) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/types', setApartmentTypes)
};