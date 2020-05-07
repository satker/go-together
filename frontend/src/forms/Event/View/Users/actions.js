import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {USER_PARTICIPATION_USERS_STATUS} from "./constants";

export const postUserStatus = () => (approvedUser) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', approvedUser)(USER_PARTICIPATION_USERS_STATUS);
};