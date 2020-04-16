import {EVENT_SERVICE_URL} from "../../utils/constants";

export const getEvent = () => (id) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/' + id, () => null);
};

export const postUpdatedEvent = () => (saveObj) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events', () => null, null, saveObj);
};

export const putNewEvent = () => (saveObj) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events', () => null, null, saveObj);
};