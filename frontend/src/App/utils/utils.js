import {keys} from "lodash";

export const createEmptyResponse = (type, defaultResponse = {}, defaultProcess = false) => ({
    type,
    inProcess: defaultProcess,
    response: defaultResponse,
    error: ''
});

export const createContextValue = (type, value = null) => ({
    type,
    value
});

export const findPath = (findType, path, components) => {
    if (!findType) {
        return {
            path: null,
            data: createEmptyResponse()
        };
    }
    for (const component of keys(components)) {
        let resultPath = {
            path: null,
            data: null
        };
        let currentPath = (path ? path + '.' : '') + component;
        if (components[component].type === findType) {
            return {
                path: currentPath,
                data: {...components[component]}
            };
        }
        if (!components[component].hasOwnProperty('type') && components[component] instanceof Object) {
            resultPath = findPath(findType, currentPath, components[component])
        }
        if (resultPath.path && resultPath.data) {
            return resultPath;
        }
    }
    return {
        path: null,
        data: null
    };
};