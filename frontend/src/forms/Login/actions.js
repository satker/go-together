import {loginFetch} from "../../App/utils/api/request";
import {LOGIN_URL} from "../utils/constants";
import {CONTEXT_USER_ID, FETCH} from "../../App/Context/constants";

export const setUserId = (userId) => (dispatch) => {
    dispatch({
        type: CONTEXT_USER_ID,
        value: userId
    })
};

export const setFetch = (fetch) => (dispatch) => {
    dispatch({
        type: FETCH,
        value: fetch
    })
};

export const postLogin = (login, password) => (dispatch, state) => {
    loginFetch(LOGIN_URL, {username: login, password: password}, state, setUserId, setFetch)
};