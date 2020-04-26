import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {usersParticipation} from "./reducers";

export const postUserStatus = () => (approvedUser) => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/users', approvedUser)(usersParticipation.userStatus);
};