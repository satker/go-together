import {USER_SERVICE_URL} from "../utils/constants";
import {PERSONAL_AREA_CHECK_MAIL, PERSONAL_AREA_UPDATED_USER, PERSONAL_AREA_USER_INFO} from "./constants";
import {PUT} from "../../App/utils/api/constants";

const URL_USER = USER_SERVICE_URL + "/users/_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';

export const getUserInfo = () => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_USER_INFO,
        url: URL_USER.replace('_id_', state.userId.value)
    });
};

export const putUpdatedUser = (body) => (dispatch, state) => {
    dispatch({
        type: PERSONAL_AREA_UPDATED_USER,
        url: URL_USER.replace('_id_', state.userId.value),
        method: PUT,
        data: body
    });
};

export const getCheckMail = (value) => (dispatch) => {
    dispatch({
        type: PERSONAL_AREA_CHECK_MAIL,
        url: URLtoCheck.replace("_mail_", value)
    });
};