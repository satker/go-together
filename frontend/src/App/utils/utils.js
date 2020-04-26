import {keys} from "lodash";

export const createEmptyResponse = (defaultProcess = true, defaultResponse = {}) => ({
    inProcess: defaultProcess,
    response: defaultResponse,
    error: ''
});

export const findPath = (findObj, path, components) => {
    for (const component of keys(components)) {
        let resultPath = null;
        let currentPath = (path ? path + '.' : '') + component;
        if (components[component] === findObj) {
            return currentPath;
        }
        if (!components[component]?.inProcess && components[component] instanceof Object) {
            resultPath = findPath(findObj, currentPath, components[component])
        }
        if (resultPath) {
            return resultPath;
        }
    }
};