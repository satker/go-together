import {USER_SERVICE_URL} from "../utils/constants";

const URL_USER = USER_SERVICE_URL + "/users/_id_";
const URLtoCheck = USER_SERVICE_URL + '/mail/check/_mail_';

export const getUserInfo = (state) => () => (fetch) => {
    fetch(URL_USER.replace('_id_', state.userId))
};

export const putUpdatedUser = (state) => (body) => fetch => {
    fetch(URL_USER.replace('_id_', state.userId), body);
};

export const getCheckMail = () => (value) => (fetch) => {
    fetch(URLtoCheck.replace("_mail_", value));
};