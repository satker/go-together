import {USER_SERVICE_URL} from "../utils/constants";
import {register} from './reducers';

const URL_USERS_LANGUAGES = USER_SERVICE_URL + "/languages";
const URL_USERS_INTERESTS = USER_SERVICE_URL + "/interests";

export const getAllLanguages = () => () => (dispatch) => {
    dispatch(URL_USERS_LANGUAGES)(register.allLanguages);
};

export const getAllInterests = () => () => (dispatch) => {
    dispatch(URL_USERS_INTERESTS)(register.allInterests);
};

export const getCheckMail = () => (value) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/check/mail/' + value)(register.checkMail);
};

export const getCheckUserName = () => (value) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/check/login/' + value)(register.checkUserName);
};