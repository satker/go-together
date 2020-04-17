import {CSRF_TOKEN, USER_ID, USER_SERVICE_URL} from "../../../forms/utils/constants";
import {set as setCookie} from 'js-cookie'

export const fetchAndSet = async (url, setResult, method = 'GET', data = {}, headers = {}) => {
    let resp;
    if (method === 'GET') {
        resp = await fetch(url, {
            method: method,
        });
    } else {
        resp = await fetch(url, {
            method: method,
            headers,
            body: JSON.stringify(data)
        });
    }
    if (resp.status === 200) {
        const result = await resp.text();
        try {
            setResult(JSON.parse(result));
        } catch (e) {
            console.log(e);
            setResult(null);
        }
    } else {
        const result = await resp.text();
        const errorMessage = JSON.parse(result).message;
        alert(errorMessage);
        setResult(null)
    }
};

export const fetchAndSetToken = (token) => (setToContext, methodAction) => async (url, data = {}) => {
    let headers = {
        'Accept': 'application/json',
        'content-type': 'application/json'
    };

    if (token) {
        headers['Authorization'] = token;
    }
    fetchAndSet(url, setToContext, methodAction, data, headers);
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
            setCookie(CSRF_TOKEN, token);
            setCookie(USER_ID, resp.id);
            setState(['userId', 'fetchWithToken'], [resp.id, fetchAndSetToken(token)]);
        };
        fetchAndSetToken(token)(setResult, 'GET')(USER_SERVICE_URL + '/users?login=' + data.username);
    } else {
        alert("Failed to login")
    }
};
