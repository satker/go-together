import {EVENTS_URL} from "../../../utils/constants";

export const getCashCategories = () => () => (fetch) => {
    fetch(EVENTS_URL + '/cashCategories', () => null);
};

export const getPayedThings = () => () => (fetch) => {
    fetch(EVENTS_URL + '/payedThings', () => null);
};