import {EVENTS_URL} from "../../../utils/constants";
import {PAID_THINGS, PAID_THINGS_CASH_CATEGORIES} from "./constants";

export const getCashCategories = () => () => (dispatch) => {
    dispatch(EVENTS_URL + '/cashCategories')(PAID_THINGS_CASH_CATEGORIES);
};

export const getPayedThings = () => () => (dispatch) => {
    dispatch(EVENTS_URL + '/payedThings')(PAID_THINGS);
};