import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {PARTICIPATION_BUTTON_FROM_LIST, PARTICIPATION_BUTTON_TO_LIST} from "./constants";
import {DELETE, POST} from "../../../../App/utils/api/constants";

export const postMeToList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', POST, meObject)(PARTICIPATION_BUTTON_TO_LIST);
};

export const deleteMeFromList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', DELETE, meObject)(PARTICIPATION_BUTTON_FROM_LIST);
};
