import {EVENT_SERVICE_URL} from "forms/utils/constants";
import {DELETE, PUT} from "App/utils/api/constants";

import {PARTICIPATION_BUTTON_FROM_LIST, PARTICIPATION_BUTTON_TO_LIST} from "./constants";

export const addMeToList = (meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_TO_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: PUT,
        data: meObject
    });
};

export const deleteMeFromList = (meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_FROM_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: DELETE,
        data: meObject
    });
};
