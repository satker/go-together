import {CSRF_TOKEN, USER_ID, USER_SERVICE_URL} from "../../../forms/utils/constants";
import {set as setCookie} from 'js-cookie'

export const fetchAndSet = async (url, setResult, method = 'GET', data = {}, headers = {},
                                  setToContext = () => null) => {
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
            setToContext(JSON.parse(result))
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

export const fetchAndSetToken = (token) => (setToContext, methodAction) => async (url, setResult, method = 'GET', data = {}) => {
    let headers = {
        'Accept': 'application/json',
        'content-type': 'application/json'
    };

    if (token) {
        headers['Authorization'] = token;
    }
    fetchAndSet(url, setResult, methodAction || method, data, headers, setToContext);
};

export const registerFetch = async (url, setScreen, data = {}, error, goToLogin) => {
    let resp = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    if (resp.status === 200 || resp.status === 201) {
        setScreen("login");
        goToLogin();
    } else {
        error();
    }
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
        fetchAndSetToken(token)(USER_SERVICE_URL + '/users?login=' + data.username, (resp) => {
            setCookie(CSRF_TOKEN, token);
            setCookie(USER_ID, resp.id);
            setState(['userId', 'fetchWithToken'], [resp.id, fetchAndSetToken(token)]);
        });
    } else {
        alert("Failed to login")
    }
};
