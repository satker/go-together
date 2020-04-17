import {USER_SERVICE_URL} from "../utils/constants";

const URL_USERS_LANGUAGES = USER_SERVICE_URL + "/languages";
const URL_USERS_INTERESTS = USER_SERVICE_URL + "/interests";

export const getAllLanguages = () => () => (fetch) => {
    fetch(URL_USERS_LANGUAGES);
};

export const getAllInterests = () => () => (fetch) => {
    fetch(URL_USERS_INTERESTS);
};

export const getCheckMail = () => (value) => (fetch) => {
    fetch(USER_SERVICE_URL + '/users/check/mail/' + value);
};

export const getCheckUserName = () => (value) => (fetch) => {
    fetch(USER_SERVICE_URL + '/users/check/login/' + value);
};