import {EVENTS_URL} from "../../../utils/constants";
import {paidThings} from "./reducers";

export const getCashCategories = () => () => (dispatch) => {
    dispatch(EVENTS_URL + '/cashCategories')(paidThings.cashCategories);
};

export const getPayedThings = () => () => (dispatch) => {
    dispatch(EVENTS_URL + '/payedThings')(paidThings.payedThings);
};