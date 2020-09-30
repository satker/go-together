import {findPath} from "App/utils/utils";
import {context} from "App/Context";

import {GET} from "./constants";
import {NOTIFICATION} from "forms/utils/components/Notification/constants";

const resolveStatus = (pathData, isToken, setResult) => (response) => {
    if (isToken) {
        response.headers
            .forEach(value => {
                if (value.startsWith('Bearer ')) {
                    pathData.data.token = value;
                    pathData.data.inProcess = false;
                    setResult(pathData);
                }
            });
    }
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
};

const resolveJson = (response) => {
    return response.json();
};

export const fetchAndSet = (url,
                            setResult,
                            method = GET,
                            data = {},
                            headers = {},
                            type,
                            isToken,
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
        .then(resolveStatus(pathData, isToken, setResult))
        .then(resolveJson)
        .then(response => {
            pathData.data.response = response;
            pathData.data.inProcess = false;
            setResult(pathData);
        }).catch(errorMessage => {
        if (errorMessage.name === 'SyntaxError') {
            return;
        }
        pathData.data.error = errorMessage;
        pathData.data.inProcess = false;
        setResult(pathData);
        setNotificationMessage(errorMessage.toString());
    });
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
    (setToContext) => ({type, url, method = GET, data = {}, value, isToken = false}) => {
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
        fetchAndSet(url, setToContext, method, data, headers, type, isToken, setNotificationMessage(setToContext));
    };