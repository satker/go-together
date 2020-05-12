import {LOGIN_URL, USER_SERVICE_URL} from "../utils/constants";
import {CONTEXT_USER_ID, CSRF_TOKEN} from "../../App/Context/constants";
import {LOGIN_HEADERS, LOGIN_ID} from "./constants";
import {POST} from "../../App/utils/api/constants";

export const setUserId = (userId) => (dispatch) => {
    dispatch({
        type: CONTEXT_USER_ID,
        value: userId
    })
};

export const setCsrfToken = (token) => (dispatch) => {
    dispatch({
        type: CSRF_TOKEN,
        value: token
    })
};

export const postLogin = (login, password) => (dispatch) => {
    dispatch({
        type: LOGIN_HEADERS,
        url: LOGIN_URL,
        method: POST,
        data: {username: login, password: password},
        isToken: true
    });
};

export const getLoginId = (login) => (dispatch) => {
    dispatch({
        type: LOGIN_ID,
        url: USER_SERVICE_URL + '/users?login=' + login
    })
};