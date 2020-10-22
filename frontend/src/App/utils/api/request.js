import {findPath} from "App/utils/utils";
import {context} from "App/Context";

import {GET} from "./constants";
import {NOTIFICATION} from "forms/utils/components/Notification/constants";

const resolveJson = (response) => response.json();

export const fetchAndSet = (url,
                            setResult,
                            method = GET,
                            data = {},
                            headers = {},
                            type,
                            setNotificationMessage) => {
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
        .then(resolveJson)
        .then(response => {
            pathData.data.response = response;
            pathData.data.inProcess = false;
            setResult(pathData);
        }).catch(errorMessage => Promise.resolve(errorMessage)
        .then(error => error.json())
        .then(exception => {
            pathData.data.error = exception;
            pathData.data.inProcess = false;
            setResult(pathData);
            setNotificationMessage(exception.message);
        }));
};

const setGlobalState = (type, value, setToContext) => {
    let pathData = findPath(type, null, context);
    if (pathData.data.response) {
        pathData.data.response = value;
    } else {
        pathData.data.value = value;
    }
    setToContext(pathData);
};

const setNotificationMessage = (setToContext) => (message) => {
    setGlobalState(NOTIFICATION, {isOpen: true, message}, setToContext)
}

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
        fetchAndSet(url, setToContext, method, data, headers, type, setNotificationMessage(setToContext));
    };