import {loginFetch} from "../../App/utils/api/request";
import {LOGIN_URL} from "../utils/constants";

export const postLogin = (state, setState) => (login, password) => () => {
    loginFetch(LOGIN_URL, {username: login, password: password}, state, setState)
};