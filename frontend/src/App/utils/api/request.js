import {set as setCookie} from 'js-cookie'
import {findPath} from "../utils";
import {GET, POST} from "./constants";
import {context} from "../../Context";
import {CSRF_TOKEN} from "../../Context/constants";

const resolveStatus = (response) => {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
};

const resolveJson = (response) => {
    return response.json()
};

export const fetchAndSet = (url,
                            setResult,
                            method = GET,
                            data = {},
                            headers = {},
                            type) => {
    const pathData = findPath(type, null, context);
    pathData.data.inProcess = true;
    setResult(pathData);

    const additionalParams = method === GET ? {} : {
        headers,
        body: JSON.stringify(data)
    };

    const requestParams = {
        method: method,
        ...additionalParams
    };
    fetch(url, requestParams)
        .then(resolveStatus)
        .then(resolveJson)
        .then(response => {
            pathData.data.response = response;
            pathData.data.inProcess = false;
            setResult(pathData);
        }).catch(errorMessage => {
        pathData.data.error = errorMessage;
        pathData.data.inProcess = false;
        setResult(pathData);
        alert(errorMessage);
    });
};

const setGlobalState = (type, value, setToContext) => {
    let pathData = findPath(type, null, context);
    pathData.data.value = value;
    setToContext(pathData);
};

export const fetchAndSetToken = (token) =>
    (setToContext) => ({type, url, method = GET, data = {}, value}) => {
        if (!url) {
            setGlobalState(type, value, setToContext);
            return;
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

export const loginFetch = (url, data = {}, setCsrfToken, dispatch) => {
    fetch(url, {
        method: POST,
        body: JSON.stringify(data),
    }).then(response => {
        response.headers
            .forEach(value => {
                if (value.startsWith('Bearer ')) {
                    setCookie(CSRF_TOKEN, value);
                    setCsrfToken(value)(dispatch);
                }
            })
    }).catch(errorMessage => alert("Failed to login: " + errorMessage));
};
