import {set as setCookie} from 'js-cookie'
import {findPath} from "../utils";
import {GET, POST} from "./constants";
import {context} from "../../Context";
import {CSRF_TOKEN} from "../../Context/constants";

export const fetchAndSet = async (url,
                                  setResult,
                                  method = GET,
                                  data = {},
                                  headers = {},
                                  type) => {
    let response;
    const pathData = findPath(type, null, context);
    pathData.data.inProcess = true;
    setResult(pathData);
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
        setResult(pathData);
    } else {
        const result = await response.text();
        const error = JSON.parse(result).message;
        pathData.data.error = error;
        pathData.data.inProcess = false;
        setResult(pathData);
        alert(error);
    }
};

const setGlobalState = (type, value, setToContext) => {
    let pathData = findPath(type, null, context);
    pathData.data.value = value;
    if (type === CSRF_TOKEN) {
        console.log(pathData,)
    }
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

export const loginFetch = async (url, data = {}, setCsrfToken, dispatch) => {
    await fetch(url, {
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
