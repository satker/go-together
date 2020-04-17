import {EVENTS_URL} from "../../../utils/constants";

export const getCashCategories = () => () => (fetch) => {
    fetch(EVENTS_URL + '/cashCategories');
};

export const getPayedThings = () => () => (fetch) => {
    fetch(EVENTS_URL + '/payedThings');
};