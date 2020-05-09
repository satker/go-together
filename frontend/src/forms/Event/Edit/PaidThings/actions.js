import {EVENTS_URL} from "../../../utils/constants";
import {PAID_THINGS, PAID_THINGS_CASH_CATEGORIES} from "./constants";

export const getCashCategories = () => (dispatch) => {
    dispatch({
        type: PAID_THINGS_CASH_CATEGORIES,
        url: EVENTS_URL + '/cashCategories'
    });
};

export const getPayedThings = () => (dispatch) => {
    dispatch({
        type: PAID_THINGS,
        url: EVENTS_URL + '/payedThings'
    });
};