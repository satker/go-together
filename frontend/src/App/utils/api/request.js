import {CSRF_TOKEN, USER_ID, USER_SERVICE_URL} from "../../../forms/utils/constants";
import {set as setCookie} from 'js-cookie'
import {initState} from "../../Context";
import {createEmptyResponse} from "../utils";

export const fetchAndSet = async (url,
                                  setResult,
                                  method = 'GET',
                                  data = {},
                                  headers = {},
                                  defaultRespObject = createEmptyResponse(false, {})) => {
    let response;
    let resp = {...defaultRespObject};
    resp.inProcess = true;
    setResult(resp);
    if (method === 'GET') {
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
        resp.response = JSON.parse(result);
        resp.inProcess = false;
        setResult(resp);
    } else {
        const result = await response.text();
        resp.error = JSON.parse(result).message;
        resp.inProcess = false;
        alert(resp.error);
        setResult(resp)
    }
};

export const fetchAndSetToken = (token) =>
    (setToContext, methodAction, state, FORM_ID, path) =>
        async (url, data = {}) => {
            let headers = {
                'Accept': 'application/json',
                'content-type': 'application/json'
            };

            if (token) {
                headers['Authorization'] = token;
            }
            const defaultRespObject = state[FORM_ID]?.[path] || initState[FORM_ID]?.[path];
            fetchAndSet(url, setToContext, methodAction, data, headers, defaultRespObject);
};

export const registerFetch = async (url, setScreen, data = {}, error, goToLogin) => {
    let headers = {
        'Accept': 'application/json',
        'content-type': 'application/json'
    };
    fetchAndSet(url, () => {
        setScreen("login");
        goToLogin();
    }, 'PUT', data, headers);
};

export const loginFetch = async (url, data = {}, state, setState) => {
    let token = null;
    await fetch(url, {
        method: "POST",
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
        (setResult, 'GET', state, null, 'userId')
        (USER_SERVICE_URL + '/users?login=' + data.username);
    } else {
        alert("Failed to login")
    }
};
