import {LOGIN_URL, USER_SERVICE_URL} from "forms/utils/constants";
import {AUTH, CSRF_TOKEN, USER_ID} from "App/Context/constants";
import {POST} from "App/utils/api/constants";

import {LOGIN_HEADERS, LOGIN_ID} from "./constants";

export const setAuth = (userId, token) => (dispatch) => {
    localStorage.setItem(CSRF_TOKEN, token);
    localStorage.setItem(USER_ID, userId);
    dispatch({
        type: AUTH,
        value: {
            userId,
            csrfToken: token
        }
    })
};

export const postLogin = (username, password) => (dispatch) => {
    dispatch({
        type: LOGIN_HEADERS,
        url: LOGIN_URL,
        method: POST,
        data: {username, password},
        isToken: true
    });
};

export const getLoginId = (login) => (dispatch) => {
    dispatch({
        type: LOGIN_ID,
        url: USER_SERVICE_URL + '/users?login=' + login
    })
};