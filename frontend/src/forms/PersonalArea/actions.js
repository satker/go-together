import {USER_SERVICE_URL} from "../utils/constants";
import {PERSONAL_AREA_CHECK_MAIL, PERSONAL_AREA_UPDATED_USER, PERSONAL_AREA_USER_INFO} from "./constants";
import {PUT} from "../../App/utils/api/constants";

const URL_USER = USER_SERVICE_URL + "/users/_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';

export const getUserInfo = (state) => () => (dispatch) => {
    dispatch(URL_USER.replace('_id_', state.userId))(PERSONAL_AREA_USER_INFO);
};

export const putUpdatedUser = (state) => (body) => dispatch => {
    dispatch(URL_USER.replace('_id_', state.userId), PUT, body)(PERSONAL_AREA_UPDATED_USER);
};

export const getCheckMail = () => (value) => (dispatch) => {
    dispatch(URLtoCheck.replace("_mail_", value))(PERSONAL_AREA_CHECK_MAIL);
};