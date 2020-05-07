import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {USER_PARTICIPATION_USERS_STATUS} from "./constants";
import {POST} from "../../../../App/utils/api/constants";

export const postUserStatus = () => (approvedUser) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', POST, approvedUser)(USER_PARTICIPATION_USERS_STATUS);
};