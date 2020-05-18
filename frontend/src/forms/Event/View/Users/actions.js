import {EVENT_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {USER_PARTICIPATION_USERS_STATUS} from "./constants";

export const postUserStatus = (approvedUser) => (dispatch) => {
    dispatch({
        type: USER_PARTICIPATION_USERS_STATUS,
        url: EVENT_SERVICE_URL + '/events/users',
        method: POST,
        data: approvedUser
    });
};