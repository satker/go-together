import {CSRF_TOKEN, USER_ID, USER_SERVICE_URL} from "../../../forms/utils/constants";
import {set as setCookie} from 'js-cookie'
import {findPath} from "../utils";
import {GET, POST} from "./constants";
import {context} from "../../Context";

export const fetchAndSet = async (url,
                                  setResult,
                                  method = GET,
                                  data = {},
                                  headers = {},
                                  type) => {
    let response;
    const pathData = findPath(type, null, context);
    if (!pathData.path || !pathData.data) {
        console.log(type, context);
        return;
    }
    pathData.data.inProcess = true;
    setResult(pathData.data, pathData.path);
    if (method === GET) {
        response = await fetch(url, {
            method: method,
        });
    } else {
        response = await fetch(url, {
            method: method,
            headers,
            body: JSON.stringify(data)
        });
    }
    if (response.status === 200) {
        const result = await response.text();
        pathData.data.response = JSON.parse(result);
        pathData.data.inProcess = false;
        setResult(pathData.data, pathData.path);
    } else {
        const result = await response.text();
        const error = JSON.parse(result).message;
        pathData.data.error = error;
        pathData.data.inProcess = false;
        setResult(pathData.data, pathData.path);
        alert(error);
    }
};

export const fetchAndSetToken = (token) =>
    (setToContext) =>
        ({type, url, method = GET, data = {}, value}) => {
            if (!url) {
                let pathData = findPath(type, null, context);
                setToContext(value, pathData.path);
            }
            let headers = {
                'Accept': 'application/json',
                'content-type': 'application/json'
            };

            if (token) {
                headers['Authorization'] = token;
            }
            fetchAndSet(url, setToContext, method, data, headers, type);
        };

export const loginFetch = async (url, data = {}, state, setUserId, setFetch) => {
    let token = null;
    await fetch(url, {
        method: POST,
        body: JSON.stringify(data),
    }).then(response => {
        response.headers
            .forEach(value => value.startsWith('Bearer ') ?
                token = value :
                null)
    }).catch(errorMessage => alert("Failed to login: " + errorMessage));
    if (token) {
        const setResult = (resp) => {
            if (!resp.inProcess) {
                setCookie(CSRF_TOKEN, token);
                setCookie(USER_ID, resp.response.id);
                setUserId(resp.response.id);
                setFetch(fetchAndSetToken(token));
            }
        };
        fetchAndSetToken(token)
        (setResult, GET, state, null, 'userId')
        ({
            type: null,
            url: USER_SERVICE_URL + '/users?login=' + data.username
        });
    } else {
        alert("Failed to login")
    }
};
