import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {participationButton} from "./reducers";

export const postMeToList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', meObject)(participationButton.meToList);
};

export const deleteMeFromList = () => (setRefresh, meObject) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', meObject)(participationButton.meFromList);
};
