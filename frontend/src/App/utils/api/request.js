import {CSRF_TOKEN, USER_ID, USER_SERVICE_URL} from "../../../forms/utils/constants";
import {set as setCookie} from 'js-cookie'
import {components} from "../../../forms/reducers";
import {findPath} from "../utils";
import {GET, POST, PUT} from "./constants";

export const fetchAndSet = async (url,
                                  setResult,
                                  method = GET,
                                  data = {},
                                  headers = {},
                                  type) => {
    let response;
    let [path, presentedRespObject] = findPath(type, null, components);
    const defaultRespObject = {...presentedRespObject};
    defaultRespObject.inProcess = true;
    setResult(defaultRespObject, path);
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
        defaultRespObject.response = JSON.parse(result);
        defaultRespObject.inProcess = false;
        setResult(defaultRespObject, path);
    } else {
        const result = await response.text();
        const error = JSON.parse(result).message;
        defaultRespObject.error = error;
        defaultRespObject.inProcess = false;
        setResult(defaultRespObject, path);
        alert(error);
    }
};

export const fetchAndSetToken = (token) =>
    (setToContext) =>
        ({type, url, method = GET, data = {}}) => {
            let headers = {
                'Accept': 'application/json',
                'content-type': 'application/json'
            };

            if (token) {
                headers['Authorization'] = token;
            }
            fetchAndSet(url, setToContext, method, data, headers, type);
        };

export const registerFetch = async (url, setScreen, data = {}, error, goToLogin) => {
    let headers = {
        'Accept': 'application/json',
        'content-type': 'application/json'
    };
    fetchAndSet(url, () => {
        setScreen("login");
        goToLogin();
    }, PUT, data, headers);
};

export const loginFetch = async (url, data = {}, state, setState) => {
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
                setState(['userId', 'fetchWithToken'], [resp.response.id, fetchAndSetToken(token)]);
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
