import {USER_SERVICE_URL} from "forms/utils/constants";
import {POST} from "App/utils/api/constants";

import {PERSONAL_AREA_CHECK_MAIL, PERSONAL_AREA_UPDATED_USER, PERSONAL_AREA_USER_INFO} from "./constants";

const URL_USER = USER_SERVICE_URL + "/users/";
const URL_USER_ID = URL_USER + "_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';

export const getUserInfo = () => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_USER_INFO,
        url: URL_USER_ID.replace('_id_', state.userId.value)
    });
};

export const updatedUser = (body) => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_UPDATED_USER,
        url: URL_USER,
        method: POST,
        data: {...body, id: state.userId.value}
    });
};

export const getCheckMail = (value) => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_CHECK_MAIL,
        url: URLtoCheck.replace("_mail_", value)
    });
};