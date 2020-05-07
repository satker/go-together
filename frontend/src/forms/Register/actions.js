import {USER_SERVICE_URL} from "../utils/constants";
import {
    REGISTER_ALL_INTERESTS,
    REGISTER_ALL_LANGUAGES,
    REGISTER_CHECK_MAIL,
    REGISTER_CHECK_USERNAME
} from "./constants";

const URL_USERS_LANGUAGES = USER_SERVICE_URL + "/languages";
const URL_USERS_INTERESTS = USER_SERVICE_URL + "/interests";

export const getAllLanguages = () => () => (dispatch) => {
    dispatch(URL_USERS_LANGUAGES)(REGISTER_ALL_LANGUAGES);
};

export const getAllInterests = () => () => (dispatch) => {
    dispatch(URL_USERS_INTERESTS)(REGISTER_ALL_INTERESTS);
};

export const getCheckMail = () => (value) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/check/mail/' + value)(REGISTER_CHECK_MAIL);
};

export const getCheckUserName = () => (value) => (dispatch) => {
    dispatch(USER_SERVICE_URL + '/users/check/login/' + value)(REGISTER_CHECK_USERNAME);
};