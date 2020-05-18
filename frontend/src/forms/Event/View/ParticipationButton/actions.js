import {EVENT_SERVICE_URL} from "forms/utils/constants";
import {DELETE, POST} from "App/utils/api/constants";

import {PARTICIPATION_BUTTON_FROM_LIST, PARTICIPATION_BUTTON_TO_LIST} from "./constants";

export const postMeToList = (setRefresh, meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_TO_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: POST,
        data: meObject
    });
};

export const deleteMeFromList = (setRefresh, meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_FROM_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: DELETE,
        data: meObject
    });
};
