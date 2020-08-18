import {EVENT_SERVICE_URL, EVENTS_URL} from "forms/utils/constants";
import {PAID_THINGS, PAID_THINGS_CASH_CATEGORIES} from "./constants";
import {POST} from "App/utils/api/constants";

export const getCashCategories = () => (dispatch) => {
    dispatch({
        type: PAID_THINGS_CASH_CATEGORIES,
        url: EVENTS_URL + '/cashCategories'
    });
};

export const getPayedThings = () => (dispatch) => {
    dispatch({
        type: PAID_THINGS,
        method: POST,
        url: EVENT_SERVICE_URL + '/find',
        data: {
            mainIdField: "paidThing"
        }
    });
};