import {EVENT_SERVICE_URL} from "../../../utils/constants";

export const postMeToList = () => (setRefresh, meObject) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/users', meObject);
};

export const deleteMeFromList = () => (setRefresh, meObject) => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/users', meObject);
};
