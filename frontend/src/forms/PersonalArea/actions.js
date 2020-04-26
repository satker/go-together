import {USER_SERVICE_URL} from "../utils/constants";
import {personalArea} from "./reducers";

const URL_USER = USER_SERVICE_URL + "/users/_id_";
const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/_mail_';

export const getUserInfo = (state) => () => (dispatch) => {
    dispatch(URL_USER.replace('_id_', state.userId))(personalArea.userInfo);
};

export const putUpdatedUser = (state) => (body) => dispatch => {
    dispatch(URL_USER.replace('_id_', state.userId), body)(personalArea.updatedUser);
};

export const getCheckMail = () => (value) => (dispatch) => {
    dispatch(URLtoCheck.replace("_mail_", value))(personalArea.checkMail);
};