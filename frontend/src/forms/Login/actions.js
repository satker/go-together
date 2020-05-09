import {loginFetch} from "../../App/utils/api/request";
import {LOGIN_URL, USER_SERVICE_URL} from "../utils/constants";
import {CONTEXT_USER_ID, CSRF_TOKEN} from "../../App/Context/constants";
import {LOGIN_ID} from "./constants";

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
    loginFetch(LOGIN_URL, {username: login, password: password}, setCsrfToken, dispatch)
};

export const getLoginId = (login) => (dispatch) => {
    dispatch({
        type: LOGIN_ID,
        url: USER_SERVICE_URL + '/users?login=' + login
    })
};