import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {PARTICIPATION_BUTTON_FROM_LIST, PARTICIPATION_BUTTON_TO_LIST} from "./constants";
import {DELETE, POST} from "../../../../App/utils/api/constants";

export const postMeToList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_TO_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: POST,
        data: meObject
    });
};

export const deleteMeFromList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch({
        type: PARTICIPATION_BUTTON_FROM_LIST,
        url: EVENT_SERVICE_URL + '/events/users',
        method: DELETE,
        data: meObject
    });
};
